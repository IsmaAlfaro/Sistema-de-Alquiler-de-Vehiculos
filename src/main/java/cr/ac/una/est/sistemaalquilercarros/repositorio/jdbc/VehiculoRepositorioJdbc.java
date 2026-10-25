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

    public VehiculoRepositorioJdbc(ConexionBD conexionBD) {

        this.conexionBD = conexionBD;

    }

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
    private Vehiculo crearVehiculo(ResultSet rs) throws SQLException {

        TipoVehiculo tipo = new TipoVehiculo(rs.getString("tipo_nombre"), CategoriaVehiculo.valueOf(rs.getString("categoria")), rs.getBoolean("requiere_pasajeros"), rs.getBoolean("requiere_traccion"), rs.getBoolean("requiere_capacidad_carga"));

        Vehiculo vehiculo;

        if (tipo.getCategoria() == CategoriaVehiculo.PASAJEROS) {

            vehiculo = new VehiculoPasajeros(rs.getString("placa"), rs.getString("marca"), rs.getString("modelo"), rs.getInt("anio"), rs.getDouble("tarifa_diaria"), tipo, rs.getInt("cantidad_pasajeros"), rs.getString("tipo_traccion"));

        } else {

            vehiculo = new VehiculoCarga(rs.getString("placa"), rs.getString("marca"), rs.getString("modelo"), rs.getInt("anio"), rs.getDouble("tarifa_diaria"), tipo, rs.getDouble("capacidad_carga"), rs.getString("tipo_traccion"));

        }

        vehiculo.setEstado(EstadoVehiculo.valueOf(rs.getString("estado")));

        return vehiculo;

    }
}