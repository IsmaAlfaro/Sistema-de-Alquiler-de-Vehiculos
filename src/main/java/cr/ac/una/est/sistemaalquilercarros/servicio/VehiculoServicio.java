package cr.ac.una.est.sistemaalquilercarros.servicio;

import cr.ac.una.est.sistemaalquilercarros.modelo.EstadoVehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.TipoVehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.Vehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.VehiculoCarga;
import cr.ac.una.est.sistemaalquilercarros.modelo.VehiculoPasajeros;
import cr.ac.una.est.sistemaalquilercarros.repositorio.VehiculoRepositorio;

import java.time.Year;
import java.util.List;

public class VehiculoServicio {

    private final VehiculoRepositorio vehiculoRepositorio;

    /*
     * Qué hace: Inicializa el servicio encargado de administrar los vehículos utilizando el repositorio de vehículos
     * Recibe: El repositorio encargado de realizar las operaciones relacionadas con los vehículos
     * Retorna: No retorna ningún valor
     */
    public VehiculoServicio(VehiculoRepositorio vehiculoRepositorio) {
        this.vehiculoRepositorio = vehiculoRepositorio;
    }

    /*
     * Qué hace: Registra un nuevo vehículo después de comprobar que el objeto no sea nulo, validar todos sus datos y verificar que no exista otro vehículo con la misma placa
     * Recibe: El objeto Vehiculo que se desea registrar
     * Retorna: No retorna ningún valor
     */
    public void registrarVehiculo(Vehiculo vehiculo) {

        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo no puede ser nulo.");
        }

        validarVehiculo(vehiculo);

        if (buscarPorPlaca(vehiculo.getPlaca()) != null) {
            throw new IllegalArgumentException("Ya existe un vehículo con esa placa.");
        }

        vehiculoRepositorio.agregar(vehiculo);
    }

    /*
     * Qué hace: Busca dentro del repositorio un vehículo utilizando la placa como identificador principal
     * Recibe: La placa del vehículo que se desea localizar
     * Retorna: El vehículo encontrado o null si no existe ninguno con esa placa
     */
    public Vehiculo buscarPorPlaca(String placa) {

        if (placa == null || placa.trim().isEmpty()) {
            return null;
        }

        return vehiculoRepositorio.buscarPorPlaca(
                placa.trim()
        );
    }

    /*
     * Qué hace: Obtiene todos los vehículos registrados actualmente dentro del repositorio del sistema
     * Recibe: No recibe parámetros
     * Retorna: La lista completa de vehículos registrados
     */
    public List<Vehiculo> obtenerTodos() {
        return vehiculoRepositorio.obtenerTodos();
    }

    /*
     * Qué hace: Obtiene todos los vehículos registrados que actualmente se encuentran disponibles para realizar un alquiler
     * Recibe: No recibe parámetros
     * Retorna: Una lista con todos los vehículos que se encuentran disponibles para alquiler
     */
    public List<Vehiculo> obtenerDisponibles() {
        return vehiculoRepositorio.obtenerDisponibles();
    }

    /*
     * Qué hace: Modifica la información de un vehículo existente después de verificar que exista, que no se encuentre alquilado, que los nuevos datos sean válidos y que la placa original no haya sido cambiada
     * Recibe: La placa del vehículo que se desea modificar y el objeto Vehiculo que contiene los nuevos datos
     * Retorna: No retorna ningún valor
     */
    public void modificarVehiculo(String placa, Vehiculo vehiculoModificado) {

        Vehiculo vehiculoActual = buscarPorPlaca(placa);

        if (vehiculoActual == null) {
            throw new IllegalArgumentException("El vehículo no existe.");
        }

        if (vehiculoActual.getEstado() == EstadoVehiculo.ALQUILADO) {
            throw new IllegalArgumentException("No se puede modificar un vehículo alquilado.");
        }

        if (vehiculoModificado == null) {
            throw new IllegalArgumentException("Los nuevos datos del vehículo no son válidos.");
        }

        if (!vehiculoActual.getPlaca().equalsIgnoreCase(vehiculoModificado.getPlaca())) {
            throw new IllegalArgumentException("La placa del vehículo no se puede modificar.");
        }

        validarVehiculo(vehiculoModificado);

        vehiculoModificado.setEstado(vehiculoActual.getEstado());

        vehiculoRepositorio.modificar(vehiculoModificado);

    }

    /*
     * Qué hace: Elimina un vehículo del sistema después de comprobar que exista y que actualmente no se encuentre alquilado
     * Recibe: La placa del vehículo que se desea eliminar
     * Retorna: No retorna ningún valor
     */
    public void eliminarVehiculo(String placa) {

        Vehiculo vehiculo = buscarPorPlaca(placa);

        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo no existe.");
        }

        if (vehiculo.getEstado() == EstadoVehiculo.ALQUILADO) {
            throw new IllegalArgumentException("No se puede eliminar un vehículo alquilado.");
        }

        vehiculoRepositorio.eliminar(placa);
    }

    /*
     * Qué hace: Cambia el estado actual de un vehículo después de comprobar que el vehículo exista dentro del sistema
     * Recibe: La placa del vehículo cuyo estado se desea cambiar y el nuevo estado que se desea asignar
     * Retorna: No retorna ningún valor
     */
    public void cambiarEstado(String placa, EstadoVehiculo estado) {

        Vehiculo vehiculo = buscarPorPlaca(placa);

        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo no existe.");
        }

        vehiculoRepositorio.cambiarEstado(placa, estado);
    }

    /*
     * Qué hace: Obtiene la cantidad total de vehículos registrados actualmente dentro del repositorio
     * Recibe: No recibe parámetros
     * Retorna: La cantidad total de vehículos registrados
     */
    public int cantidadVehiculos() {
        return vehiculoRepositorio.obtenerTodos().size();
    }

    /*
     * Qué hace: Calcula cuántos vehículos registrados se encuentran actualmente disponibles para realizar un nuevo alquiler
     * Recibe: No recibe parámetros
     * Retorna: La cantidad de vehículos disponibles
     */
    public int cantidadDisponibles() {
        return vehiculoRepositorio.obtenerDisponibles().size();
    }

    /*
     * Qué hace: Calcula cuántos vehículos se encuentran actualmente alquilados restando los vehículos disponibles de la cantidad total registrada
     * Recibe: No recibe parámetros
     * Retorna: La cantidad de vehículos que se encuentran alquilados
     */
    public int cantidadAlquilados() {
        return cantidadVehiculos() - cantidadDisponibles();
    }

    /*
     * Qué hace: Verifica que la información general y específica de un vehículo cumpla con las reglas del sistema, incluyendo placa, marca, modelo, año, tarifa, cantidad de pasajeros, capacidad de carga y tipo de tracción según corresponda
     * Recibe: El objeto Vehiculo cuyos datos se desean validar antes de registrarlo o modificarlo
     * Retorna: No retorna ningún valor
     */
    private void validarVehiculo(Vehiculo vehiculo) {

        if (vehiculo.getPlaca() == null || vehiculo.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("La placa es obligatoria.");
        }

        if (vehiculo.getMarca() == null || vehiculo.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("La marca es obligatoria.");
        }

        if (vehiculo.getModelo() == null || vehiculo.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo es obligatorio.");
        }

        int anioMaximo = Year.now().getValue() + 1;

        if (vehiculo.getAnio() < 1900 || vehiculo.getAnio() > anioMaximo) {
            throw new IllegalArgumentException("El año del vehículo no es válido.");
        }

        if (vehiculo.getTarifaDiaria() <= 0) {
            throw new IllegalArgumentException("La tarifa diaria debe ser mayor que cero.");
        }

        if (vehiculo instanceof VehiculoPasajeros pasajeros) {

            TipoVehiculo tipo = pasajeros.getTipoVehiculo();

            if (tipo == null) {
                throw new IllegalArgumentException("Debe seleccionar un tipo de vehículo.");
            }

            if (tipo.isRequierePasajeros() && (pasajeros.getCantidadPasajeros() <= 0 || pasajeros.getCantidadPasajeros() > 20)) {
                throw new IllegalArgumentException("La cantidad de pasajeros debe estar entre 1 y 20.");
            }

            if (tipo.isRequiereTraccion() && (pasajeros.getTipoTraccion() == null || pasajeros.getTipoTraccion().trim().isEmpty())) {
                throw new IllegalArgumentException("El tipo de tracción es obligatorio.");
            }
        }

        if (vehiculo instanceof VehiculoCarga carga) {

            TipoVehiculo tipo = carga.getTipoVehiculo();

            if (tipo == null) {
                throw new IllegalArgumentException("Debe seleccionar un tipo de vehículo.");
            }

            if (tipo.isRequiereCapacidadCarga() && carga.getCapacidadCarga() <= 0) {
                throw new IllegalArgumentException("La capacidad de carga debe ser mayor que cero.");
            }

            if (tipo.isRequiereTraccion() && (carga.getTipoTraccion() == null || carga.getTipoTraccion().trim().isEmpty())) {
                throw new IllegalArgumentException("El tipo de tracción es obligatorio.");
            }
        }
    }
}