package cr.ac.una.est.sistemaalquilercarros.controlador;
import cr.ac.una.est.sistemaalquilercarros.app.ContextoAplicacion;
import javafx.application.Platform; import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
public class PrincipalController {

    private ContextoAplicacion contexto;

    public void setContexto(ContextoAplicacion contexto) {
        this.contexto = contexto; mostrarInicio();
    }

    @FXML
    private BorderPane panelPrincipal;

    @FXML
    private void mostrarInicio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/est/sistemaalquilercarros/fxml/inicio.fxml"));
            Parent vista = loader.load();
            InicioController controller = loader.getController();
            controller.setContexto(contexto);
            panelPrincipal.setCenter(vista);
        } catch (IOException e) {
            mostrarMensaje("No se pudo cargar la pantalla de inicio.");
            e.printStackTrace();
        }
    }
    @FXML
    private void mostrarVehiculos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/est/sistemaalquilercarros/fxml/vehiculos.fxml"));
            Parent vista = loader.load();
            VehiculosController controller = loader.getController();
            controller.setVehiculoServicio(contexto.getVehiculoServicio());
            panelPrincipal.setCenter(vista);
        } catch (IOException e) {
            mostrarMensaje("No se pudo cargar la pantalla de vehículos.");
            e.printStackTrace();
        }
    }
    @FXML
    private void mostrarClientes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/est/sistemaalquilercarros/fxml/clientes.fxml"));
            Parent vista = loader.load();
            ClientesController controller = loader.getController();
            controller.setClienteServicio(contexto.getClienteServicio());
            panelPrincipal.setCenter(vista);
        } catch (IOException e) {
            mostrarMensaje("No se pudo cargar la pantalla de clientes.");
            e.printStackTrace();
        }
    }
    @FXML
    private void mostrarAlquileres() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/est/sistemaalquilercarros/fxml/alquileres.fxml"));
            Parent vista = loader.load();
            AlquileresController controller = loader.getController();
            controller.setServicios(contexto.getClienteServicio(), contexto.getVehiculoServicio(), contexto.getAlquilerServicio());
            panelPrincipal.setCenter(vista);
        } catch (IOException e) {
            mostrarMensaje("No se pudo cargar la pantalla de alquileres.");
            e.printStackTrace();
        }
    }
    @FXML
    private void mostrarDevolucion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/est/sistemaalquilercarros/fxml/devolucion.fxml"));
            Parent vista = loader.load();
            DevolucionController controller = loader.getController();
            controller.setAlquilerServicio(contexto.getAlquilerServicio());
            panelPrincipal.setCenter(vista);
        } catch (IOException e) {
            mostrarMensaje("No se pudo cargar la pantalla de devolución.");
            e.printStackTrace();
        }
    }
    @FXML
    private void mostrarHistorial() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/est/sistemaalquilercarros/fxml/historial.fxml"));
            Parent vista = loader.load();
            HistorialController controller = loader.getController();
            controller.setAlquilerServicio(contexto.getAlquilerServicio());
            panelPrincipal.setCenter(vista);
        } catch (IOException e) {
            mostrarMensaje("No se pudo cargar el historial.");
            e.printStackTrace();
        }
    }
    @FXML
    private void cerrarAplicacion() {
        Platform.exit();
    }

    private void mostrarMensaje(String mensaje) {
        Label label = new Label(mensaje); label.setStyle("-fx-font-size: 24px;"); panelPrincipal.setCenter(label);
    }
}