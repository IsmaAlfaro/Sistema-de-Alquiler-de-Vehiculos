package alquilercarros.modelo;

public class Carro extends Vehiculo {
    private final int numeroPuertas;

    public Carro(String placa, String marca, String modelo, int anio,
                 double tarifaDiaria, int numeroPuertas) {
        super(placa, marca, modelo, anio, tarifaDiaria);
        if (numeroPuertas <= 0) {
            throw new IllegalArgumentException("El numero de puertas debe ser mayor que cero.");
        }
        this.numeroPuertas = numeroPuertas;
    }

    public int getNumeroPuertas() {
        return numeroPuertas;
    }

    @Override
    public String toString() {
        return getPlaca() + " - " + getMarca() + " " + getModelo()
                + " (" + getAnio() + ") | " + numeroPuertas + " puertas | " + getEstado();
    }
}
