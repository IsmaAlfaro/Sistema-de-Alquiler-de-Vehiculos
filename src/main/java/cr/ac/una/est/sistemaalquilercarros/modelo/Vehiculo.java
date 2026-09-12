package cr.ac.una.est.sistemaalquilercarros.modelo;

import java.io.Serializable;

public abstract class Vehiculo implements Serializable {

    private String placa;
    private String marca;
    private String modelo;
    private int anio;
    private double tarifaDiaria;
    private EstadoVehiculo estado;
    private TipoVehiculo tipoVehiculo;

    public Vehiculo(String placa, String marca, String modelo, int anio, double tarifaDiaria, TipoVehiculo tipoVehiculo) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.tarifaDiaria = tarifaDiaria;
        this.tipoVehiculo = tipoVehiculo;
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

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
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

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }
}
