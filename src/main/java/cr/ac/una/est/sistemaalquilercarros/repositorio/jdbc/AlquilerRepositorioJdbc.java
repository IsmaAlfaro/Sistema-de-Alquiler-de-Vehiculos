package cr.ac.una.est.sistemaalquilercarros.repositorio.jdbc;

import cr.ac.una.est.sistemaalquilercarros.modelo.Alquiler;
import cr.ac.una.est.sistemaalquilercarros.modelo.Cliente;
import cr.ac.una.est.sistemaalquilercarros.modelo.EstadoAlquiler;
import cr.ac.una.est.sistemaalquilercarros.modelo.Vehiculo;
import cr.ac.una.est.sistemaalquilercarros.persistencia.ConexionBD;
import cr.ac.una.est.sistemaalquilercarros.repositorio.AlquilerRepositorio;
import cr.ac.una.est.sistemaalquilercarros.repositorio.ClienteRepositorio;
import cr.ac.una.est.sistemaalquilercarros.repositorio.VehiculoRepositorio;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AlquilerRepositorioJdbc implements AlquilerRepositorio {

    private final ConexionBD conexionBD;
    private final ClienteRepositorio clienteRepositorio;
    private final VehiculoRepositorio vehiculoRepositorio;

    public AlquilerRepositorioJdbc(ConexionBD conexionBD, ClienteRepositorio clienteRepositorio, VehiculoRepositorio vehiculoRepositorio) {

        this.conexionBD = conexionBD;
        this.clienteRepositorio = clienteRepositorio;
        this.vehiculoRepositorio = vehiculoRepositorio;

    }

    @Override
    public Alquiler agregar(Alquiler alquiler) {

        String sql ="INSERT INTO alquiler " + "(cedula_cliente, placa_vehiculo, fecha_inicio, " + "cantidad_dias, fecha_devolucion_real, " + "tarifa_diaria_aplicada, deposito_garantia, estado) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, alquiler.getCliente().getCedula());

            ps.setString(2, alquiler.getVehiculo().getPlaca());

            ps.setDate(3, Date.valueOf(alquiler.getFechaInicio()));

            ps.setInt(4, alquiler.getCantidadDias());

            if (alquiler.getFechaDevolucionReal() == null) {

                ps.setNull(5, Types.DATE);

            } else {

                ps.setDate(5, Date.valueOf(alquiler.getFechaDevolucionReal()));

            }

            ps.setDouble(6, alquiler.getTarifaDiariaAplicada());

            ps.setDouble(7, alquiler.getDepositoGarantia());

            ps.setString(8, alquiler.getEstado().name());

            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {

                if (claves.next()) {

                    int numero = claves.getInt(1);

                    return new Alquiler(numero, alquiler.getCliente(), alquiler.getVehiculo(), alquiler.getFechaInicio(), alquiler.getCantidadDias(), alquiler.getFechaDevolucionReal(), alquiler.getTarifaDiariaAplicada(), alquiler.getDepositoGarantia(), alquiler.getEstado());

                }
            }

            throw new RuntimeException("No se pudo obtener el número del alquiler.");

        } catch (SQLException e) {

            throw new RuntimeException("No se pudo registrar el alquiler.", e);

        }
    }

    @Override
    public Alquiler buscarPorNumero(int numeroAlquiler) {

        String sql ="SELECT * FROM alquiler " + "WHERE numero_alquiler = ?";

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, numeroAlquiler);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return crearAlquiler(rs);

                }

            }

            return null;

        } catch (SQLException e) {

            throw new RuntimeException("No se pudo buscar el alquiler.", e);

        }
    }

    @Override
    public List<Alquiler> obtenerTodos() {

        String sql = "SELECT * FROM alquiler";

        List<Alquiler> alquileres = new ArrayList<>();

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                alquileres.add(crearAlquiler(rs));
            }

            return alquileres;

        } catch (SQLException e) {

            throw new RuntimeException("No se pudieron cargar los alquileres.", e);

        }
    }

    @Override
    public List<Alquiler> obtenerActivos() {

        String sql ="SELECT * FROM alquiler " + "WHERE estado = ?";

        List<Alquiler> alquileres = new ArrayList<>();

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, EstadoAlquiler.ACTIVO.name());

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    alquileres.add(crearAlquiler(rs));

                }

            }

            return alquileres;

        } catch (SQLException e) {

            throw new RuntimeException("No se pudieron cargar los alquileres activos.", e);

        }
    }

    @Override
    public void modificar(Alquiler alquiler) {

        String sql ="UPDATE alquiler SET " + "cedula_cliente = ?, " + "placa_vehiculo = ?, " + "fecha_inicio = ?, " + "cantidad_dias = ?, " + "fecha_devolucion_real = ?, " + "tarifa_diaria_aplicada = ?, " + "deposito_garantia = ?, " + "estado = ? " + "WHERE numero_alquiler = ?";

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, alquiler.getCliente().getCedula());

            ps.setString(2, alquiler.getVehiculo().getPlaca());

            ps.setDate(3, Date.valueOf(alquiler.getFechaInicio()));

            ps.setInt(4, alquiler.getCantidadDias());

            if (alquiler.getFechaDevolucionReal() == null) {

                ps.setNull(5, Types.DATE);

            } else {

                ps.setDate(5, Date.valueOf(alquiler.getFechaDevolucionReal()));

            }

            ps.setDouble(6, alquiler.getTarifaDiariaAplicada());

            ps.setDouble(7, alquiler.getDepositoGarantia());

            ps.setString(8, alquiler.getEstado().name());

            ps.setInt(9, alquiler.getNumeroAlquiler());

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("No se pudo modificar el alquiler.", e);

        }
    }

    @Override
    public boolean eliminar(int numeroAlquiler) {

        String sql ="DELETE FROM alquiler " + "WHERE numero_alquiler = ?";

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, numeroAlquiler);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new RuntimeException("No se pudo eliminar el alquiler.", e);

        }
    }

    private Alquiler crearAlquiler(ResultSet rs) throws SQLException {

        Cliente cliente = clienteRepositorio.buscarPorCedula(rs.getString("cedula_cliente"));

        Vehiculo vehiculo = vehiculoRepositorio.buscarPorPlaca(rs.getString("placa_vehiculo"));

        Date fechaReal = rs.getDate("fecha_devolucion_real");

        LocalDate fechaDevolucionReal = fechaReal == null ? null : fechaReal.toLocalDate();

        return new Alquiler(rs.getInt("numero_alquiler"), cliente, vehiculo, rs.getDate("fecha_inicio").toLocalDate(), rs.getInt("cantidad_dias"), fechaDevolucionReal, rs.getDouble("tarifa_diaria_aplicada"), rs.getDouble("deposito_garantia"), EstadoAlquiler.valueOf(rs.getString("estado")));

    }
}