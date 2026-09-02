package cr.ac.una.est.sistemaalquilercarros.modelo;

public class Pickup extends Vehiculo {

    private double capacidadCarga;
    private String tipoTraccion;

    public Pickup(String placa, String marca, String modelo,
                  int anio, double tarifaDiaria,
                  double capacidadCarga, String tipoTraccion) {

        super(placa, marca, modelo, anio, tarifaDiaria);

        this.capacidadCarga = capacidadCarga;
        this.tipoTraccion = tipoTraccion;
    }

    public double getCapacidadCarga() {
        return capacidadCarga;
    }

    public void setCapacidadCarga(double capacidadCarga) {
        this.capacidadCarga = capacidadCarga;
    }

    public String getTipoTraccion() {
        return tipoTraccion;
    }

    public void setTipoTraccion(String tipoTraccion) {
        this.tipoTraccion = tipoTraccion;
    }

    @Override
    public String toString() {
        return getPlaca() + " - " + getMarca() + " - " + getModelo();
    }
}