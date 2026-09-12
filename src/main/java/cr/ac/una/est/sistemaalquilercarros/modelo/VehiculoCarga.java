package cr.ac.una.est.sistemaalquilercarros.modelo;

public class VehiculoCarga extends Vehiculo {

    private double capacidadCarga;
    private String tipoTraccion;

    public VehiculoCarga(String placa, String marca, String modelo, int anio, double tarifaDiaria, TipoVehiculo tipoVehiculo, double capacidadCarga, String tipoTraccion) {
        super(placa, marca, modelo, anio, tarifaDiaria, tipoVehiculo);

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