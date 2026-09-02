package cr.ac.una.est.sistemaalquilercarros.modelo;

public class Sedan extends Vehiculo {

    private int cantidaPasajeros;

    public Sedan(String placa, String marca, String modelo,
                 int anio, double tarifaDiaria, int cantidaPasajeros) {

        super(placa, marca, modelo, anio, tarifaDiaria);
        this.cantidaPasajeros = cantidaPasajeros;
    }

    public int getCantidaPasajeros() {
        return cantidaPasajeros;
    }

    public void setCantidaPasajeros(int cantidaPasajeros) {
        this.cantidaPasajeros = cantidaPasajeros;
    }

    @Override
    public String toString() {
        return getPlaca() + " - " + getMarca() + " - " + getModelo();
    }
}