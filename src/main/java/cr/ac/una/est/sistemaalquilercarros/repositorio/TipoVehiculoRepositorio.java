package cr.ac.una.est.sistemaalquilercarros.repositorio;

import cr.ac.una.est.sistemaalquilercarros.modelo.TipoVehiculo;
import java.util.List;

public interface TipoVehiculoRepositorio {

    void agregar(TipoVehiculo tipo);

    TipoVehiculo buscarPorNombre(String nombre);

    List<TipoVehiculo> obtenerTodos();

    void modificar(TipoVehiculo tipo);

    boolean eliminar(String nombre);
}