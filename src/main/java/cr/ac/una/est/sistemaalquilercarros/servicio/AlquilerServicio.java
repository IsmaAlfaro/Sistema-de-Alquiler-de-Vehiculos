package cr.ac.una.est.sistemaalquilercarros.servicio;

import cr.ac.una.est.sistemaalquilercarros.modelo.Alquiler;
import cr.ac.una.est.sistemaalquilercarros.modelo.Cliente;
import cr.ac.una.est.sistemaalquilercarros.modelo.EstadoAlquiler;
import cr.ac.una.est.sistemaalquilercarros.modelo.EstadoVehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.Vehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.ResultadoDevolucion;
import cr.ac.una.est.sistemaalquilercarros.persistencia.DatosSistema;
import cr.ac.una.est.sistemaalquilercarros.persistencia.PersistenciaDatos;
import cr.ac.una.est.sistemaalquilercarros.repositorio.Repositorio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AlquilerServicio {

    private static final double DEPOSITO_GARANTIA = 50000;

    private final Repositorio<Alquiler> repositorioAlquileres;
    private final DatosSistema datosSistema;
    private final PersistenciaDatos persistencia;
    private final VehiculoServicio vehiculoServicio;
    private final ClienteServicio clienteServicio;

    /*
     * Qué hace: Inicializa el servicio encargado de administrar los alquileres, enlazando los datos generales del sistema, la persistencia y los servicios de vehículos y clientes, además carga en el repositorio los alquileres que ya estaban guardados
     * Recibe: El objeto DatosSistema, el servicio de persistencia, el servicio de vehículos y el servicio de clientes
     * Retorna: No retorna ningún valor
     */
    public AlquilerServicio(DatosSistema datosSistema,
                            PersistenciaDatos persistencia,
                            VehiculoServicio vehiculoServicio,
                            ClienteServicio clienteServicio) {

        this.datosSistema = datosSistema;
        this.persistencia = persistencia;
        this.vehiculoServicio = vehiculoServicio;
        this.clienteServicio = clienteServicio;

        this.repositorioAlquileres = new Repositorio<>();

        this.repositorioAlquileres.reemplazarTodos(
                datosSistema.getAlquileres()
        );
    }

    /*
     * Qué hace: Actualiza en DatosSistema las listas actuales de alquileres, vehículos y clientes, y posteriormente guarda toda la información utilizando el mecanismo de persistencia
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    private void guardarCambios() {

        datosSistema.setAlquileres(
                repositorioAlquileres.obtenerTodos()
        );

        datosSistema.setVehiculos(
                vehiculoServicio.obtenerTodos()
        );

        datosSistema.setClientes(
                clienteServicio.obtenerTodos()
        );

        persistencia.guardar(datosSistema);
    }

    /*
     * Qué hace: Crea un nuevo alquiler después de validar que el cliente y el vehículo existan, que el vehículo esté disponible, que la fecha sea válida y que la cantidad de días sea correcta, luego cambia el vehículo a estado alquilado y guarda los cambios
     * Recibe: La cédula del cliente, la placa del vehículo, la fecha de inicio y la cantidad de días del alquiler
     * Retorna: El objeto Alquiler que fue creado y registrado en el sistema
     */
    public Alquiler crearAlquiler(String cedulaCliente,
                                  String placaVehiculo,
                                  LocalDate fechaInicio,
                                  int cantidadDias) {

        Cliente cliente =
                clienteServicio.buscarPorCedula(cedulaCliente);

        if (cliente == null) {
            throw new IllegalArgumentException(
                    "El cliente no existe."
            );
        }

        Vehiculo vehiculo =
                vehiculoServicio.buscarPorPlaca(placaVehiculo);

        if (vehiculo == null) {
            throw new IllegalArgumentException(
                    "El vehículo no existe."
            );
        }

        if (vehiculo.getEstado() != EstadoVehiculo.DISPONIBLE) {
            throw new IllegalArgumentException(
                    "El vehículo no está disponible."
            );
        }

        if (fechaInicio == null) {
            throw new IllegalArgumentException(
                    "La fecha de inicio es obligatoria."
            );
        }

        if (cantidadDias <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad de días debe ser mayor que cero."
            );
        }

        int numeroAlquiler =
                generarNumeroAlquiler();

        Alquiler alquiler =
                new Alquiler(
                        numeroAlquiler,
                        cliente,
                        vehiculo,
                        fechaInicio,
                        cantidadDias,
                        DEPOSITO_GARANTIA
                );

        repositorioAlquileres.agregar(alquiler);

        vehiculo.setEstado(
                EstadoVehiculo.ALQUILADO
        );

        guardarCambios();

        return alquiler;
    }

    /*
     * Qué hace: Procesa la devolución de un alquiler activo, valida la fecha de devolución, calcula los días de atraso, la multa, el depósito a devolver y cualquier saldo pendiente, finaliza el alquiler y vuelve a marcar el vehículo como disponible
     * Recibe: El número identificador del alquiler y la fecha en la que se devuelve el vehículo
     * Retorna: Un objeto ResultadoDevolucion con los resultados calculados durante la devolución
     */
    public ResultadoDevolucion devolverAlquiler(
            int numeroAlquiler,
            LocalDate fechaDevolucion) {

        Alquiler alquiler =
                buscarPorNumero(numeroAlquiler);

        if (alquiler == null) {
            throw new IllegalArgumentException(
                    "El alquiler no existe."
            );
        }

        if (alquiler.getEstado() == EstadoAlquiler.FINALIZADO) {
            throw new IllegalArgumentException(
                    "El alquiler ya fue finalizado."
            );
        }

        if (fechaDevolucion == null) {
            throw new IllegalArgumentException(
                    "La fecha de devolución es obligatoria."
            );
        }

        if (fechaDevolucion.isBefore(alquiler.getFechaInicio())) {

            throw new IllegalArgumentException(
                    "La fecha de devolución no puede ser anterior al inicio."
            );
        }

        long diasAtraso =
                alquiler.calcularDiasAtraso(fechaDevolucion);

        double multa =
                alquiler.calcularMulta(fechaDevolucion);

        double depositoDevuelto =
                alquiler.calcularDepositoADevolver(fechaDevolucion);

        double saldoPendiente =
                alquiler.calcularSaldoPendiente(fechaDevolucion);

        alquiler.finalizar(fechaDevolucion);

        alquiler.getVehiculo().setEstado(
                EstadoVehiculo.DISPONIBLE
        );

        guardarCambios();

        return new ResultadoDevolucion(
                diasAtraso,
                multa,
                depositoDevuelto,
                saldoPendiente
        );
    }

    /*
     * Qué hace: Busca dentro del repositorio de alquileres un registro cuyo número identificador coincida con el número proporcionado
     * Recibe: El número de alquiler que se desea buscar
     * Retorna: El alquiler encontrado o null si no existe un alquiler con ese número
     */
    public Alquiler buscarPorNumero(int numeroAlquiler) {

        for (Alquiler alquiler :
                repositorioAlquileres.obtenerTodos()) {

            if (alquiler.getNumeroAlquiler() == numeroAlquiler) {
                return alquiler;
            }
        }

        return null;
    }

    /*
     * Qué hace: Obtiene todos los alquileres almacenados actualmente dentro del repositorio del sistema
     * Recibe: No recibe parámetros
     * Retorna: La lista completa de alquileres registrados
     */
    public List<Alquiler> obtenerTodos() {

        return repositorioAlquileres.obtenerTodos();
    }

    /*
     * Qué hace: Recorre todos los alquileres registrados y construye una lista que contiene únicamente aquellos cuyo estado actual es ACTIVO
     * Recibe: No recibe parámetros
     * Retorna: Una lista con todos los alquileres que todavía se encuentran activos
     */
    public List<Alquiler> obtenerActivos() {

        List<Alquiler> activos = new ArrayList<>();

        for (Alquiler alquiler :
                repositorioAlquileres.obtenerTodos()) {

            if (alquiler.getEstado() == EstadoAlquiler.ACTIVO) {
                activos.add(alquiler);
            }
        }

        return activos;
    }

    /*
     * Qué hace: Determina cuántos alquileres se encuentran actualmente activos utilizando la lista generada por el método obtenerActivos
     * Recibe: No recibe parámetros
     * Retorna: La cantidad total de alquileres activos
     */
    public int cantidadAlquileresActivos() {

        return obtenerActivos().size();
    }

    /*
     * Qué hace: Obtiene el monto fijo establecido por el sistema como depósito de garantía para cada nuevo alquiler
     * Recibe: No recibe parámetros
     * Retorna: El monto utilizado como depósito de garantía
     */
    public double getDepositoGarantia() {

        return DEPOSITO_GARANTIA;
    }

    /*
     * Qué hace: Genera automáticamente el siguiente número de alquiler buscando el número más alto registrado actualmente y aumentando ese valor en uno
     * Recibe: No recibe parámetros
     * Retorna: El siguiente número disponible que debe asignarse a un nuevo alquiler
     */
    private int generarNumeroAlquiler() {

        int mayorNumero = 0;

        for (Alquiler alquiler :
                repositorioAlquileres.obtenerTodos()) {

            if (alquiler.getNumeroAlquiler() > mayorNumero) {
                mayorNumero = alquiler.getNumeroAlquiler();
            }
        }

        return mayorNumero + 1;
    }
}