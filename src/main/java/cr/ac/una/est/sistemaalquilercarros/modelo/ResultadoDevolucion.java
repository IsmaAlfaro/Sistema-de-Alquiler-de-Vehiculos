package cr.ac.una.est.sistemaalquilercarros.modelo;

public class ResultadoDevolucion {

    private final long diasAtraso;
    private final double multa;
    private final double depositoDevuelto;
    private final double saldoPendiente;

    public ResultadoDevolucion(long diasAtraso,
                               double multa,
                               double depositoDevuelto,
                               double saldoPendiente) {

        this.diasAtraso = diasAtraso;
        this.multa = multa;
        this.depositoDevuelto = depositoDevuelto;
        this.saldoPendiente = saldoPendiente;
    }

    public long getDiasAtraso() {
        return diasAtraso;
    }

    public double getMulta() {
        return multa;
    }

    public double getDepositoDevuelto() {
        return depositoDevuelto;
    }

    public double getSaldoPendiente() {
        return saldoPendiente;
    }
}