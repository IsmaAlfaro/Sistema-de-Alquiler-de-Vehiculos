package cr.ac.una.est.sistemaalquilercarros.app;

import cr.ac.una.est.sistemaalquilercarros.persistencia.ConexionBD;
import cr.ac.una.est.sistemaalquilercarros.persistencia.DatosSistema;
import cr.ac.una.est.sistemaalquilercarros.persistencia.PersistenciaDatos;
import cr.ac.una.est.sistemaalquilercarros.repositorio.AlquilerRepositorio;
import cr.ac.una.est.sistemaalquilercarros.repositorio.ClienteRepositorio;
import cr.ac.una.est.sistemaalquilercarros.repositorio.TipoVehiculoRepositorio;
import cr.ac.una.est.sistemaalquilercarros.repositorio.VehiculoRepositorio;
import cr.ac.una.est.sistemaalquilercarros.repositorio.jdbc.AlquilerRepositorioJdbc;
import cr.ac.una.est.sistemaalquilercarros.repositorio.jdbc.ClienteRepositorioJdbc;
import cr.ac.una.est.sistemaalquilercarros.repositorio.jdbc.TipoVehiculoRepositorioJdbc;
import cr.ac.una.est.sistemaalquilercarros.repositorio.jdbc.VehiculoRepositorioJdbc;
import cr.ac.una.est.sistemaalquilercarros.servicio.AlquilerServicio;
import cr.ac.una.est.sistemaalquilercarros.servicio.ClienteServicio;
import cr.ac.una.est.sistemaalquilercarros.servicio.TipoVehiculoServicio;
import cr.ac.una.est.sistemaalquilercarros.servicio.VehiculoServicio;

public class ContextoAplicacion {

    private final PersistenciaDatos persistencia;
    private final DatosSistema datosSistema;
    private final ConexionBD conexionBD;
    private final ClienteRepositorio clienteRepositorio;
    private final TipoVehiculoRepositorio tipoVehiculoRepositorio;
    private final VehiculoRepositorio vehiculoRepositorio;
    private final AlquilerRepositorio alquilerRepositorio;
    private final VehiculoServicio vehiculoServicio;
    private final ClienteServicio clienteServicio;
    private final AlquilerServicio alquilerServicio;
    private final TipoVehiculoServicio tipoVehiculoServicio;

    /*
     * Qué hace: Inicializa el contexto general de la aplicación creando el sistema de persistencia, cargando los datos previamente almacenados, estableciendo la conexión con MySQL y construyendo los diferentes repositorios y servicios que serán compartidos por las pantallas del programa
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    public ContextoAplicacion() {

        persistencia = new PersistenciaDatos();
        datosSistema = persistencia.cargar();

        conexionBD = new ConexionBD();

        clienteRepositorio = new ClienteRepositorioJdbc(conexionBD);

        tipoVehiculoRepositorio = new TipoVehiculoRepositorioJdbc(conexionBD);

        vehiculoRepositorio = new VehiculoRepositorioJdbc(conexionBD);

        alquilerRepositorio = new AlquilerRepositorioJdbc(conexionBD, clienteRepositorio, vehiculoRepositorio);

        tipoVehiculoServicio = new TipoVehiculoServicio(tipoVehiculoRepositorio);

        vehiculoServicio = new VehiculoServicio(vehiculoRepositorio);

        clienteServicio = new ClienteServicio(clienteRepositorio, datosSistema);

        alquilerServicio = new AlquilerServicio(alquilerRepositorio, vehiculoServicio, clienteServicio);

    }

    /*
     * Qué hace: Obtiene el servicio encargado de administrar los tipos de vehículo del sistema
     * Recibe: No recibe parámetros
     * Retorna: El objeto TipoVehiculoServicio
     */
    public TipoVehiculoServicio getTipoVehiculoServicio() {
        return tipoVehiculoServicio;
    }

    /*
     * Qué hace: Obtiene el servicio encargado de administrar los vehículos del sistema
     * Recibe: No recibe parámetros
     * Retorna: El objeto VehiculoServicio
     */
    public VehiculoServicio getVehiculoServicio() {
        return vehiculoServicio;
    }

    /*
     * Qué hace: Obtiene el servicio encargado de administrar los clientes del sistema
     * Recibe: No recibe parámetros
     * Retorna: El objeto ClienteServicio
     */
    public ClienteServicio getClienteServicio() {
        return clienteServicio;
    }

    /*
     * Qué hace: Obtiene el servicio encargado de administrar los alquileres del sistema
     * Recibe: No recibe parámetros
     * Retorna: El objeto AlquilerServicio
     */
    public AlquilerServicio getAlquilerServicio() {
        return alquilerServicio;
    }
}