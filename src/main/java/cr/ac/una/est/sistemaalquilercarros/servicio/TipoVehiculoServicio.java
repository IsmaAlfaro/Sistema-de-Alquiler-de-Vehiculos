package cr.ac.una.est.sistemaalquilercarros.servicio;

import cr.ac.una.est.sistemaalquilercarros.modelo.CategoriaVehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.TipoVehiculo;
import cr.ac.una.est.sistemaalquilercarros.persistencia.DatosSistema;
import cr.ac.una.est.sistemaalquilercarros.persistencia.PersistenciaDatos;
import cr.ac.una.est.sistemaalquilercarros.repositorio.Repositorio;

import java.util.List;

public class TipoVehiculoServicio {

    private final Repositorio<TipoVehiculo> repositorioTipos;
    private final DatosSistema datosSistema;
    private final PersistenciaDatos persistencia;

    public TipoVehiculoServicio(DatosSistema datosSistema, PersistenciaDatos persistencia) {

        this.datosSistema = datosSistema;
        this.persistencia = persistencia;

        repositorioTipos = new Repositorio<>();

        if (datosSistema.getTiposVehiculo() != null) {
            repositorioTipos.reemplazarTodos(
                    datosSistema.getTiposVehiculo()
            );
        }

        if (repositorioTipos.estaVacio()) {
            crearTiposIniciales();
        }
    }

    public void registrarTipo(TipoVehiculo tipo) {

        validarTipo(tipo);

        if (buscarPorNombre(tipo.getNombre()) != null) {
            throw new IllegalArgumentException("Ya existe un tipo de vehículo con ese nombre");
        }

        repositorioTipos.agregar(tipo);

        guardarCambios();
    }

    public TipoVehiculo buscarPorNombre(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }

        for (TipoVehiculo tipo : repositorioTipos.obtenerTodos()) {

            if (tipo.getNombre().equalsIgnoreCase(nombre.trim())) {
                return tipo;
            }
        }

        return null;
    }

    public List<TipoVehiculo> obtenerTodos() {
        return repositorioTipos.obtenerTodos();
    }

    public void eliminarTipo(String nombre) {

        TipoVehiculo tipo = buscarPorNombre(nombre);

        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de vehículo no existe");
        }

        repositorioTipos.eliminar(tipo);

        guardarCambios();
    }

    private void validarTipo(TipoVehiculo tipo) {

        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de vehículo no puede ser nulo");
        }

        if (tipo.getNombre() == null || tipo.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del tipo es obligatorio");
        }

        if (tipo.getCategoria() == null) {
            throw new IllegalArgumentException("La categoría del vehículo es obligatoria");}}

    private void crearTiposIniciales() {

        repositorioTipos.agregar(new TipoVehiculo("Sedán", CategoriaVehiculo.PASAJEROS, true, false, false));

        repositorioTipos.agregar(new TipoVehiculo("SUV", CategoriaVehiculo.PASAJEROS, true, true, false));

        repositorioTipos.agregar(new TipoVehiculo("Minivan", CategoriaVehiculo.PASAJEROS, true, true, false));

        repositorioTipos.agregar(new TipoVehiculo("CUV", CategoriaVehiculo.PASAJEROS, true, true, false));

        repositorioTipos.agregar(new TipoVehiculo("Deportivo", CategoriaVehiculo.PASAJEROS, true, true, false));

        repositorioTipos.agregar(new TipoVehiculo("Pickup", CategoriaVehiculo.CARGA, false, true, true));

        guardarCambios();
    }

    private void guardarCambios() {

        datosSistema.setTiposVehiculo(repositorioTipos.obtenerTodos());

        persistencia.guardar(datosSistema);
    }
}