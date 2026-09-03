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

    public Alquiler(int numeroAlquiler, Cliente cliente, Vehiculo vehiculo, LocalDate fechaInicio, int cantidadDias, double depositoGarantia) {

        this.numeroAlquiler = numeroAlquiler;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.fechaInicio = fechaInicio;
        this.cantidadDias = cantidadDias;

        this.fechaDevolucionEsperada =
                fechaInicio.plusDays(cantidadDias);

        this.tarifaDiariaAplicada =
                vehiculo.getTarifaDiaria();

        this.depositoGarantia = depositoGarantia;

        this.estado = EstadoAlquiler.ACTIVO;
    }

    public double calcularSubtotal() {
        return tarifaDiariaAplicada * cantidadDias;
    }

    @Override
    public double calcularTotal() {
        return calcularSubtotal() + depositoGarantia;
    }

    public long calcularDiasAtraso(LocalDate fechaDevolucion) {

        if (!fechaDevolucion.isAfter(fechaDevolucionEsperada)) {
            return 0;
        }

        return ChronoUnit.DAYS.between(
                fechaDevolucionEsperada,
                fechaDevolucion
        );
    }

    public double calcularMulta(LocalDate fechaDevolucion) {

        long diasAtraso =
                calcularDiasAtraso(fechaDevolucion);

        return tarifaDiariaAplicada * diasAtraso;
    }

    public double calcularDepositoADevolver(LocalDate fechaDevolucion) {

        double multa = calcularMulta(fechaDevolucion);

        return Math.max(
                0,
                depositoGarantia - multa
        );
    }

    public double calcularSaldoPendiente(LocalDate fechaDevolucion) {

        double multa = calcularMulta(fechaDevolucion);

        return Math.max(
                0,
                multa - depositoGarantia
        );
    }

    public void finalizar(LocalDate fechaDevolucionReal) {

        this.fechaDevolucionReal = fechaDevolucionReal;
        this.estado = EstadoAlquiler.FINALIZADO;
    }

    public int getNumeroAlquiler() {
        return numeroAlquiler;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public int getCantidadDias() {
        return cantidadDias;
    }

    public LocalDate getFechaDevolucionEsperada() {
        return fechaDevolucionEsperada;
    }

    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public double getTarifaDiariaAplicada() {
        return tarifaDiariaAplicada;
    }

    public double getDepositoGarantia() {
        return depositoGarantia;
    }

    public EstadoAlquiler getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return "Alquiler #" + numeroAlquiler + " - " + cliente.getNombreCompleto() + " - " + vehiculo.getPlaca();
    }
}