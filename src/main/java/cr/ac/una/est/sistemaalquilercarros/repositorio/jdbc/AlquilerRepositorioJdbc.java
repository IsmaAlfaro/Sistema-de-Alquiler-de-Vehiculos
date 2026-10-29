package cr.ac.una.est.sistemaalquilercarros.repositorio.jdbc;

import cr.ac.una.est.sistemaalquilercarros.modelo.Alquiler;
import cr.ac.una.est.sistemaalquilercarros.modelo.Cliente;
import cr.ac.una.est.sistemaalquilercarros.modelo.EstadoAlquiler;
import cr.ac.una.est.sistemaalquilercarros.modelo.EstadoVehiculo;
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

    /*
     * Qué hace: Inicializa el repositorio de alquileres utilizando la conexión con la base de datos y los repositorios necesarios para recuperar clientes y vehículos relacionados
     * Recibe: El objeto ConexionBD, el repositorio de clientes y el repositorio de vehículos
     * Retorna: No retorna ningún valor
     */
    public AlquilerRepositorioJdbc(ConexionBD conexionBD, ClienteRepositorio clienteRepositorio, VehiculoRepositorio vehiculoRepositorio) {
        this.conexionBD = conexionBD;
        this.clienteRepositorio = clienteRepositorio;
        this.vehiculoRepositorio = vehiculoRepositorio;
    }

    /*
     * Qué hace: Registra un nuevo alquiler y cambia el vehículo relacionado de DISPONIBLE a ALQUILADO dentro de una misma transacción, deshaciendo los cambios si alguna operación falla
     * Recibe: El objeto Alquiler que se desea registrar
     * Retorna: El alquiler registrado con el número generado automáticamente por la base de datos
     */
    @Override
    public Alquiler agregar(Alquiler alquiler) {

        String sqlAlquiler = "INSERT INTO alquiler " + "(cedula_cliente, placa_vehiculo, fecha_inicio, " + "cantidad_dias, fecha_devolucion_real, " + "tarifa_diaria_aplicada, deposito_garantia, estado) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlVehiculo = "UPDATE vehiculo " + "SET estado = ? " + "WHERE placa = ? AND estado = ?";

        try (Connection cn = conexionBD.obtenerConexion()) {

            cn.setAutoCommit(false);

            try {

                int numeroAlquiler;

                try (PreparedStatement ps = cn.prepareStatement(sqlAlquiler, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setString(1, alquiler.getCliente().getCedula());
                    ps.setString(2, alquiler.getVehiculo().getPlaca());
                    ps.setDate(3, Date.valueOf(alquiler.getFechaInicio()));
                    ps.setInt(4, alquiler.getCantidadDias());
                    ps.setNull(5, Types.DATE);
                    ps.setDouble(6, alquiler.getTarifaDiariaAplicada());
                    ps.setDouble(7, alquiler.getDepositoGarantia());
                    ps.setString(8, alquiler.getEstado().name());
                    ps.executeUpdate();

                    try (ResultSet claves = ps.getGeneratedKeys()) {

                        if (!claves.next()) {

                            throw new SQLException("No se pudo generar el número del alquiler.");
                        }

                        numeroAlquiler = claves.getInt(1);
                    }
                }

                try (PreparedStatement ps = cn.prepareStatement(sqlVehiculo)) {

                    ps.setString(1, EstadoVehiculo.ALQUILADO.name());
                    ps.setString(2, alquiler.getVehiculo().getPlaca());
                    ps.setString(3, EstadoVehiculo.DISPONIBLE.name());

                    int filas = ps.executeUpdate();

                    if (filas != 1) {

                        throw new SQLException("El vehículo ya no está disponible.");
                    }
                }

                cn.commit();

                return new Alquiler(numeroAlquiler, alquiler.getCliente(), alquiler.getVehiculo(), alquiler.getFechaInicio(), alquiler.getCantidadDias(), alquiler.getFechaDevolucionReal(), alquiler.getTarifaDiariaAplicada(), alquiler.getDepositoGarantia(), alquiler.getEstado());

            } catch (SQLException e) {
                cn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo registrar el alquiler.", e);
        }
    }

    /*
     * Qué hace: Busca un alquiler en la base de datos utilizando su número de alquiler
     * Recibe: El número del alquiler que se desea buscar
     * Retorna: El alquiler encontrado o null si no existe ningún alquiler con ese número
     */
    @Override
    public Alquiler buscarPorNumero(int numeroAlquiler) {

        String sql = "SELECT * FROM alquiler " + "WHERE numero_alquiler = ?";

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

    /*
     * Qué hace: Obtiene todos los alquileres registrados actualmente en la base de datos
     * Recibe: No recibe parámetros
     * Retorna: Una lista que contiene todos los alquileres registrados
     */
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

    /*
     * Qué hace: Obtiene únicamente los alquileres cuyo estado actual es ACTIVO
     * Recibe: No recibe parámetros
     * Retorna: Una lista que contiene todos los alquileres activos
     */
    @Override
    public List<Alquiler> obtenerActivos() {

        String sql = "SELECT * FROM alquiler " + "WHERE estado = ?";

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

    /*
     * Qué hace: Finaliza un alquiler activo registrando su fecha real de devolución y cambiando el vehículo relacionado de ALQUILADO a DISPONIBLE dentro de una misma transacción, deshaciendo los cambios si alguna operación falla
     * Recibe: El objeto Alquiler que contiene los datos de la devolución que se desea finalizar
     * Retorna: No retorna ningún valor
     */
    @Override
    public void finalizar(Alquiler alquiler) {

        String sqlAlquiler = "UPDATE alquiler " + "SET fecha_devolucion_real = ?, estado = ? " + "WHERE numero_alquiler = ? AND estado = ?";

        String sqlVehiculo = "UPDATE vehiculo " + "SET estado = ? " + "WHERE placa = ? AND estado = ?";

        try (Connection cn = conexionBD.obtenerConexion()) {

            cn.setAutoCommit(false);

            try {

                try (PreparedStatement ps = cn.prepareStatement(sqlAlquiler)) {

                    ps.setDate(1, Date.valueOf(alquiler.getFechaDevolucionReal()));
                    ps.setString(2, EstadoAlquiler.FINALIZADO.name());
                    ps.setInt(3, alquiler.getNumeroAlquiler());
                    ps.setString(4, EstadoAlquiler.ACTIVO.name());

                    int filas = ps.executeUpdate();

                    if (filas != 1) {
                        throw new SQLException("El alquiler ya no está activo.");
                    }
                }

                try (PreparedStatement ps = cn.prepareStatement(sqlVehiculo)) {

                    ps.setString(1, EstadoVehiculo.DISPONIBLE.name());
                    ps.setString(2, alquiler.getVehiculo().getPlaca());
                    ps.setString(3, EstadoVehiculo.ALQUILADO.name());

                    int filas = ps.executeUpdate();

                    if (filas != 1) {
                        throw new SQLException("No se pudo liberar el vehículo.");
                    }
                }

                cn.commit();

            } catch (SQLException e) {

                cn.rollback();
                throw e;

            }

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo finalizar el alquiler.", e);
        }
    }

    /*
     * Qué hace: Elimina de la base de datos el alquiler identificado por su número de alquiler
     * Recibe: El número del alquiler que se desea eliminar
     * Retorna: true si se eliminó un alquiler y false si no se encontró ningún registro para eliminar
     */
    @Override
    public boolean eliminar(int numeroAlquiler) {

        String sql = "DELETE FROM alquiler " + "WHERE numero_alquiler = ?";

        try (Connection cn = conexionBD.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, numeroAlquiler);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo eliminar el alquiler.", e);
        }
    }

    /*
     * Qué hace: Construye un objeto Alquiler utilizando los datos obtenidos de una consulta y recupera los objetos Cliente y Vehiculo relacionados por medio de sus respectivos repositorios
     * Recibe: El ResultSet ubicado en el registro del alquiler que se desea convertir
     * Retorna: El objeto Alquiler construido con la información obtenida de la base de datos
     */
    private Alquiler crearAlquiler(ResultSet rs) throws SQLException {

        Cliente cliente = clienteRepositorio.buscarPorCedula(rs.getString("cedula_cliente"));
        Vehiculo vehiculo = vehiculoRepositorio.buscarPorPlaca(rs.getString("placa_vehiculo"));
        Date fechaReal = rs.getDate("fecha_devolucion_real");
        LocalDate fechaDevolucionReal = fechaReal == null ? null : fechaReal.toLocalDate();

        return new Alquiler(rs.getInt("numero_alquiler"), cliente, vehiculo, rs.getDate("fecha_inicio").toLocalDate(), rs.getInt("cantidad_dias"), fechaDevolucionReal, rs.getDouble("tarifa_diaria_aplicada"), rs.getDouble("deposito_garantia"), EstadoAlquiler.valueOf(rs.getString("estado")));

    }
}