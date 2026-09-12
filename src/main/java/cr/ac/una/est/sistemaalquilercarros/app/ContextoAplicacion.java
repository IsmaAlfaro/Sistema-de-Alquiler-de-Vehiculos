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

    public ContextoAplicacion() {

        persistencia = new PersistenciaDatos();

        datosSistema = persistencia.cargar();

        tipoVehiculoServicio = new TipoVehiculoServicio(datosSistema, persistencia);

        vehiculoServicio = new VehiculoServicio(datosSistema, persistencia);

        clienteServicio = new ClienteServicio(datosSistema, persistencia);

        alquilerServicio = new AlquilerServicio(datosSistema, persistencia, vehiculoServicio, clienteServicio);
    }

    public TipoVehiculoServicio getTipoVehiculoServicio() {
        return tipoVehiculoServicio;
    }

    public VehiculoServicio getVehiculoServicio() {
        return vehiculoServicio;
    }

    public ClienteServicio getClienteServicio() {
        return clienteServicio;
    }

    public AlquilerServicio getAlquilerServicio() {
        return alquilerServicio;
    }
}
