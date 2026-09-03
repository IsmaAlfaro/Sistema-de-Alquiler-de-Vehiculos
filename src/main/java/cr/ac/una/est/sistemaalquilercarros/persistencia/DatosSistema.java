package cr.ac.una.est.sistemaalquilercarros.persistencia;

import cr.ac.una.est.sistemaalquilercarros.modelo.Vehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.Cliente;
import cr.ac.una.est.sistemaalquilercarros.modelo.Alquiler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DatosSistema implements Serializable {

    private List <Vehiculo> vehiculos;
    private List <Cliente> clientes;
    private List <Alquiler> alquileres;

  public DatosSistema() {
      vehiculos = new ArrayList<>();
      clientes = new ArrayList<>();
      alquileres = new ArrayList<>();
  }

  public List <Vehiculo> getVehiculos() {
      return vehiculos;
  }

  public void setVehiculos(List <Vehiculo> vehiculos) {
      this.vehiculos = vehiculos;
  }


    public List <Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List <Cliente> clientes) {
        this.clientes = clientes;
    }

    public List <Alquiler> getAlquileres() {
        return alquileres;
    }

    public void setAlquiler(List <Alquiler> alquileres) {
        this.alquileres = alquileres;
    }







}
