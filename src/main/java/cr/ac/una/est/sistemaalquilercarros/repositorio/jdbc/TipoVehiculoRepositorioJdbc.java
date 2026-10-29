package cr.ac.una.est.sistemaalquilercarros.repositorio.jdbc;

import cr.ac.una.est.sistemaalquilercarros.modelo.CategoriaVehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.TipoVehiculo;
import cr.ac.una.est.sistemaalquilercarros.persistencia.ConexionBD;
import cr.ac.una.est.sistemaalquilercarros.repositorio.TipoVehiculoRepositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoVehiculoRepositorioJdbc implements TipoVehiculoRepositorio {

    private final ConexionBD conexionBD;

    /*
     * Qué hace: Inicializa el repositorio de tipos de vehículo utilizando el objeto encargado de proporcionar las conexiones con la base de datos
     * Recibe: El objeto ConexionBD que permite obtener conexiones con MySQL
     * Retorna: No retorna ningún valor
     */
    public TipoVehiculoRepositorioJdbc(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /*
     * Qué hace: Registra en la base de datos un nuevo tipo de vehículo junto con su categoría y las características que requiere
     * Recibe: El objeto TipoVehiculo que se desea registrar
     * Retorna: No retorna ningún valor
     */
    @Override
    public void agregar(TipoVehiculo tipo) {

        String sql = "INSERT INTO tipo_vehiculo " + "(nombre, categoria, requiere_pasajeros, requiere_traccion, requiere_capacidad_carga) " + "VALUES (?, ?, ?, ?, ?)";

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, tipo.getNombre());
            ps.setString(2, tipo.getCategoria().name());
            ps.setBoolean(3, tipo.isRequierePasajeros());
            ps.setBoolean(4, tipo.isRequiereTraccion());
            ps.setBoolean(5, tipo.isRequiereCapacidadCarga());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo registrar el tipo de vehículo.", e);
        }
    }

    /*
     * Qué hace: Busca en la base de datos un tipo de vehículo utilizando su nombre como identificador
     * Recibe: El nombre del tipo de vehículo que se desea buscar
     * Retorna: El tipo de vehículo encontrado o null si no existe ningún registro con ese nombre
     */
    @Override
    public TipoVehiculo buscarPorNombre(String nombre) {

        String sql = "SELECT nombre, categoria, requiere_pasajeros, " + "requiere_traccion, requiere_capacidad_carga " + "FROM tipo_vehiculo WHERE nombre = ?";

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearTipo(rs);
                }
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo buscar el tipo de vehículo.", e);
        }
    }

    /*
     * Qué hace: Obtiene todos los tipos de vehículo registrados actualmente en la base de datos
     * Recibe: No recibe parámetros
     * Retorna: Una lista que contiene todos los tipos de vehículo registrados
     */
    @Override
    public List<TipoVehiculo> obtenerTodos() {

        String sql = "SELECT nombre, categoria, requiere_pasajeros, " + "requiere_traccion, requiere_capacidad_carga " + "FROM tipo_vehiculo";

        List<TipoVehiculo> tipos = new ArrayList<>();

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tipos.add(crearTipo(rs));
            }

            return tipos;

        } catch (SQLException e) {
            throw new RuntimeException("No se pudieron cargar los tipos de vehículo.", e);
        }
    }

    /*
     * Qué hace: Actualiza la categoría y las características requeridas del tipo de vehículo indicado utilizando su nombre como identificador
     * Recibe: El objeto TipoVehiculo que contiene los datos actualizados
     * Retorna: No retorna ningún valor
     */
    @Override
    public void modificar(TipoVehiculo tipo) {

        String sql = "UPDATE tipo_vehiculo " + "SET categoria = ?, requiere_pasajeros = ?, " + "requiere_traccion = ?, requiere_capacidad_carga = ? " + "WHERE nombre = ?";

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, tipo.getCategoria().name());
            ps.setBoolean(2, tipo.isRequierePasajeros());
            ps.setBoolean(3, tipo.isRequiereTraccion());
            ps.setBoolean(4, tipo.isRequiereCapacidadCarga());
            ps.setString(5, tipo.getNombre());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo modificar el tipo de vehículo.", e);
        }
    }

    /*
     * Qué hace: Elimina de la base de datos el tipo de vehículo identificado por el nombre indicado
     * Recibe: El nombre del tipo de vehículo que se desea eliminar
     * Retorna: true si se eliminó un registro y false si no se encontró ningún tipo para eliminar
     */
    @Override
    public boolean eliminar(String nombre) {

        String sql = "DELETE FROM tipo_vehiculo WHERE nombre = ?";

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nombre);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo eliminar el tipo de vehículo.", e);
        }

    }

    /*
     * Qué hace: Construye un objeto TipoVehiculo utilizando los datos obtenidos de un registro de la base de datos
     * Recibe: El ResultSet ubicado en el registro del tipo de vehículo que se desea convertir
     * Retorna: El objeto TipoVehiculo construido con los datos obtenidos
     */
    private TipoVehiculo crearTipo(ResultSet rs) throws SQLException {

        String nombre = rs.getString("nombre");

        CategoriaVehiculo categoria = CategoriaVehiculo.valueOf(rs.getString("categoria"));

        boolean requierePasajeros = rs.getBoolean("requiere_pasajeros");
        boolean requiereTraccion = rs.getBoolean("requiere_traccion");
        boolean requiereCapacidadCarga = rs.getBoolean("requiere_capacidad_carga");

        return new TipoVehiculo(nombre, categoria, requierePasajeros, requiereTraccion, requiereCapacidadCarga);

    }

}