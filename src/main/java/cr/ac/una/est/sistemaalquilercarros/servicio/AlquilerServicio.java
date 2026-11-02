package cr.ac.una.est.sistemaalquilercarros.servicio;

import cr.ac.una.est.sistemaalquilercarros.modelo.Alquiler;
import cr.ac.una.est.sistemaalquilercarros.modelo.Cliente;
import cr.ac.una.est.sistemaalquilercarros.modelo.EstadoAlquiler;
import cr.ac.una.est.sistemaalquilercarros.modelo.EstadoVehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.MetodoPago;
import cr.ac.una.est.sistemaalquilercarros.modelo.ResultadoDevolucion;
import cr.ac.una.est.sistemaalquilercarros.modelo.Vehiculo;
import cr.ac.una.est.sistemaalquilercarros.repositorio.AlquilerRepositorio;

import java.time.LocalDate;
import java.util.List;

public class AlquilerServicio {

    private static final double DEPOSITO_GARANTIA = 50000;

    private final AlquilerRepositorio alquilerRepositorio;
    private final VehiculoServicio vehiculoServicio;
    private final ClienteServicio clienteServicio;

    /*
     * Qué hace: Inicializa el servicio encargado de administrar los alquileres utilizando el repositorio de alquileres y los servicios de vehículos y clientes
     * Recibe: El repositorio encargado de realizar las operaciones de alquileres, el servicio de vehículos y el servicio de clientes
     * Retorna: No retorna ningún valor
     */
    public AlquilerServicio(AlquilerRepositorio alquilerRepositorio, VehiculoServicio vehiculoServicio, ClienteServicio clienteServicio) {

        this.alquilerRepositorio = alquilerRepositorio;

        this.vehiculoServicio = vehiculoServicio;

        this.clienteServicio = clienteServicio;

    }

    /*
     * Qué hace: Crea un nuevo alquiler después de validar que el cliente y el vehículo existan, que el vehículo esté disponible, que la fecha sea válida, que la cantidad de días sea correcta y que el pago inicial sea válido, luego guarda el alquiler mediante el repositorio y cambia el vehículo a estado alquilado
     * Recibe: La cédula del cliente, la placa del vehículo, la fecha de inicio, la cantidad de días del alquiler, el método de pago utilizado y el monto recibido cuando el pago se realiza en efectivo
     * Retorna: El objeto Alquiler que fue creado y registrado en el sistema con el número generado por la base de datos
     */
    public Alquiler crearAlquiler(String cedulaCliente, String placaVehiculo, LocalDate fechaInicio, int cantidadDias, MetodoPago metodoPago, Double montoRecibido) {

        Cliente cliente = clienteServicio.buscarPorCedula(cedulaCliente);

        if (cliente == null) {

            throw new IllegalArgumentException("El cliente no existe.");

        }

        Vehiculo vehiculo = vehiculoServicio.buscarPorPlaca(placaVehiculo);

        if (vehiculo == null) {

            throw new IllegalArgumentException("El vehículo no existe.");

        }

        if (vehiculo.getEstado() != EstadoVehiculo.DISPONIBLE) {

            throw new IllegalArgumentException("El vehículo no está disponible.");

        }

        if (fechaInicio == null) {

            throw new IllegalArgumentException("La fecha de inicio es obligatoria.");

        }

        if (cantidadDias <= 0) {

            throw new IllegalArgumentException("La cantidad de días debe ser mayor que cero.");

        }

        if (metodoPago == null) {

            throw new IllegalArgumentException("Debe seleccionar una forma de pago.");

        }

        double total = vehiculo.getTarifaDiaria() * cantidadDias + DEPOSITO_GARANTIA;

        Double vuelto = null;

        if (metodoPago == MetodoPago.EFECTIVO) {

            if (montoRecibido == null) {

                throw new IllegalArgumentException("Debe ingresar el monto recibido en efectivo.");

            }

            if (montoRecibido < 0) {

                throw new IllegalArgumentException("El monto recibido no puede ser negativo.");

            }

            if (montoRecibido % 10 != 0) {

                throw new IllegalArgumentException("El monto recibido debe ser múltiplo de ₡10.");

            }

            if (montoRecibido < total) {

                throw new IllegalArgumentException("El monto recibido es menor al total del alquiler.");

            }

            vuelto = montoRecibido - total;

        }

        Alquiler alquiler = new Alquiler(0, cliente, vehiculo, fechaInicio, cantidadDias, DEPOSITO_GARANTIA);

        alquiler.registrarPago(metodoPago, total, metodoPago == MetodoPago.EFECTIVO ? montoRecibido : null, metodoPago == MetodoPago.EFECTIVO ? vuelto : null);

        return alquilerRepositorio.agregar(alquiler);

    }

    /*
     * Qué hace: Procesa la devolución de un alquiler activo, valida la fecha de devolución, calcula los días de atraso, la multa, el depósito a devolver y cualquier saldo pendiente, registra un pago adicional únicamente cuando la multa supera el depósito de garantía y finaliza el alquiler mediante el repositorio
     * Recibe: El número identificador del alquiler, la fecha en la que se devuelve el vehículo, el método de pago utilizado para un saldo adicional y el monto recibido cuando ese pago se realiza en efectivo
     * Retorna: Un objeto ResultadoDevolucion con los resultados calculados durante la devolución
     */
    public ResultadoDevolucion devolverAlquiler(int numeroAlquiler, LocalDate fechaDevolucion, MetodoPago metodoPago, Double montoRecibido) {

        Alquiler alquiler = buscarPorNumero(numeroAlquiler);

        if (alquiler == null) {

            throw new IllegalArgumentException("El alquiler no existe.");

        }

        if (alquiler.getEstado() == EstadoAlquiler.FINALIZADO) {

            throw new IllegalArgumentException("El alquiler ya fue finalizado.");

        }

        if (fechaDevolucion == null) {

            throw new IllegalArgumentException("La fecha de devolución es obligatoria.");

        }

        if (fechaDevolucion.isBefore(alquiler.getFechaInicio())) {

            throw new IllegalArgumentException("La fecha de devolución no puede ser anterior al inicio.");

        }

        long diasAtraso = alquiler.calcularDiasAtraso(fechaDevolucion);

        double multa = alquiler.calcularMulta(fechaDevolucion);

        double depositoDevuelto = alquiler.calcularDepositoADevolver(fechaDevolucion);

        double saldoPendiente = alquiler.calcularSaldoPendiente(fechaDevolucion);

        if (saldoPendiente > 0) {

            if (metodoPago == null) {

                throw new IllegalArgumentException("Debe seleccionar una forma de pago para cancelar el saldo pendiente.");

            }

            Double vuelto = null;

            if (metodoPago == MetodoPago.EFECTIVO) {

                if (montoRecibido == null) {

                    throw new IllegalArgumentException("Debe ingresar el monto recibido en efectivo.");

                }

                if (montoRecibido < 0) {

                    throw new IllegalArgumentException("El monto recibido no puede ser negativo.");

                }

                if (montoRecibido % 10 != 0) {

                    throw new IllegalArgumentException("El monto recibido debe ser múltiplo de ₡10.");

                }

                if (montoRecibido < saldoPendiente) {

                    throw new IllegalArgumentException("El monto recibido es menor al saldo pendiente.");

                }

                vuelto = montoRecibido - saldoPendiente;

            }

            alquiler.registrarPagoDevolucion(metodoPago, saldoPendiente, metodoPago == MetodoPago.EFECTIVO ? montoRecibido : null, metodoPago == MetodoPago.EFECTIVO ? vuelto : null);

        }

        alquiler.finalizar(fechaDevolucion);

        alquilerRepositorio.finalizar(alquiler);

        return new ResultadoDevolucion(diasAtraso, multa, depositoDevuelto, saldoPendiente);

    }

    /*
     * Qué hace: Busca un alquiler utilizando su número identificador mediante el repositorio de alquileres
     * Recibe: El número de alquiler que se desea buscar
     * Retorna: El alquiler encontrado o null si no existe un alquiler con ese número
     */
    public Alquiler buscarPorNumero(int numeroAlquiler) {

        return alquilerRepositorio.buscarPorNumero(numeroAlquiler);

    }

    /*
     * Qué hace: Obtiene todos los alquileres almacenados actualmente mediante el repositorio de alquileres
     * Recibe: No recibe parámetros
     * Retorna: La lista completa de alquileres registrados
     */
    public List<Alquiler> obtenerTodos() {

        return alquilerRepositorio.obtenerTodos();

    }

    /*
     * Qué hace: Obtiene mediante el repositorio todos los alquileres cuyo estado actual es ACTIVO
     * Recibe: No recibe parámetros
     * Retorna: Una lista con todos los alquileres que todavía se encuentran activos
     */
    public List<Alquiler> obtenerActivos() {

        return alquilerRepositorio.obtenerActivos();

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

}