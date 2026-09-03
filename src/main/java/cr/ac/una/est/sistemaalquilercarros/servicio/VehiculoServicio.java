package cr.ac.una.est.sistemaalquilercarros.servicio;

import cr.ac.una.est.sistemaalquilercarros.modelo.EstadoVehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.Vehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.Sedan;
import cr.ac.una.est.sistemaalquilercarros.modelo.SUV;
import cr.ac.una.est.sistemaalquilercarros.modelo.Pickup;
import cr.ac.una.est.sistemaalquilercarros.persistencia.DatosSistema;
import cr.ac.una.est.sistemaalquilercarros.persistencia.PersistenciaDatos;
import cr.ac.una.est.sistemaalquilercarros.repositorio.Repositorio;

import java.util.ArrayList;
import java.util.List;
import java.time.Year;

public class VehiculoServicio {

    private final Repositorio<Vehiculo> repositorioVehiculos;
    private final DatosSistema datosSistema;
    private final PersistenciaDatos persistencia;

    public VehiculoServicio(DatosSistema datosSistema, PersistenciaDatos persistencia) {
        this.datosSistema = datosSistema;
        this.persistencia = persistencia;
        this.repositorioVehiculos = new Repositorio<>();
        this.repositorioVehiculos.remplazarTodos(datosSistema.getVehiculos());
    }

    private void guardarCambios() {

        datosSistema.setVehiculos(repositorioVehiculos.obetenerTodos());

        persistencia.guardar(datosSistema);

    }

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

    public Vehiculo buscarPorPlaca(String placa) {

        if (placa == null || placa.trim().isEmpty()) {
            return null;
        }

        for (Vehiculo vehiculo : repositorioVehiculos.obetenerTodos()) {

            if (vehiculo.getPlaca().equalsIgnoreCase(placa.trim())) {

                return vehiculo;
            }
        }

        return null;
    }

    public List<Vehiculo> obtenerTodos() {
        return repositorioVehiculos.obetenerTodos();
    }

    public List<Vehiculo> obtenerDisponibles() {

        List<Vehiculo> disponibles = new ArrayList<>();

        for (Vehiculo vehiculo : repositorioVehiculos.obetenerTodos()) {

            if (vehiculo.getEstado() == EstadoVehiculo.DISPONIBLE) {
                disponibles.add(vehiculo);
            }
        }

        return disponibles;
    }

    public void modificarVehiculo(String placa, Vehiculo vehiculoModificado) {

        Vehiculo vehiculoActual = buscarPorPlaca(placa);

        if (vehiculoActual == null) {
            throw new IllegalArgumentException("El vehículo no existe");
        }

        if (vehiculoActual.getEstado() == EstadoVehiculo.ALQUILADO) {
            throw new IllegalArgumentException("No se puede modificar un vehículo alquilado");
        }

        if (vehiculoModificado == null) {
            throw new IllegalArgumentException("Los nuevos datos del vehículo no son válidos");
        }

        if (!vehiculoActual.getPlaca().equalsIgnoreCase(vehiculoModificado.getPlaca())) {
            throw new IllegalArgumentException("La placa del vehículo no se puede modificar");
        }

        validarVehiculo(vehiculoModificado);

        List<Vehiculo> vehiculos = repositorioVehiculos.obetenerTodos();

        int posicion = vehiculos.indexOf(vehiculoActual);

        vehiculoModificado.setEstado(vehiculoActual.getEstado());

        vehiculos.set(posicion, vehiculoModificado);

        repositorioVehiculos.remplazarTodos(vehiculos);

        guardarCambios();
    }

    public void eliminarVehiculo(String placa) {

        Vehiculo vehiculo = buscarPorPlaca(placa);

        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo no existe");
        }

        if (vehiculo.getEstado() == EstadoVehiculo.ALQUILADO) {

            throw new IllegalArgumentException("No se puede eliminar un vehículo alquilado");
        }

        repositorioVehiculos.eliminar(vehiculo);

        guardarCambios();
    }

    public int cantidadVehiculos() {
        return repositorioVehiculos.cantidad();
    }

    public int cantidadDisponibles() {
        return obtenerDisponibles().size();
    }

    public int cantidadAlquilados() {
        return cantidadVehiculos() - cantidadDisponibles();
    }

    private void validarVehiculo(Vehiculo vehiculo) {

        if (vehiculo.getPlaca() == null || vehiculo.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("La placa es obligatoria");
        }

        if (vehiculo.getMarca() == null || vehiculo.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("La marca es obligatoria");
        }

        if (vehiculo.getModelo() == null || vehiculo.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo es obligatorio");
        }

        int anioMaximo = Year.now().getValue() + 1;

        if (vehiculo.getAnio() < 1900 || vehiculo.getAnio() > anioMaximo) {
            throw new IllegalArgumentException("El año del vehículo no es válido");
        }

        if (vehiculo.getTarifaDiaria() <= 0) {
            throw new IllegalArgumentException("La tarifa diaria debe ser mayor que cero");
        }

        if (vehiculo instanceof Sedan sedan) {
            if (sedan.getCantidaPasajeros() <= 0) {
                throw new IllegalArgumentException("La cantidad de pasajeros debe ser mayor que cero");
            }
        }

        if (vehiculo instanceof SUV suv) {
            if (suv.getCantidadPasajeros() <= 0) {
                throw new IllegalArgumentException("La cantidad de pasajeros debe ser mayor que cero");
            }

            if (suv.getTipoTraccion() == null || suv.getTipoTraccion().trim().isEmpty()) {
                throw new IllegalArgumentException("El tipo de tracción es obligatorio");
            }
        }

        if (vehiculo instanceof Pickup pickup) {
            if (pickup.getCapacidadCarga() <= 0) {
                throw new IllegalArgumentException("La capacidad de carga debe ser mayor que cero");
            }

            if (pickup.getTipoTraccion() == null || pickup.getTipoTraccion().trim().isEmpty()) {
                throw new IllegalArgumentException("El tipo de tracción es obligatorio");
            }
        }
    }
}