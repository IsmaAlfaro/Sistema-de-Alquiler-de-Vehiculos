package cr.ac.una.est.sistemaalquilercarros.repositorio.jdbc;

import cr.ac.una.est.sistemaalquilercarros.modelo.CategoriaVehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.EstadoVehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.TipoVehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.Vehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.VehiculoCarga;
import cr.ac.una.est.sistemaalquilercarros.modelo.VehiculoPasajeros;
import cr.ac.una.est.sistemaalquilercarros.persistencia.ConexionBD;
import cr.ac.una.est.sistemaalquilercarros.repositorio.VehiculoRepositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class VehiculoRepositorioJdbc implements VehiculoRepositorio {

    private final ConexionBD conexionBD;

    /*
     * Qué hace: Inicializa el repositorio de vehículos utilizando el objeto encargado de proporcionar las conexiones con la base de datos
     * Recibe: El objeto ConexionBD que permite obtener conexiones con MySQL
     * Retorna: No retorna ningún valor
     */
    public VehiculoRepositorioJdbc(ConexionBD conexionBD) {

        this.conexionBD = conexionBD;

    }

    /*
     * Qué hace: Registra un nuevo vehículo en la base de datos almacenando sus datos generales y las características correspondientes según sea un vehículo de pasajeros o de carga
     * Recibe: El objeto Vehiculo que se desea registrar
     * Retorna: No retorna ningún valor
     */
    @Override
    public void agregar(Vehiculo vehiculo) {

        String sql = "INSERT INTO vehiculo " + "(placa, marca, modelo, anio, tarifa_diaria, estado, " + "tipo_nombre, cantidad_pasajeros, tipo_traccion, capacidad_carga) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, vehiculo.getPlaca());

            ps.setString(2, vehiculo.getMarca());

            ps.setString(3, vehiculo.getModelo());

            ps.setInt(4, vehiculo.getAnio());

            ps.setDouble(5, vehiculo.getTarifaDiaria());

            ps.setString(6, vehiculo.getEstado().name());

            ps.setString(7, vehiculo.getTipoVehiculo().getNombre());

            if (vehiculo instanceof VehiculoPasajeros pasajeros) {

                ps.setInt(8, pasajeros.getCantidadPasajeros());

                ps.setString(9, pasajeros.getTipoTraccion());

                ps.setNull(10, Types.DECIMAL);

            } else if (vehiculo instanceof VehiculoCarga carga) {

                ps.setNull(8, Types.INTEGER);

                ps.setString(9, carga.getTipoTraccion());

                ps.setDouble(10, carga.getCapacidadCarga());

            }

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("No se pudo registrar el vehículo.", e);

        }

    }

    /*
     * Qué hace: Busca en la base de datos un vehículo utilizando su placa y reconstruye el objeto correspondiente con los datos de su tipo de vehículo
     * Recibe: La placa del vehículo que se desea buscar
     * Retorna: El vehículo encontrado o null si no existe ningún vehículo con esa placa
     */
    @Override
    public Vehiculo buscarPorPlaca(String placa) {

        String sql = "SELECT v.placa, v.marca, v.modelo, v.anio, " + "v.tarifa_diaria, v.estado, v.tipo_nombre, " + "v.cantidad_pasajeros, v.tipo_traccion, v.capacidad_carga, " + "t.categoria, t.requiere_pasajeros, t.requiere_traccion, " + "t.requiere_capacidad_carga " + "FROM vehiculo v " + "INNER JOIN tipo_vehiculo t ON v.tipo_nombre = t.nombre " + "WHERE v.placa = ?";

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, placa);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return crearVehiculo(rs);

                }

            }

            return null;

        } catch (SQLException e) {

            throw new RuntimeException("No se pudo buscar el vehículo.", e);

        }

    }

    /*
     * Qué hace: Obtiene todos los vehículos registrados en la base de datos junto con la información correspondiente a sus tipos de vehículo
     * Recibe: No recibe parámetros
     * Retorna: Una lista que contiene todos los vehículos registrados
     */
    @Override
    public List<Vehiculo> obtenerTodos() {

        String sql = "SELECT v.placa, v.marca, v.modelo, v.anio, " + "v.tarifa_diaria, v.estado, v.tipo_nombre, " + "v.cantidad_pasajeros, v.tipo_traccion, v.capacidad_carga, " + "t.categoria, t.requiere_pasajeros, t.requiere_traccion, " + "t.requiere_capacidad_carga " + "FROM vehiculo v " + "INNER JOIN tipo_vehiculo t ON v.tipo_nombre = t.nombre";

        List<Vehiculo> vehiculos = new ArrayList<>();

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                vehiculos.add(crearVehiculo(rs));

            }

            return vehiculos;

        } catch (SQLException e) {

            throw new RuntimeException("No se pudieron cargar los vehículos.", e);

        }

    }

    /*
     * Qué hace: Obtiene de la base de datos únicamente los vehículos cuyo estado actual es DISPONIBLE
     * Recibe: No recibe parámetros
     * Retorna: Una lista que contiene los vehículos disponibles
     */
    @Override
    public List<Vehiculo> obtenerDisponibles() {

        String sql = "SELECT v.placa, v.marca, v.modelo, v.anio, " + "v.tarifa_diaria, v.estado, v.tipo_nombre, " + "v.cantidad_pasajeros, v.tipo_traccion, v.capacidad_carga, " + "t.categoria, t.requiere_pasajeros, t.requiere_traccion, " + "t.requiere_capacidad_carga " + "FROM vehiculo v " + "INNER JOIN tipo_vehiculo t ON v.tipo_nombre = t.nombre " + "WHERE v.estado = ?";

        List<Vehiculo> vehiculos = new ArrayList<>();

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, EstadoVehiculo.DISPONIBLE.name());

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    vehiculos.add(crearVehiculo(rs));

                }

            }

            return vehiculos;

        } catch (SQLException e) {

            throw new RuntimeException("No se pudieron cargar los vehículos disponibles.", e);

        }

    }

    /*
     * Qué hace: Actualiza en la base de datos los datos del vehículo indicado utilizando su placa como identificador
     * Recibe: El objeto Vehiculo que contiene los datos actualizados
     * Retorna: No retorna ningún valor
     */
    @Override
    public void modificar(Vehiculo vehiculo) {

        String sql = "UPDATE vehiculo SET " + "marca = ?, modelo = ?, anio = ?, tarifa_diaria = ?, " + "estado = ?, tipo_nombre = ?, cantidad_pasajeros = ?, " + "tipo_traccion = ?, capacidad_carga = ? " + "WHERE placa = ?";

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, vehiculo.getMarca());

            ps.setString(2, vehiculo.getModelo());

            ps.setInt(3, vehiculo.getAnio());

            ps.setDouble(4, vehiculo.getTarifaDiaria());

            ps.setString(5, vehiculo.getEstado().name());

            ps.setString(6, vehiculo.getTipoVehiculo().getNombre());

            if (vehiculo instanceof VehiculoPasajeros pasajeros) {

                ps.setInt(7, pasajeros.getCantidadPasajeros());

                ps.setString(8, pasajeros.getTipoTraccion());

                ps.setNull(9, Types.DECIMAL);

            } else if (vehiculo instanceof VehiculoCarga carga) {

                ps.setNull(7, Types.INTEGER);

                ps.setString(8, carga.getTipoTraccion());

                ps.setDouble(9, carga.getCapacidadCarga());

            }

            ps.setString(10, vehiculo.getPlaca());

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("No se pudo modificar el vehículo.", e);

        }

    }

    /*
     * Qué hace: Elimina de la base de datos el vehículo identificado por la placa indicada
     * Recibe: La placa del vehículo que se desea eliminar
     * Retorna: true si se eliminó un vehículo y false si no se encontró ningún registro para eliminar
     */
    @Override
    public boolean eliminar(String placa) {

        String sql = "DELETE FROM vehiculo WHERE placa = ?";

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, placa);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new RuntimeException("No se pudo eliminar el vehículo.", e);

        }
    }

    /*
     * Qué hace: Actualiza únicamente el estado de un vehículo almacenado en la base de datos utilizando su placa
     * Recibe: La placa del vehículo y el nuevo estado que se desea asignar
     * Retorna: No retorna ningún valor
     */
    @Override
    public void cambiarEstado(String placa, EstadoVehiculo estado) {

        String sql ="UPDATE vehiculo SET estado = ? WHERE placa = ?";

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, estado.name());

            ps.setString(2, placa);

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("No se pudo cambiar el estado del vehículo.", e);

        }
    }

    /*
     * Qué hace: Construye un objeto Vehiculo a partir de los datos obtenidos de una consulta y determina si corresponde a un vehículo de pasajeros o de carga
     * Recibe: El ResultSet ubicado en el registro del vehículo que se desea convertir
     * Retorna: El objeto Vehiculo construido con la información obtenida de la base de datos
     */
    private Vehiculo crearVehiculo(ResultSet rs) throws SQLException {

        TipoVehiculo tipo = new TipoVehiculo(rs.getString("tipo_nombre"), CategoriaVehiculo.valueOf(rs.getString("categoria")),
                rs.getBoolean("requiere_pasajeros"), rs.getBoolean("requiere_traccion"), rs.getBoolean("requiere_capacidad_carga"));

        Vehiculo vehiculo;

        if (tipo.getCategoria() == CategoriaVehiculo.PASAJEROS) {

            vehiculo = new VehiculoPasajeros(rs.getString("placa"), rs.getString("marca"), rs.getString("modelo"), rs.getInt("anio"),
                    rs.getDouble("tarifa_diaria"), tipo, rs.getInt("cantidad_pasajeros"), rs.getString("tipo_traccion"));

        } else {

            vehiculo = new VehiculoCarga(rs.getString("placa"), rs.getString("marca"), rs.getString("modelo"), rs.getInt("anio"),
                    rs.getDouble("tarifa_diaria"), tipo, rs.getDouble("capacidad_carga"), rs.getString("tipo_traccion"));

        }

        vehiculo.setEstado(EstadoVehiculo.valueOf(rs.getString("estado")));

        return vehiculo;

    }

}