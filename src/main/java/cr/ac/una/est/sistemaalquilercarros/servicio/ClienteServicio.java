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

    /*
     * Qué hace: Inicializa el servicio encargado de administrar los clientes, enlaza los datos generales y la persistencia, y carga en el repositorio los clientes previamente almacenados
     * Recibe: El objeto DatosSistema y el servicio encargado de guardar la información
     * Retorna: No retorna ningún valor
     */
    public ClienteServicio(DatosSistema datosSistema,
                           PersistenciaDatos persistencia) {

        this.datosSistema = datosSistema;
        this.persistencia = persistencia;

        this.repositorioClientes = new Repositorio<>();

        this.repositorioClientes.reemplazarTodos(
                datosSistema.getClientes()
        );
    }

    /*
     * Qué hace: Actualiza en DatosSistema la lista actual de clientes y posteriormente guarda toda la información utilizando el mecanismo de persistencia
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    private void guardarCambios() {

        datosSistema.setClientes(
                repositorioClientes.obtenerTodos()
        );

        persistencia.guardar(datosSistema);
    }

    /*
     * Qué hace: Registra un nuevo cliente después de validar que el objeto sea correcto, que sus datos cumplan con las reglas establecidas y que no exista otro cliente con la misma cédula
     * Recibe: El objeto Cliente que se desea registrar
     * Retorna: No retorna ningún valor
     */
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

    /*
     * Qué hace: Busca un cliente dentro del repositorio utilizando su número de cédula como identificador principal
     * Recibe: La cédula del cliente que se desea localizar
     * Retorna: El cliente encontrado o null si no existe ningún cliente con esa cédula
     */
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

    /*
     * Qué hace: Obtiene todos los clientes registrados actualmente dentro del repositorio del sistema
     * Recibe: No recibe parámetros
     * Retorna: La lista completa de clientes registrados
     */
    public List<Cliente> obtenerTodos() {

        return repositorioClientes.obtenerTodos();
    }

    /*
     * Qué hace: Modifica los datos de un cliente existente después de verificar que el cliente exista, que los nuevos datos sean válidos y que su cédula original no haya sido modificada
     * Recibe: La cédula del cliente que se desea modificar y el objeto Cliente que contiene los nuevos datos
     * Retorna: No retorna ningún valor
     */
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

    /*
     * Qué hace: Elimina un cliente del sistema después de verificar que exista y que no tenga ningún alquiler activo asociado que impida su eliminación
     * Recibe: La cédula del cliente que se desea eliminar
     * Retorna: No retorna ningún valor
     */
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

    /*
     * Qué hace: Obtiene la cantidad total de clientes que se encuentran registrados actualmente dentro del repositorio
     * Recibe: No recibe parámetros
     * Retorna: La cantidad total de clientes registrados
     */
    public int cantidadClientes() {

        return repositorioClientes.cantidad();
    }

    /*
     * Qué hace: Verifica que los datos principales de un cliente cumplan con las reglas establecidas, incluyendo cédula, nombre, teléfono y formato del correo electrónico, y genera una excepción si encuentra algún dato inválido
     * Recibe: El objeto Cliente cuyos datos se desean validar
     * Retorna: No retorna ningún valor
     */
    private void validarCliente(Cliente cliente) {

        if (cliente.getCedula() == null ||
                cliente.getCedula().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "La cédula es obligatoria."
            );
        }

        if (!cliente.getCedula().matches("\\d{9}")) {
            throw new IllegalArgumentException(
                    "La cédula debe contener exactamente 9 números."
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

        if (!cliente.getTelefono().matches("\\d{8}")) {
            throw new IllegalArgumentException(
                    "El teléfono debe contener exactamente 8 números."
            );
        }

        if (cliente.getCorreo() == null ||
                cliente.getCorreo().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El correo es obligatorio."
            );
        }

        if (!cliente.getCorreo().matches(
                "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{3,}$"
        )) {
            throw new IllegalArgumentException(
                    "El correo electrónico no tiene un formato válido."
            );
        }

    }
}