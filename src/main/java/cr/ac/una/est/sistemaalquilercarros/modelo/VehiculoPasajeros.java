package cr.ac.una.est.sistemaalquilercarros.modelo;

public class VehiculoPasajeros extends Vehiculo {

    private int cantidadPasajeros;
    private String tipoTraccion;

    public VehiculoPasajeros(String placa, String marca, String modelo, int anio, double tarifaDiaria, TipoVehiculo tipoVehiculo, int cantidadPasajeros, String tipoTraccion) {
        super(placa, marca, modelo, anio, tarifaDiaria, tipoVehiculo);

        this.cantidadPasajeros = cantidadPasajeros;
        this.tipoTraccion = tipoTraccion;
    }

    public int getCantidadPasajeros() {
        return cantidadPasajeros;
    }

    public void setCantidadPasajeros(int cantidadPasajeros) {
        this.cantidadPasajeros = cantidadPasajeros;
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