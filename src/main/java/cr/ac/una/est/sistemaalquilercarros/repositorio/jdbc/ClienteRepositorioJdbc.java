package cr.ac.una.est.sistemaalquilercarros.repositorio.jdbc;

import cr.ac.una.est.sistemaalquilercarros.modelo.Cliente;
import cr.ac.una.est.sistemaalquilercarros.persistencia.ConexionBD;
import cr.ac.una.est.sistemaalquilercarros.repositorio.ClienteRepositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepositorioJdbc implements ClienteRepositorio {

    private final ConexionBD conexionBD;

    /*
     * Qué hace: Inicializa el repositorio de clientes utilizando el objeto encargado de proporcionar las conexiones con la base de datos
     * Recibe: El objeto ConexionBD que permite obtener conexiones con MySQL
     * Retorna: No retorna ningún valor
     */
    public ClienteRepositorioJdbc(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /*
     * Qué hace: Registra un nuevo cliente en la base de datos utilizando sus datos personales
     * Recibe: El objeto Cliente que se desea almacenar
     * Retorna: No retorna ningún valor
     */
    @Override
    public void agregar(Cliente cliente) {

        String sql = "INSERT INTO cliente " + "(cedula, nombre_completo, telefono, correo) " + "VALUES (?, ?, ?, ?)";

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, cliente.getCedula());
            ps.setString(2, cliente.getNombreCompleto());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getCorreo());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo registrar el cliente.", e);
        }
    }

    /*
     * Qué hace: Busca en la base de datos un cliente utilizando su número de cédula
     * Recibe: La cédula del cliente que se desea buscar
     * Retorna: El cliente encontrado o null si no existe ningún cliente con esa cédula
     */
    @Override
    public Cliente buscarPorCedula(String cedula) {

        String sql = "SELECT cedula, nombre_completo, telefono, correo " + "FROM cliente WHERE cedula = ?";

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, cedula);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(rs.getString("cedula"), rs.getString("nombre_completo"), rs.getString("telefono"), rs.getString("correo"));
                }
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo buscar el cliente.", e);
        }
    }

    /*
     * Qué hace: Obtiene todos los clientes almacenados actualmente en la base de datos
     * Recibe: No recibe parámetros
     * Retorna: Una lista que contiene todos los clientes registrados
     */
    @Override
    public List<Cliente> obtenerTodos() {
        String sql = "SELECT cedula, nombre_completo, telefono, correo " + "FROM cliente";

        List<Cliente> clientes = new ArrayList<>();

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Cliente cliente = new Cliente(rs.getString("cedula"), rs.getString("nombre_completo"), rs.getString("telefono"), rs.getString("correo"));
                clientes.add(cliente);

            }

            return clientes;

        } catch (SQLException e) {
            throw new RuntimeException("No se pudieron cargar los clientes.", e);
        }
    }

    /*
     * Qué hace: Actualiza en la base de datos el nombre, teléfono y correo de un cliente utilizando su cédula como identificador
     * Recibe: El objeto Cliente que contiene los datos actualizados
     * Retorna: No retorna ningún valor
     */
    @Override
    public void modificar(Cliente cliente) {
        String sql = "UPDATE cliente " + "SET nombre_completo = ?, telefono = ?, correo = ? " + "WHERE cedula = ?";

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombreCompleto());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getCedula());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo modificar el cliente.", e);
        }
    }

    /*
     * Qué hace: Elimina de la base de datos el cliente identificado por la cédula indicada
     * Recibe: La cédula del cliente que se desea eliminar
     * Retorna: true si se eliminó un cliente y false si no se encontró ningún registro para eliminar
     */
    @Override
    public boolean eliminar(String cedula) {

        String sql = "DELETE FROM cliente WHERE cedula = ?";

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {ps.setString(1, cedula);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo eliminar el cliente.", e);
        }
    }
}