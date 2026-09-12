package cr.ac.una.est.sistemaalquilercarros.modelo;

import java.io.Serializable;

public class TipoVehiculo implements Serializable {

    private String nombre;
    private CategoriaVehiculo categoria;
    private boolean requierePasajeros;
    private boolean requiereTraccion;
    private boolean requiereCapacidadCarga;

    public TipoVehiculo(String nombre, CategoriaVehiculo categoria, boolean requierePasajeros, boolean requiereTraccion, boolean requiereCapacidadCarga) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.requierePasajeros = requierePasajeros;
        this.requiereTraccion = requiereTraccion;
        this.requiereCapacidadCarga = requiereCapacidadCarga;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public CategoriaVehiculo getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaVehiculo categoria) {
        this.categoria = categoria;
    }

    public boolean isRequierePasajeros() {
        return requierePasajeros;
    }

    public void setRequierePasajeros(boolean requierePasajeros) {
        this.requierePasajeros = requierePasajeros;
    }

    public boolean isRequiereTraccion() {
        return requiereTraccion;
    }

    public void setRequiereTraccion(boolean requiereTraccion) {
        this.requiereTraccion = requiereTraccion;
    }

    public boolean isRequiereCapacidadCarga() {
        return requiereCapacidadCarga;
    }

    public void setRequiereCapacidadCarga(boolean requiereCapacidadCarga) {
        this.requiereCapacidadCarga = requiereCapacidadCarga;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
