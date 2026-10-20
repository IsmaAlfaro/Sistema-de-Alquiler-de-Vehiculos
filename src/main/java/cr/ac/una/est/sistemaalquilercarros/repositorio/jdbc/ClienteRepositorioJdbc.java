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

    public ClienteRepositorioJdbc(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

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

    @Override
    public boolean eliminar(String cedula) {
        String sql = "DELETE FROM cliente WHERE cedula = ?";
        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo eliminar el cliente.", e);
        }
    }
}