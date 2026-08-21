package alquilercarros.modelo;

public abstract class Vehiculo {

    private String placa;
    private String marca;
    private String modelo;
    private int anio;
    private double tarifaDiaria;
    private EstadoVehiculo estado;

    public Vehiculo (String placa, String marca, String modelo, int anio, double tarifaDiaria) {

        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anio= anio;
        this.tarifaDiaria = tarifaDiaria;
        this.estado = EstadoVehiculo.DISPONIBLE;

    }

    public String getPlaca() {

        return placa;
    }

    public void setPlaca(String placa) {

        this.placa = placa;
    }

    public String getMarca() {

        return marca;
    }

    public void setMarca(String marca) {

        this.marca = marca;
    }

    public String getModelo() {

        return modelo;
    }

    public int getAnio(){

        return anio;
    }
    public void setAnio(int anio){

        this.anio=anio;
    }
    public void setModelo(String modelo) {

        this.modelo = modelo;
    }

    public double getTarifaDiaria() {

        return tarifaDiaria;
    }

    public void setTarifaDiaria(double tarifaDiaria) {

        this.tarifaDiaria = tarifaDiaria;
    }

    public EstadoVehiculo getEstado() {

        return estado;
    }

    public void setEstado(EstadoVehiculo estado) {

        this.estado = estado;
    }

    @Override

    public String toString() {
        return placa + " - " + marca + " - " + modelo;
    }

}
