package cr.ac.una.est.sistemaalquilercarros.modelo;

import cr.ac.una.est.sistemaalquilercarros.interfaces.Calculable;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Alquiler implements Calculable, Serializable {

    private int numeroAlquiler;
    private Cliente cliente;
    private Vehiculo vehiculo;
    private LocalDate fechaInicio;
    private int cantidadDias;
    private LocalDate fechaDevolucionEsperada;
    private LocalDate fechaDevolucionReal;
    private double tarifaDiariaAplicada;
    private double depositoGarantia;
    private EstadoAlquiler estado;
    private MetodoPago metodoPago;
    private Double montoPagado;
    private Double montoRecibido;
    private Double vuelto;
    private MetodoPago metodoPagoDevolucion;
    private Double montoPagadoDevolucion;
    private Double montoRecibidoDevolucion;
    private Double vueltoDevolucion;

    /*
     * Qué hace: Crea un nuevo alquiler utilizando los datos del cliente, el vehículo seleccionado, la fecha de inicio y la cantidad de días, además calcula la fecha esperada de devolución, guarda la tarifa diaria actual del vehículo y establece el alquiler inicialmente como activo
     * Recibe: El número de alquiler, el cliente, el vehículo, la fecha de inicio, la cantidad de días y el depósito de garantía
     * Retorna: No retorna ningún valor
     */
    public Alquiler(int numeroAlquiler, Cliente cliente, Vehiculo vehiculo, LocalDate fechaInicio, int cantidadDias, double depositoGarantia) {

        this(numeroAlquiler, cliente, vehiculo, fechaInicio, cantidadDias, null, vehiculo.getTarifaDiaria(), depositoGarantia, EstadoAlquiler.ACTIVO);

    }

    /*
     * Qué hace: Reconstruye un alquiler utilizando todos sus datos guardados, incluyendo la fecha real de devolución, la tarifa diaria aplicada originalmente, el depósito de garantía y el estado del alquiler
     * Recibe: El número de alquiler, el cliente, el vehículo, la fecha de inicio, la cantidad de días, la fecha real de devolución, la tarifa diaria aplicada, el depósito de garantía y el estado del alquiler
     * Retorna: No retorna ningún valor
     */
    public Alquiler(int numeroAlquiler, Cliente cliente, Vehiculo vehiculo, LocalDate fechaInicio, int cantidadDias, LocalDate fechaDevolucionReal, double tarifaDiariaAplicada, double depositoGarantia, EstadoAlquiler estado) {

        this(numeroAlquiler, cliente, vehiculo, fechaInicio, cantidadDias, fechaDevolucionReal, tarifaDiariaAplicada, depositoGarantia, estado, null, null, null, null);

    }

    /*
     * Qué hace: Reconstruye un alquiler utilizando todos sus datos guardados, incluyendo la información de devolución, el estado y los datos relacionados con la forma de pago utilizada
     * Recibe: El número de alquiler, el cliente, el vehículo, la fecha de inicio, la cantidad de días, la fecha real de devolución, la tarifa diaria aplicada, el depósito de garantía, el estado, el método de pago, el monto pagado, el monto recibido y el vuelto
     * Retorna: No retorna ningún valor
     */
    public Alquiler(int numeroAlquiler, Cliente cliente, Vehiculo vehiculo, LocalDate fechaInicio, int cantidadDias, LocalDate fechaDevolucionReal, double tarifaDiariaAplicada, double depositoGarantia, EstadoAlquiler estado, MetodoPago metodoPago, Double montoPagado, Double montoRecibido, Double vuelto) {

        this.numeroAlquiler = numeroAlquiler;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.fechaInicio = fechaInicio;
        this.cantidadDias = cantidadDias;
        this.fechaDevolucionEsperada = fechaInicio.plusDays(cantidadDias);
        this.fechaDevolucionReal = fechaDevolucionReal;
        this.tarifaDiariaAplicada = tarifaDiariaAplicada;
        this.depositoGarantia = depositoGarantia;
        this.estado = estado;
        this.metodoPago = metodoPago;
        this.montoPagado = montoPagado;
        this.montoRecibido = montoRecibido;
        this.vuelto = vuelto;

    }

    /*
     * Qué hace: Calcula el costo base del alquiler multiplicando la tarifa diaria que tenía el vehículo al momento de realizar el alquiler por la cantidad de días solicitados
     * Recibe: No recibe parámetros
     * Retorna: El subtotal correspondiente al alquiler sin incluir el depósito de garantía
     */
    public double calcularSubtotal() {

        return tarifaDiariaAplicada * cantidadDias;

    }

    /*
     * Qué hace: Calcula el monto total inicial que debe pagarse por el alquiler sumando el subtotal de los días alquilados y el depósito de garantía
     * Recibe: No recibe parámetros
     * Retorna: El monto total del alquiler incluyendo el depósito de garantía
     */
    @Override
    public double calcularTotal() {

        return calcularSubtotal() + depositoGarantia;

    }

    /*
     * Qué hace: Determina cuántos días de atraso existen comparando la fecha real de devolución indicada con la fecha esperada de devolución del vehículo
     * Recibe: La fecha en la que se está devolviendo el vehículo
     * Retorna: La cantidad de días de atraso, o cero si el vehículo fue devuelto a tiempo
     */
    public long calcularDiasAtraso(LocalDate fechaDevolucion) {

        if (!fechaDevolucion.isAfter(fechaDevolucionEsperada)) {

            return 0;

        }

        return ChronoUnit.DAYS.between(fechaDevolucionEsperada, fechaDevolucion);

    }

    /*
     * Qué hace: Calcula la multa que debe pagar el cliente por devolver el vehículo después de la fecha establecida, utilizando la tarifa diaria por cada día de atraso
     * Recibe: La fecha en la que se está devolviendo el vehículo
     * Retorna: El monto correspondiente a la multa por atraso
     */
    public double calcularMulta(LocalDate fechaDevolucion) {

        long diasAtraso = calcularDiasAtraso(fechaDevolucion);

        return tarifaDiariaAplicada * diasAtraso;

    }

    /*
     * Qué hace: Calcula cuánto dinero del depósito de garantía debe devolverse al cliente después de descontar cualquier multa generada por días de atraso
     * Recibe: La fecha en la que se está devolviendo el vehículo
     * Retorna: El monto del depósito que debe devolverse al cliente
     */
    public double calcularDepositoADevolver(LocalDate fechaDevolucion) {

        double multa = calcularMulta(fechaDevolucion);

        return Math.max(0, depositoGarantia - multa);

    }

    /*
     * Qué hace: Calcula si el cliente todavía debe pagar dinero adicional cuando la multa por atraso supera el monto disponible en el depósito de garantía
     * Recibe: La fecha en la que se está devolviendo el vehículo
     * Retorna: El saldo adicional que el cliente debe pagar, o cero si el depósito cubre completamente la multa
     */
    public double calcularSaldoPendiente(LocalDate fechaDevolucion) {

        double multa = calcularMulta(fechaDevolucion);

        return Math.max(0, multa - depositoGarantia);

    }

    /*
     * Qué hace: Finaliza el alquiler registrando la fecha real en que fue devuelto el vehículo y cambiando el estado del alquiler de activo a finalizado
     * Recibe: La fecha real en la que se realizó la devolución del vehículo
     * Retorna: No retorna ningún valor
     */
    public void finalizar(LocalDate fechaDevolucionReal) {

        this.fechaDevolucionReal = fechaDevolucionReal;

        this.estado = EstadoAlquiler.FINALIZADO;

    }

    /*
     * Qué hace: Registra la información relacionada con el pago realizado al finalizar el alquiler
     * Recibe: El método de pago utilizado, el monto pagado, el monto recibido en efectivo y el vuelto correspondiente
     * Retorna: No retorna ningún valor
     */
    public void registrarPago(MetodoPago metodoPago, double montoPagado, Double montoRecibido, Double vuelto) {

        this.metodoPago = metodoPago;

        this.montoPagado = montoPagado;

        this.montoRecibido = montoRecibido;

        this.vuelto = vuelto;

    }

    /*
     * Qué hace: Registra la información relacionada con el pago realizado al finalizar la devolucion
     * Recibe: El método de pago utilizado, el monto pagado, el monto recibido en efectivo y el vuelto correspondiente
     * Retorna: No retorna ningún valor
     */
    public void registrarPagoDevolucion(MetodoPago metodoPagoDevolucion, double montoPagadoDevolucion, Double montoRecibidoDevolucion, Double vueltoDevolucion) {

        this.metodoPagoDevolucion = metodoPagoDevolucion;

        this.montoPagadoDevolucion = montoPagadoDevolucion;

        this.montoRecibidoDevolucion = montoRecibidoDevolucion;

        this.vueltoDevolucion = vueltoDevolucion;

    }

    /*
     * Qué hace: Obtiene el número identificador asignado al alquiler para poder reconocerlo dentro del sistema
     * Recibe: No recibe parámetros
     * Retorna: El número identificador del alquiler
     */
    public int getNumeroAlquiler() {

        return numeroAlquiler;

    }

    /*
     * Qué hace: Obtiene el cliente que se encuentra asociado con este alquiler
     * Recibe: No recibe parámetros
     * Retorna: El cliente relacionado con el alquiler
     */
    public Cliente getCliente() {

        return cliente;

    }

    /*
     * Qué hace: Obtiene el vehículo que fue seleccionado y asociado con este alquiler
     * Recibe: No recibe parámetros
     * Retorna: El vehículo relacionado con el alquiler
     */
    public Vehiculo getVehiculo() {

        return vehiculo;

    }

    /*
     * Qué hace: Obtiene la fecha en la que inició oficialmente el alquiler del vehículo
     * Recibe: No recibe parámetros
     * Retorna: La fecha de inicio del alquiler
     */
    public LocalDate getFechaInicio() {

        return fechaInicio;

    }

    /*
     * Qué hace: Obtiene la cantidad de días por los cuales el cliente solicitó alquilar el vehículo
     * Recibe: No recibe parámetros
     * Retorna: La cantidad de días establecidos para el alquiler
     */
    public int getCantidadDias() {

        return cantidadDias;

    }

    /*
     * Qué hace: Obtiene la fecha calculada en la que el cliente debería devolver el vehículo según la duración del alquiler
     * Recibe: No recibe parámetros
     * Retorna: La fecha esperada de devolución del vehículo
     */
    public LocalDate getFechaDevolucionEsperada() {

        return fechaDevolucionEsperada;

    }

    /*
     * Qué hace: Obtiene la fecha en la que el vehículo fue realmente devuelto al finalizar el alquiler
     * Recibe: No recibe parámetros
     * Retorna: La fecha real de devolución del vehículo
     */
    public LocalDate getFechaDevolucionReal() {

        return fechaDevolucionReal;

    }

    /*
     * Qué hace: Obtiene la tarifa diaria que fue guardada y aplicada al vehículo cuando se realizó originalmente el alquiler
     * Recibe: No recibe parámetros
     * Retorna: La tarifa diaria utilizada para calcular el alquiler
     */
    public double getTarifaDiariaAplicada() {

        return tarifaDiariaAplicada;

    }

    /*
     * Qué hace: Obtiene el monto del depósito de garantía que fue cobrado al cliente al realizar el alquiler
     * Recibe: No recibe parámetros
     * Retorna: El monto correspondiente al depósito de garantía
     */
    public double getDepositoGarantia() {

        return depositoGarantia;

    }

    /*
     * Qué hace: Obtiene el estado actual del alquiler para conocer si todavía se encuentra activo o si ya fue finalizado
     * Recibe: No recibe parámetros
     * Retorna: El estado actual del alquiler
     */
    public EstadoAlquiler getEstado() {

        return estado;

    }

    /*
     * Qué hace: Obtiene el método de pago utilizado para finalizar el alquiler
     * Recibe: No recibe parámetros
     * Retorna: El método de pago utilizado o null si el alquiler todavía no ha sido pagado
     */
    public MetodoPago getMetodoPago() {

        return metodoPago;

    }

    /*
     * Qué hace: Obtiene el monto que el cliente debía pagar al momento de finalizar el alquiler
     * Recibe: No recibe parámetros
     * Retorna: El monto pagado o null si todavía no se ha registrado ningún pago
     */
    public Double getMontoPagado() {

        return montoPagado;

    }

    /*
     * Qué hace: Obtiene la cantidad de dinero en efectivo entregada por el cliente al realizar el pago
     * Recibe: No recibe parámetros
     * Retorna: El monto recibido en efectivo o null cuando el pago no fue realizado en efectivo
     */
    public Double getMontoRecibido() {

        return montoRecibido;

    }

    /*
     * Qué hace: Obtiene el monto que debe devolverse al cliente como vuelto cuando el pago fue realizado en efectivo
     * Recibe: No recibe parámetros
     * Retorna: El vuelto correspondiente o null cuando el pago no fue realizado en efectivo
     */
    public Double getVuelto() {

        return vuelto;

    }

    /*
     * Qué hace: Obtiene el método de pago utilizado por el cliente al realizar el pago de la devolución
     * Recibe: No recibe parámetros
     * Retorna: El método de pago utilizado en la devolución
     */
    public MetodoPago getMetodoPagoDevolucion() {

        return metodoPagoDevolucion;

    }

    /*
     * Qué hace: Obtiene el monto que debe devolverse al cliente como vuelto por el pago de la devolución
     * Recibe: No recibe parámetros
     * Retorna: El vuelto correspondiente o null cuando el pago no fue realizado en efectivo
     */
    public Double getVueltoDevolucion() {

        return vueltoDevolucion;

    }

    /*
     * Qué hace: Obtiene la cantidad de dinero en efectivo entregada por el cliente al realizar el pago de la devolución
     * Recibe: No recibe parámetros
     * Retorna: El monto recibido en efectivo o null cuando el pago no fue realizado en efectivo
     */
    public Double getMontoRecibidoDevolucion() {

        return montoRecibidoDevolucion;

    }

    /*
     * Qué hace: Obtiene el monto total pagado por el cliente al realizar la devolución del vehículo
     * Recibe: No recibe parámetros
     * Retorna: El monto pagado correspondiente a la devolución
     */
    public Double getMontoPagadoDevolucion() {

        return montoPagadoDevolucion;

    }

    /*
     * Qué hace: Genera una representación en texto del alquiler mostrando su número, el nombre del cliente y la placa del vehículo asociado
     * Recibe: No recibe parámetros
     * Retorna: Una cadena de texto que representa de forma resumida el alquiler
     */
    @Override
    public String toString() {

        return "Alquiler #" + numeroAlquiler + " - " + cliente.getNombreCompleto() + " - " + vehiculo.getPlaca();

    }

}