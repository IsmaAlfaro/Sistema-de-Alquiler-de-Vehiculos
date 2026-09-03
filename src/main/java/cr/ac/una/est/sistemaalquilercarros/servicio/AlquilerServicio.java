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

    public Alquiler buscarPorNumero(int numeroAlquiler) {

        for (Alquiler alquiler :
                repositorioAlquileres.obtenerTodos()) {

            if (alquiler.getNumeroAlquiler() == numeroAlquiler) {
                return alquiler;
            }
        }

        return null;
    }

    public List<Alquiler> obtenerTodos() {

        return repositorioAlquileres.obtenerTodos();
    }

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

    public int cantidadAlquileresActivos() {

        return obtenerActivos().size();
    }

    public double getDepositoGarantia() {

        return DEPOSITO_GARANTIA;
    }

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