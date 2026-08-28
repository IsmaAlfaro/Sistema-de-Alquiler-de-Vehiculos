package modelo;

/**
 * Representa una operación de alquiler dentro del sistema.
 * Guarda la información del cliente, el carro, la cantidad
 * de días, el depósito de garantía y el estado del alquiler.
 */
public class Alquiler {

    private int numeroAlquiler;
    private Cliente cliente;
    private Carro carro;
    private int cantidadDias;
    private double depositoGarantia;
    private boolean activo;

    /**
     * Crea un nuevo alquiler.
     *
     * @param numeroAlquiler identificador del alquiler.
     * @param cliente cliente que realiza el alquiler.
     * @param carro carro que será alquilado.
     * @param cantidadDias cantidad de días del alquiler.
     * @param depositoGarantia monto del depósito de garantía.
     */
    public Alquiler(int numeroAlquiler, Cliente cliente, Carro carro,
                    int cantidadDias, double depositoGarantia) {

        this.numeroAlquiler = numeroAlquiler;
        this.cliente = cliente;
        this.carro = carro;
        this.cantidadDias = cantidadDias;
        this.depositoGarantia = depositoGarantia;
        this.activo = true;
    }

    /**
     * Calcula el subtotal del alquiler sin incluir el depósito.
     *
     * @return tarifa diaria del carro multiplicada por
     * la cantidad de días.
     */
    public double calcularSubtotal() {
        return carro.getTarifaDiaria() * cantidadDias;
    }

    /**
     * Calcula el total inicial del alquiler.
     *
     * @return subtotal más depósito de garantía.
     */
    public double calcularTotal() {
        return calcularSubtotal() + depositoGarantia;
    }

    /**
     * Calcula la multa correspondiente a los días de atraso.
     *
     * @param diasAtraso cantidad de días de atraso.
     * @return monto de la multa.
     */
    public double calcularMulta(int diasAtraso) {

        if (diasAtraso <= 0) {
            return 0;
        }

        return carro.getTarifaDiaria() * diasAtraso;
    }

    /**
     * Marca el alquiler como finalizado.
     *
     * @return no retorna ningún valor.
     */
    public void finalizar() {
        activo = false;
    }

    /**
     * Obtiene el número del alquiler.
     *
     * @return número identificador del alquiler.
     */
    public int getNumeroAlquiler() {
        return numeroAlquiler;
    }

    /**
     * Obtiene el cliente asociado al alquiler.
     *
     * @return cliente del alquiler.
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Obtiene el carro asociado al alquiler.
     *
     * @return carro alquilado.
     */
    public Carro getCarro() {
        return carro;
    }

    /**
     * Obtiene la cantidad de días del alquiler.
     *
     * @return cantidad de días.
     */
    public int getCantidadDias() {
        return cantidadDias;
    }

    /**
     * Obtiene el depósito de garantía.
     *
     * @return monto del depósito.
     */
    public double getDepositoGarantia() {
        return depositoGarantia;
    }

    /**
     * Indica si el alquiler sigue activo.
     *
     * @return true si está activo o false si ya finalizó.
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * Devuelve una representación resumida del alquiler.
     *
     * @return texto con número de alquiler, cliente y placa del carro.
     */
    @Override
    public String toString() {
        return "Alquiler #" + numeroAlquiler
                + " - " + cliente.getNombre()
                + " - " + carro.getPlaca();
    }
}