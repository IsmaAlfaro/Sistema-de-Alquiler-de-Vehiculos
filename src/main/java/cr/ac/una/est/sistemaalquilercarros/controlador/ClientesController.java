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

    /*
     * Qué hace: Inicializa la pantalla de clientes configurando las columnas de la tabla y el evento que permite cargar automáticamente en el formulario los datos del cliente seleccionado
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void initialize() {

       colCedula.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getCedula()));

       colNombre.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getNombreCompleto()));

       colTelefono.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getTelefono()));

       colCorreo.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getCorreo()));

       tablaClientes.setOnMouseClicked(evento -> cargarClienteSeleccionado());
    }

    /*
     * Qué hace: Asigna el servicio encargado de administrar los clientes y actualiza inmediatamente la tabla con la información disponible
     * Recibe: El servicio de clientes que utilizará el controlador
     * Retorna: No retorna ningún valor
     */
    public void setClienteServicio(ClienteServicio clienteServicio) {

        this.clienteServicio = clienteServicio;

        actualizarTabla();

    }

    /*
     * Qué hace: Crea un cliente utilizando los datos escritos en el formulario, solicita su registro al servicio y luego actualiza la tabla y limpia los campos
     * Recibe: No recibe parámetros directamente, utiliza los valores ingresados en el formulario
     * Retorna: No retorna ningún valor
     */
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

    /*
     * Qué hace: Modifica los datos del cliente seleccionado en la tabla utilizando la información actual del formulario y conserva la cédula como identificador que no puede modificarse
     * Recibe: No recibe parámetros directamente, utiliza el cliente seleccionado y los datos del formulario
     * Retorna: No retorna ningún valor
     */
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

    /*
     * Qué hace: Elimina del sistema el cliente seleccionado en la tabla, siempre que las reglas del servicio permitan realizar la eliminación
     * Recibe: No recibe parámetros directamente, utiliza el cliente seleccionado en la tabla
     * Retorna: No retorna ningún valor
     */
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

    /*
     * Qué hace: Limpia todos los campos del formulario de clientes, vuelve a habilitar la edición de la cédula y elimina cualquier selección existente en la tabla
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void limpiarFormulario() {

        txtCedula.clear();
        txtNombre.clear();
        txtTelefono.clear();
        txtCorreo.clear();

        txtCedula.setDisable(false);

        tablaClientes.getSelectionModel().clearSelection();

    }

    /*
     * Qué hace: Construye un nuevo objeto Cliente utilizando los valores escritos actualmente en los campos del formulario
     * Recibe: No recibe parámetros directamente, obtiene los datos desde los TextField de la pantalla
     * Retorna: Un objeto Cliente creado con la información ingresada
     */
    private Cliente crearClienteDesdeFormulario() {

        return new Cliente(txtCedula.getText().trim(), txtNombre.getText().trim(), txtTelefono.getText().trim(), txtCorreo.getText().trim());

    }

    /*
     * Qué hace: Obtiene el cliente seleccionado en la tabla y coloca sus datos en los campos del formulario para permitir su consulta o modificación
     * Recibe: No recibe parámetros directamente, utiliza la selección actual de la tabla
     * Retorna: No retorna ningún valor
     */
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

    /*
     * Qué hace: Actualiza la tabla de clientes cargando todos los registros disponibles actualmente desde el servicio de clientes
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    private void actualizarTabla() {

        if(clienteServicio == null) {
            return;
        }

        tablaClientes.setItems(FXCollections.observableArrayList(clienteServicio.obtenerTodos()));

    }

    /*
     * Qué hace: Muestra una ventana de alerta de tipo error para informar al usuario sobre un problema ocurrido durante una operación con clientes
     * Recibe: El mensaje de error que se desea mostrar
     * Retorna: No retorna ningún valor
     */
    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }

    /*
     * Qué hace: Muestra una ventana informativa para comunicar al usuario que una operación relacionada con clientes finalizó correctamente
     * Recibe: El mensaje informativo que se desea mostrar
     * Retorna: No retorna ningún valor
     */
    private void mostrarInformacion(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }

}
