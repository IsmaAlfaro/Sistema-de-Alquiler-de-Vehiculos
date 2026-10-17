package cr.ac.una.est.sistemaalquilercarros.repositorio;

import cr.ac.una.est.sistemaalquilercarros.modelo.Cliente;
import java.util.List;

public interface ClienteRepositorio {

    void agregar(Cliente cliente);

    Cliente buscarPorCedula(String cedula);

    List<Cliente> obtenerTodos();

    void modificar(Cliente cliente);

    boolean eliminar(String cedula);

}