package cr.ac.una.est.sistemaalquilercarros.servicio;

import cr.ac.una.est.sistemaalquilercarros.modelo.Cliente;
import cr.ac.una.est.sistemaalquilercarros.modelo.Alquiler;
import cr.ac.una.est.sistemaalquilercarros.modelo.EstadoAlquiler;
import cr.ac.una.est.sistemaalquilercarros.persistencia.DatosSistema;
import cr.ac.una.est.sistemaalquilercarros.persistencia.PersistenciaDatos;
import cr.ac.una.est.sistemaalquilercarros.repositorio.Repositorio;

import java.util.List;

public class ClienteServicio {

    private final Repositorio<Cliente> repositorioClientes;
    private final DatosSistema datosSistema;
    private final PersistenciaDatos persistencia;

    public ClienteServicio(DatosSistema datosSistema,
                           PersistenciaDatos persistencia) {

        this.datosSistema = datosSistema;
        this.persistencia = persistencia;

        this.repositorioClientes = new Repositorio<>();

        this.repositorioClientes.reemplazarTodos(
                datosSistema.getClientes()
        );
    }

    private void guardarCambios() {

        datosSistema.setClientes(
                repositorioClientes.obtenerTodos()
        );

        persistencia.guardar(datosSistema);
    }

    public void registrarCliente(Cliente cliente) {

        if (cliente == null) {
            throw new IllegalArgumentException(
                    "El cliente no puede ser nulo."
            );
        }

        validarCliente(cliente);

        if (buscarPorCedula(cliente.getCedula()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un cliente con esa cédula."
            );
        }

        repositorioClientes.agregar(cliente);

        guardarCambios();
    }

    public Cliente buscarPorCedula(String cedula) {

        if (cedula == null || cedula.trim().isEmpty()) {
            return null;
        }

        for (Cliente cliente : repositorioClientes.obtenerTodos()) {

            if (cliente.getCedula().equalsIgnoreCase(cedula.trim())) {
                return cliente;
            }
        }

        return null;
    }

    public List<Cliente> obtenerTodos() {

        return repositorioClientes.obtenerTodos();
    }

    public void modificarCliente(String cedula,
                                 Cliente clienteModificado) {

        Cliente clienteActual = buscarPorCedula(cedula);

        if (clienteActual == null) {
            throw new IllegalArgumentException(
                    "El cliente no existe."
            );
        }

        if (clienteModificado == null) {
            throw new IllegalArgumentException(
                    "Los nuevos datos del cliente no son válidos."
            );
        }

        if (!clienteActual.getCedula()
                .equalsIgnoreCase(clienteModificado.getCedula())) {

            throw new IllegalArgumentException(
                    "La cédula del cliente no se puede modificar."
            );
        }

        validarCliente(clienteModificado);

        List<Cliente> clientes =
                repositorioClientes.obtenerTodos();

        int posicion =
                clientes.indexOf(clienteActual);

        clientes.set(
                posicion,
                clienteModificado
        );

        repositorioClientes.reemplazarTodos(clientes);

        guardarCambios();
    }

    public void eliminarCliente(String cedula) {

        Cliente cliente = buscarPorCedula(cedula);

        if (cliente == null) {
            throw new IllegalArgumentException(
                    "El cliente no existe."
            );
        }

        for (Alquiler alquiler : datosSistema.getAlquileres()) {

            if (alquiler.getEstado() == EstadoAlquiler.ACTIVO &&
                    alquiler.getCliente()
                            .getCedula()
                            .equalsIgnoreCase(cedula)) {

                throw new IllegalArgumentException(
                        "No se puede eliminar un cliente con un alquiler activo."
                );
            }
        }

        repositorioClientes.eliminar(cliente);

        guardarCambios();
    }

    public int cantidadClientes() {

        return repositorioClientes.cantidad();
    }

    private void validarCliente(Cliente cliente) {

        if (cliente.getCedula() == null ||
                cliente.getCedula().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "La cédula es obligatoria."
            );
        }

        if (cliente.getNombreCompleto() == null ||
                cliente.getNombreCompleto().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El nombre es obligatorio."
            );
        }

        if (cliente.getTelefono() == null ||
                cliente.getTelefono().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El teléfono es obligatorio."
            );
        }

        if (cliente.getCorreo() == null ||
                cliente.getCorreo().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El correo es obligatorio."
            );
        }
    }
}