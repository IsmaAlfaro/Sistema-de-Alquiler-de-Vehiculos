package cr.ac.una.est.sistemaalquilercarros.controlador;

import cr.ac.una.est.sistemaalquilercarros.app.ContextoAplicacion;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class PrincipalController {

    private ContextoAplicacion contexto;

    /*
     * Qué hace: Asigna el contexto general de la aplicación al controlador principal y carga automáticamente la pantalla de inicio como vista inicial
     * Recibe: El contexto general de la aplicación con acceso a todos los servicios
     * Retorna: No retorna ningún valor
     */
    public void setContexto(ContextoAplicacion contexto) {

        this.contexto = contexto;

        mostrarInicio();

    }

    @FXML
    private BorderPane panelPrincipal;

    /*
     * Qué hace: Carga el archivo FXML correspondiente a la pantalla de inicio, asigna su controlador y coloca la vista resultante en el centro de la ventana principal
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
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

    /*
     * Qué hace: Carga la pantalla de gestión de vehículos, conecta los servicios de vehículos y tipos de vehículo con su controlador y muestra la vista en la ventana principal
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void mostrarVehiculos() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/est/sistemaalquilercarros/fxml/vehiculos.fxml"));

            Parent vista = loader.load();

            VehiculosController controller = loader.getController();

            controller.setServicios(contexto.getVehiculoServicio(), contexto.getTipoVehiculoServicio());

            panelPrincipal.setCenter(vista);

        } catch (IOException e) {
            mostrarMensaje("No se pudo cargar la pantalla de vehículos.");
            e.printStackTrace();
        }
    }

    /*
     * Qué hace: Carga la pantalla utilizada para administrar los tipos de vehículo, asigna su servicio correspondiente y muestra la vista dentro del panel principal
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void mostrarTiposVehiculo() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/est/sistemaalquilercarros/fxml/tiposVehiculo.fxml"));

            Parent vista = loader.load();

            TiposVehiculoController controller = loader.getController();

            controller.setTipoVehiculoServicio(contexto.getTipoVehiculoServicio());

            panelPrincipal.setCenter(vista);

        } catch (IOException e) {
            mostrarMensaje("No se pudo cargar la pantalla de tipos de vehículos.");
            e.printStackTrace();
        }
    }

    /*
     * Qué hace: Carga la pantalla de gestión de clientes, asigna el servicio de clientes a su controlador y muestra la vista dentro de la ventana principal
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void mostrarClientes() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/est/sistemaalquilercarros/fxml/cliente.fxml"));

            Parent vista = loader.load();

            ClientesController controller = loader.getController();

            controller.setClienteServicio(contexto.getClienteServicio());

            panelPrincipal.setCenter(vista);

        } catch (IOException e) {
            mostrarMensaje("No se pudo cargar la pantalla de clientes.");
            e.printStackTrace();
        }
    }

    /*
     * Qué hace: Carga la pantalla de alquileres, conecta los servicios de clientes, vehículos y alquileres con su controlador y muestra la vista en el panel principal
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void mostrarAlquileres() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/est/sistemaalquilercarros/fxml/alquiler.fxml"));

            Parent vista = loader.load();

            AlquileresController controller = loader.getController();

            controller.setServicios(contexto.getClienteServicio(), contexto.getVehiculoServicio(), contexto.getAlquilerServicio());

            panelPrincipal.setCenter(vista);

        } catch (IOException e) {
            mostrarMensaje("No se pudo cargar la pantalla de alquileres.");
            e.printStackTrace();
        }
    }

    /*
     * Qué hace: Carga la pantalla de devolución de vehículos, asigna el servicio de alquileres a su controlador y muestra la vista dentro del panel principal
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
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

    /*
     * Qué hace: Carga la pantalla del historial de alquileres, asigna el servicio correspondiente a su controlador y muestra la vista dentro de la ventana principal
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
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

    /*
     * Qué hace: Finaliza completamente la ejecución de la aplicación JavaFX cuando el usuario selecciona la opción de cerrar el sistema
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void cerrarAplicacion() {
        Platform.exit();
    }

    /*
     * Qué hace: Muestra un mensaje directamente en el centro de la ventana principal cuando no es posible cargar alguna de las pantallas del sistema
     * Recibe: El mensaje que se desea mostrar al usuario
     * Retorna: No retorna ningún valor
     */
    private void mostrarMensaje(String mensaje) {

        Label label = new Label(mensaje);

        label.setStyle("-fx-font-size: 24px;");

        panelPrincipal.setCenter(label);
    }
}
