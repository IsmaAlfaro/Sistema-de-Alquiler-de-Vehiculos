package cr.ac.una.est.sistemaalquilercarros.persistencia;

import cr.ac.una.est.sistemaalquilercarros.modelo.TipoVehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.Vehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.Cliente;
import cr.ac.una.est.sistemaalquilercarros.modelo.Alquiler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DatosSistema implements Serializable {

    private List<Vehiculo> vehiculos;
    private List<TipoVehiculo> tiposVehiculo;
    private List<Cliente> clientes;
    private List<Alquiler> alquileres;

    /*
     * Qué hace: Crea la estructura principal de datos del sistema e inicializa las listas necesarias para almacenar vehículos, tipos de vehículo, clientes y alquileres, evitando que estas colecciones comiencen con valores nulos
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    public DatosSistema() {
        vehiculos = new ArrayList<>();
        tiposVehiculo = new ArrayList<>();
        clientes = new ArrayList<>();
        alquileres = new ArrayList<>();
    }

    /*
     * Qué hace: Obtiene la lista completa de vehículos almacenados actualmente dentro de los datos generales del sistema
     * Recibe: No recibe parámetros
     * Retorna: La lista de vehículos registrados
     */
    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    /*
     * Qué hace: Reemplaza la lista actual de vehículos almacenada en el sistema por una nueva lista proporcionada
     * Recibe: La nueva lista de vehículos que se desea almacenar
     * Retorna: No retorna ningún valor
     */
    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    /*
     * Qué hace: Obtiene la lista completa de tipos de vehículo registrados y disponibles dentro del sistema
     * Recibe: No recibe parámetros
     * Retorna: La lista de tipos de vehículo almacenados
     */
    public List<TipoVehiculo> getTiposVehiculo() {
        return tiposVehiculo;
    }

    /*
     * Qué hace: Reemplaza la lista actual de tipos de vehículo por una nueva lista proporcionada al sistema
     * Recibe: La nueva lista de tipos de vehículo que se desea almacenar
     * Retorna: No retorna ningún valor
     */
    public void setTiposVehiculo(List<TipoVehiculo> tiposVehiculo) {
        this.tiposVehiculo = tiposVehiculo;
    }

    /*
     * Qué hace: Obtiene la lista completa de clientes que se encuentran almacenados actualmente dentro del sistema
     * Recibe: No recibe parámetros
     * Retorna: La lista de clientes registrados
     */
    public List<Cliente> getClientes() {
        return clientes;
    }

    /*
     * Qué hace: Reemplaza la lista actual de clientes almacenada en el sistema por una nueva lista proporcionada
     * Recibe: La nueva lista de clientes que se desea almacenar
     * Retorna: No retorna ningún valor
     */
    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    /*
     * Qué hace: Obtiene la lista completa de alquileres que se encuentran registrados actualmente dentro del sistema
     * Recibe: No recibe parámetros
     * Retorna: La lista de alquileres registrados
     */
    public List<Alquiler> getAlquileres() {
        return alquileres;
    }

    /*
     * Qué hace: Reemplaza la lista actual de alquileres almacenada en el sistema por una nueva lista proporcionada
     * Recibe: La nueva lista de alquileres que se desea almacenar
     * Retorna: No retorna ningún valor
     */
    public void setAlquileres(List<Alquiler> alquileres) {
        this.alquileres = alquileres;
    }
}
