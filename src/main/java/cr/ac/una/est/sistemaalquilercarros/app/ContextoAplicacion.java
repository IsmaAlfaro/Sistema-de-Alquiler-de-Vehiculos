package cr.ac.una.est.sistemaalquilercarros.app;

import cr.ac.una.est.sistemaalquilercarros.persistencia.DatosSistema;
import cr.ac.una.est.sistemaalquilercarros.persistencia.PersistenciaDatos;
import cr.ac.una.est.sistemaalquilercarros.servicio.AlquilerServicio;
import cr.ac.una.est.sistemaalquilercarros.servicio.ClienteServicio;
import cr.ac.una.est.sistemaalquilercarros.servicio.VehiculoServicio;
import cr.ac.una.est.sistemaalquilercarros.servicio.TipoVehiculoServicio;

public class ContextoAplicacion {

    private final PersistenciaDatos persistencia;
    private final DatosSistema datosSistema;
    private final VehiculoServicio vehiculoServicio;
    private final ClienteServicio clienteServicio;
    private final AlquilerServicio alquilerServicio;
    private final TipoVehiculoServicio tipoVehiculoServicio;

    /*
     * Qué hace: Inicializa el contexto general de la aplicación creando el sistema de persistencia, cargando los datos previamente almacenados y construyendo los diferentes servicios que serán compartidos por las pantallas del programa
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    public ContextoAplicacion() {

        persistencia = new PersistenciaDatos();

        datosSistema = persistencia.cargar();

        tipoVehiculoServicio = new TipoVehiculoServicio(datosSistema, persistencia);

        vehiculoServicio = new VehiculoServicio(datosSistema, persistencia);

        clienteServicio = new ClienteServicio(datosSistema, persistencia);

        alquilerServicio = new AlquilerServicio(datosSistema, persistencia, vehiculoServicio, clienteServicio);
    }

    /*
     * Qué hace: Permite acceder a la instancia del servicio encargado de administrar los tipos de vehículo utilizada de forma compartida dentro de la aplicación
     * Recibe: No recibe parámetros
     * Retorna: La instancia de TipoVehiculoServicio utilizada por el sistema
     */
    public TipoVehiculoServicio getTipoVehiculoServicio() {
        return tipoVehiculoServicio;
    }

    /*
     * Qué hace: Permite acceder a la instancia del servicio encargado de administrar los vehículos utilizada de forma compartida dentro de la aplicación
     * Recibe: No recibe parámetros
     * Retorna: La instancia de VehiculoServicio utilizada por el sistema
     */
    public VehiculoServicio getVehiculoServicio() {
        return vehiculoServicio;
    }

    /*
     * Qué hace: Permite acceder a la instancia del servicio encargado de administrar los clientes utilizada de forma compartida dentro de la aplicación
     * Recibe: No recibe parámetros
     * Retorna: La instancia de ClienteServicio utilizada por el sistema
     */
    public ClienteServicio getClienteServicio() {
        return clienteServicio;
    }

    /*
     * Qué hace: Permite acceder a la instancia del servicio encargado de administrar los alquileres y devoluciones utilizada de forma compartida dentro de la aplicación
     * Recibe: No recibe parámetros
     * Retorna: La instancia de AlquilerServicio utilizada por el sistema
     */
    public AlquilerServicio getAlquilerServicio() {
        return alquilerServicio;
    }

}
