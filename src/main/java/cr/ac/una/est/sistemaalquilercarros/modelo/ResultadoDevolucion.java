package cr.ac.una.est.sistemaalquilercarros.modelo;

public class ResultadoDevolucion {

    private final long diasAtraso;
    private final double multa;
    private final double depositoDevuelto;
    private final double saldoPendiente;

    /*
     * Qué hace: Crea un resultado de devolución almacenando todos los valores calculados al finalizar un alquiler, incluyendo atraso, multa, depósito devuelto y cualquier saldo pendiente
     * Recibe: Los días de atraso, el monto de la multa, el depósito que será devuelto y el saldo pendiente
     * Retorna: No retorna ningún valor
     */
    public ResultadoDevolucion(long diasAtraso,
                               double multa,
                               double depositoDevuelto,
                               double saldoPendiente) {

        this.diasAtraso = diasAtraso;
        this.multa = multa;
        this.depositoDevuelto = depositoDevuelto;
        this.saldoPendiente = saldoPendiente;
    }

    /*
     * Qué hace: Obtiene la cantidad de días de atraso que fueron determinados al procesar la devolución del vehículo
     * Recibe: No recibe parámetros
     * Retorna: La cantidad de días de atraso registrados
     */
    public long getDiasAtraso() {
        return diasAtraso;
    }

    /*
     * Qué hace: Obtiene el monto de la multa calculada debido a los días de atraso en la devolución del vehículo
     * Recibe: No recibe parámetros
     * Retorna: El monto de la multa aplicada
     */
    public double getMulta() {
        return multa;
    }

    /*
     * Qué hace: Obtiene el monto del depósito de garantía que debe ser devuelto al cliente después de aplicar las multas correspondientes
     * Recibe: No recibe parámetros
     * Retorna: El monto del depósito que será devuelto
     */
    public double getDepositoDevuelto() {
        return depositoDevuelto;
    }

    /*
     * Qué hace: Obtiene el monto adicional que el cliente todavía debe pagar cuando la multa supera el depósito de garantía disponible
     * Recibe: No recibe parámetros
     * Retorna: El saldo pendiente que debe pagar el cliente
     */
    public double getSaldoPendiente() {
        return saldoPendiente;
    }
}