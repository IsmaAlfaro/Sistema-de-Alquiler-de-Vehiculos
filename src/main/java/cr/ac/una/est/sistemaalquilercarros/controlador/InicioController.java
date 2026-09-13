package cr.ac.una.est.sistemaalquilercarros.controlador;

import cr.ac.una.est.sistemaalquilercarros.app.ContextoAplicacion;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InicioController {

    @FXML
    private Label lblCantidadVehiculos;

    @FXML
    private Label lblVehiculosDisponibles;

    @FXML
    private Label lblCantidadClientes;

    @FXML
    private Label lblAlquileresActivos;

    private ContextoAplicacion contexto;

    /*
     * Qué hace: Recibe y almacena el contexto general de la aplicación para poder acceder a los distintos servicios y actualizar los datos mostrados en la pantalla de inicio
     * Recibe: El contexto general de la aplicación
     * Retorna: No retorna ningún valor
     */
    public void setContexto(ContextoAplicacion contexto) {

        this.contexto = contexto;

        actualizarDatos();
    }

    /*
     * Qué hace: Consulta los servicios del sistema y actualiza los indicadores de cantidad de vehículos, vehículos disponibles, clientes registrados y alquileres activos
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    private void actualizarDatos() {

        lblCantidadVehiculos.setText(String.valueOf(contexto.getVehiculoServicio().cantidadVehiculos()));

        lblVehiculosDisponibles.setText(String.valueOf(contexto.getVehiculoServicio().cantidadDisponibles()));

        lblCantidadClientes.setText(String.valueOf(contexto.getClienteServicio().cantidadClientes()));

        lblAlquileresActivos.setText(String.valueOf(contexto.getAlquilerServicio().cantidadAlquileresActivos()));
    }
}