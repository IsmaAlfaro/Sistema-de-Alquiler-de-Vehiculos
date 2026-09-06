package cr.ac.una.est.sistemaalquilercarros.controlador;

import cr.ac.una.est.sistemaalquilercarros.modelo.Cliente;
import cr.ac.una.est.sistemaalquilercarros.servicio.ClienteServicio;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ClientesController {

    @FXML
    private TextField txtCedula;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TableView<Cliente> tablaClientes;

    @FXML
    private TableColumn<Cliente, String> colCedula;

    @FXML
    private TableColumn<Cliente, String> colNombre;

    @FXML
    private TableColumn<Cliente, String> colTelefono;

    @FXML
    private TableColumn<Cliente, String> colCorreo;


    private ClienteServicio clienteServicio;

    @FXML
    private void initialize() {

       colCedula.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getCedula()));

       colNombre.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getNombreCompleto()));

       colTelefono.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getTelefono()));

       colCorreo.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getCorreo()));

       tablaClientes.setOnMouseClicked(evento -> cargarClienteSeleccionado());
    }

    public void setClienteServicio(ClienteServicio clienteServicio) {

        this.clienteServicio = clienteServicio;

        actualizarTabla();

    }

    @FXML
    private void registrarCliente() {

        try {

            Cliente cliente = crearClienteDesdeFormulario();
            clienteServicio.registrarCliente(cliente);

            actualizarTabla();
            limpiarFormulario();

            mostrarInformacion("Cliente registrado correctamente");
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void modificarCliente() {

        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarError("Debe seleccionar un cliente.");
            return;
        }

        try {

            Cliente modificado = crearClienteDesdeFormulario();

            clienteServicio.modificarCliente(seleccionado.getCedula(), modificado);

            actualizarTabla();
            limpiarFormulario();

            mostrarInformacion("Cliente modificado correctamente");
        }catch (IllegalArgumentException e){
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void eliminarCliente() {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarError("Debe seleccionar un cliente.");
            return;
        }

        try {

            clienteServicio.eliminarCliente(seleccionado.getCedula());

            actualizarTabla();
            limpiarFormulario();

            mostrarInformacion("Cliente eliminado con correctamente.");
        }catch (IllegalArgumentException e){
            mostrarError(e.getMessage());
        }

    }

    @FXML
    private void limpiarFormulario() {

        txtCedula.clear();
        txtNombre.clear();
        txtTelefono.clear();
        txtCorreo.clear();

        txtCedula.setDisable(false);

        tablaClientes.getSelectionModel().clearSelection();

    }

    private Cliente crearClienteDesdeFormulario() {

        return new Cliente(txtCedula.getText().trim(), txtNombre.getText().trim(), txtTelefono.getText().trim(), txtCorreo.getText().trim());

    }

    private void cargarClienteSeleccionado() {
        Cliente cliente = tablaClientes.getSelectionModel().getSelectedItem();

        if (cliente == null) {
            return;
        }

        txtCedula.setText(cliente.getCedula());

        txtNombre.setText(cliente.getNombreCompleto());

        txtTelefono.setText(cliente.getTelefono());

        txtCorreo.setText(cliente.getCorreo());

        txtCedula.setDisable(true);

    }

    private void actualizarTabla() {

        if(clienteServicio == null) {
            return;
        }

        tablaClientes.setItems(FXCollections.observableArrayList(clienteServicio.obtenerTodos()));

    }

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }

    private void mostrarInformacion(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }

}
