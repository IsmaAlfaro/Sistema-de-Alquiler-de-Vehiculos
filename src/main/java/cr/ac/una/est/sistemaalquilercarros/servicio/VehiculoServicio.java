package cr.ac.una.est.sistemaalquilercarros.servicio;

import cr.ac.una.est.sistemaalquilercarros.modelo.EstadoVehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.Vehiculo;
import cr.ac.una.est.sistemaalquilercarros.persistencia.DatosSistema;
import cr.ac.una.est.sistemaalquilercarros.persistencia.PersistenciaDatos;
import cr.ac.una.est.sistemaalquilercarros.repositorio.Repositorio;
import cr.ac.una.est.sistemaalquilercarros.modelo.VehiculoPasajeros;
import cr.ac.una.est.sistemaalquilercarros.modelo.VehiculoCarga;
import cr.ac.una.est.sistemaalquilercarros.modelo.TipoVehiculo;

import java.util.ArrayList;
import java.util.List;
import java.time.Year;

public class VehiculoServicio {

    private final Repositorio<Vehiculo> repositorioVehiculos;
    private final DatosSistema datosSistema;
    private final PersistenciaDatos persistencia;

    /*
     * Qué hace: Inicializa el servicio encargado de administrar los vehículos, enlaza los datos generales y la persistencia, y carga en el repositorio los vehículos previamente registrados
     * Recibe: El objeto DatosSistema y el servicio encargado de guardar la información
     * Retorna: No retorna ningún valor
     */
    public VehiculoServicio(DatosSistema datosSistema, PersistenciaDatos persistencia) {
        this.datosSistema = datosSistema;
        this.persistencia = persistencia;

        this.repositorioVehiculos = new Repositorio<>();
        this.repositorioVehiculos.reemplazarTodos(datosSistema.getVehiculos());
    }

    /*
     * Qué hace: Actualiza en DatosSistema la lista actual de vehículos almacenados en el repositorio y posteriormente guarda toda la información mediante el mecanismo de persistencia
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    private void guardarCambios() {

        datosSistema.setVehiculos(repositorioVehiculos.obtenerTodos());

        persistencia.guardar(datosSistema);
    }

    /*
     * Qué hace: Registra un nuevo vehículo después de comprobar que el objeto no sea nulo, validar todos sus datos y verificar que no exista otro vehículo con la misma placa
     * Recibe: El objeto Vehiculo que se desea registrar
     * Retorna: No retorna ningún valor
     */
    public void registrarVehiculo(Vehiculo vehiculo) {

        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo no puede ser nulo");
        }

        validarVehiculo(vehiculo);

        if (buscarPorPlaca(vehiculo.getPlaca()) != null) {
            throw new IllegalArgumentException("Ya existe un vehículo con esa placa");
        }

        repositorioVehiculos.agregar(vehiculo);

        guardarCambios();
    }

    /*
     * Qué hace: Busca dentro del repositorio un vehículo utilizando la placa como identificador principal y comparando el valor sin distinguir entre mayúsculas y minúsculas
     * Recibe: La placa del vehículo que se desea localizar
     * Retorna: El vehículo encontrado o null si no existe ninguno con esa placa
     */
    public Vehiculo buscarPorPlaca(String placa) {

        if (placa == null || placa.trim().isEmpty()) {
            return null;
        }

        for (Vehiculo vehiculo : repositorioVehiculos.obtenerTodos()) {

            if (vehiculo.getPlaca().equalsIgnoreCase(placa.trim())) {

                return vehiculo;
            }
        }

        return null;
    }

    /*
     * Qué hace: Obtiene todos los vehículos registrados actualmente dentro del repositorio del sistema
     * Recibe: No recibe parámetros
     * Retorna: La lista completa de vehículos registrados
     */
    public List<Vehiculo> obtenerTodos() {
        return repositorioVehiculos.obtenerTodos();
    }

    /*
     * Qué hace: Recorre todos los vehículos registrados y construye una lista que contiene únicamente aquellos cuyo estado actual es DISPONIBLE
     * Recibe: No recibe parámetros
     * Retorna: Una lista con todos los vehículos que se encuentran disponibles para alquiler
     */
    public List<Vehiculo> obtenerDisponibles() {

        List<Vehiculo> disponibles = new ArrayList<>();

        for (Vehiculo vehiculo : repositorioVehiculos.obtenerTodos()) {

            if (vehiculo.getEstado() == EstadoVehiculo.DISPONIBLE) {
                disponibles.add(vehiculo);
            }
        }

        return disponibles;
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

        List<Vehiculo> vehiculos = repositorioVehiculos.obtenerTodos();

        int posicion = vehiculos.indexOf(vehiculoActual);

        vehiculoModificado.setEstado(vehiculoActual.getEstado());

        vehiculos.set(posicion, vehiculoModificado);

        repositorioVehiculos.reemplazarTodos(vehiculos);

        guardarCambios();
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

        repositorioVehiculos.eliminar(vehiculo);

        guardarCambios();
    }

    /*
     * Qué hace: Obtiene la cantidad total de vehículos registrados actualmente dentro del repositorio
     * Recibe: No recibe parámetros
     * Retorna: La cantidad total de vehículos registrados
     */
    public int cantidadVehiculos() {
        return repositorioVehiculos.cantidad();
    }

    /*
     * Qué hace: Calcula cuántos vehículos registrados se encuentran actualmente disponibles para realizar un nuevo alquiler
     * Recibe: No recibe parámetros
     * Retorna: La cantidad de vehículos disponibles
     */
    public int cantidadDisponibles() {
        return obtenerDisponibles().size();
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
                throw new IllegalArgumentException(
                        "La cantidad de pasajeros debe estar entre 1 y 20."
                );
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
