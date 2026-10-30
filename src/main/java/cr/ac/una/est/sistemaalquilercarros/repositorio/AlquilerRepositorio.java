package cr.ac.una.est.sistemaalquilercarros.repositorio;

import cr.ac.una.est.sistemaalquilercarros.modelo.Alquiler;
import java.util.List;

public interface AlquilerRepositorio {

    Alquiler agregar(Alquiler alquiler);

    Alquiler buscarPorNumero(int numeroAlquiler);

    List<Alquiler> obtenerTodos();

    List<Alquiler> obtenerActivos();

    void finalizar(Alquiler alquiler);

    boolean eliminar(int numeroAlquiler);

}
