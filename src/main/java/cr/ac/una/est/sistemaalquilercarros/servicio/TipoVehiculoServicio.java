package cr.ac.una.est.sistemaalquilercarros.servicio;

import cr.ac.una.est.sistemaalquilercarros.modelo.TipoVehiculo;
import cr.ac.una.est.sistemaalquilercarros.repositorio.TipoVehiculoRepositorio;

import java.util.List;

public class TipoVehiculoServicio {

    private final TipoVehiculoRepositorio tipoVehiculoRepositorio;

    public TipoVehiculoServicio(
            TipoVehiculoRepositorio tipoVehiculoRepositorio) {
        this.tipoVehiculoRepositorio = tipoVehiculoRepositorio;
    }

    public void registrarTipo(TipoVehiculo tipo) {

        validarTipo(tipo);

        if (buscarPorNombre(tipo.getNombre()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un tipo de vehículo con ese nombre."
            );
        }

        tipoVehiculoRepositorio.agregar(tipo);
    }

    public TipoVehiculo buscarPorNombre(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }

        return tipoVehiculoRepositorio.buscarPorNombre(
                nombre.trim()
        );
    }

    public List<TipoVehiculo> obtenerTodos() {
        return tipoVehiculoRepositorio.obtenerTodos();
    }

    public void modificarTipo(TipoVehiculo tipo) {

        validarTipo(tipo);

        if (buscarPorNombre(tipo.getNombre()) == null) {
            throw new IllegalArgumentException(
                    "El tipo de vehículo no existe."
            );
        }

        tipoVehiculoRepositorio.modificar(tipo);
    }

    public void eliminarTipo(String nombre) {

        TipoVehiculo tipo = buscarPorNombre(nombre);

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "El tipo de vehículo no existe."
            );
        }

        tipoVehiculoRepositorio.eliminar(nombre);
    }

    private void validarTipo(TipoVehiculo tipo) {

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "El tipo de vehículo no puede ser nulo."
            );
        }

        if (tipo.getNombre() == null
                || tipo.getNombre().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El nombre del tipo es obligatorio."
            );
        }

        if (tipo.getCategoria() == null) {
            throw new IllegalArgumentException(
                    "La categoría del vehículo es obligatoria."
            );
        }
    }
}
