package cr.ac.una.est.sistemaalquilercarros.repositorio;

import cr.ac.una.est.sistemaalquilercarros.modelo.EstadoVehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.Vehiculo;

import java.util.List;

public interface VehiculoRepositorio {

    void agregar(Vehiculo vehiculo);

    Vehiculo buscarPorPlaca(String placa);

    List<Vehiculo> obtenerTodos();

    List<Vehiculo> obtenerDisponibles();

    void modificar(Vehiculo vehiculo);

    boolean eliminar(String placa);

    void cambiarEstado(String placa, EstadoVehiculo estado);

}