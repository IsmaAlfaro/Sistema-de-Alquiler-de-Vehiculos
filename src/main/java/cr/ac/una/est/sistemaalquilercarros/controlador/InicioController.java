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

    public void setContexto(ContextoAplicacion contexto) {

        this.contexto = contexto;

        actualizarDatos();
    }

    private void actualizarDatos() {

        lblCantidadVehiculos.setText(String.valueOf(contexto.getVehiculoServicio().cantidadVehiculos()));

        lblVehiculosDisponibles.setText(String.valueOf(contexto.getVehiculoServicio().cantidadDisponibles()));

        lblCantidadClientes.setText(String.valueOf(contexto.getClienteServicio().cantidadClientes()));

        lblAlquileresActivos.setText(String.valueOf(contexto.getAlquilerServicio().cantidadAlquileresActivos()));
    }
}