package cr.ac.una.est.sistemaalquilercarros.controlador;

import cr.ac.una.est.sistemaalquilercarros.modelo.Alquiler;
import cr.ac.una.est.sistemaalquilercarros.modelo.Cliente;
import cr.ac.una.est.sistemaalquilercarros.modelo.Vehiculo;
import cr.ac.una.est.sistemaalquilercarros.servicio.AlquilerServicio;
import cr.ac.una.est.sistemaalquilercarros.servicio.ClienteServicio;
import cr.ac.una.est.sistemaalquilercarros.servicio.VehiculoServicio;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

public class AlquileresController {

    @FXML
    private ComboBox<Cliente> cmbCliente;

    @FXML
    private ComboBox<Vehiculo> cmbVehiculo;

    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private TextField txtCantidadDias;

    @FXML
    private Label lblTarifaDiaria;

    @FXML
    private Label lblSubtotal;

    @FXML
    private Label lblDeposito;

    @FXML
    private Label lblTotal;

    @FXML
    private TableView<Alquiler> tablaAlquileres;

    @FXML
    private TableColumn<Alquiler, Integer> colNumero;

    @FXML
    private TableColumn<Alquiler, String> colCliente;

    @FXML
    private TableColumn<Alquiler, String> colVehiculo;

    @FXML
    private TableColumn<Alquiler, LocalDate> colFechaInicio;

    @FXML
    private TableColumn<Alquiler, LocalDate> colFechaEsperada;

    @FXML
   private TableColumn<Alquiler, Integer> colDias;

    @FXML
    private TableColumn<Alquiler, String> colTotal;

    @FXML
    private TableColumn<Alquiler, String> colEstado;

    private ClienteServicio clienteServicio;
    private VehiculoServicio vehiculoServicio;
    private AlquilerServicio alquilerServicio;

    /*
     * Qué hace: Inicializa la pantalla de alquileres configurando la fecha actual, los valores iniciales de los labels, las columnas de la tabla y el evento que actualiza automáticamente la tarifa diaria cuando se selecciona un vehículo
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void initialize() {

        dpFechaInicio.setValue(LocalDate.now());

        lblTarifaDiaria.setText("₡0.00");
        lblSubtotal.setText("₡0.00");
        lblDeposito.setText("₡0.00");
        lblTotal.setText("₡0.00");

        colNumero.setCellValueFactory(dato -> new ReadOnlyObjectWrapper<>(dato.getValue().getNumeroAlquiler()));

        colCliente.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getCliente().getNombreCompleto()));

        colVehiculo.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getVehiculo().toString()));

        colFechaInicio.setCellValueFactory(dato -> new ReadOnlyObjectWrapper<>(dato.getValue().getFechaInicio()));

        colFechaEsperada.setCellValueFactory(dato -> new ReadOnlyObjectWrapper<>(dato.getValue().getFechaDevolucionEsperada()));

        colDias.setCellValueFactory(dato -> new ReadOnlyObjectWrapper<>(dato.getValue().getCantidadDias()));

        colTotal.setCellValueFactory(dato -> new SimpleStringProperty(String.format("₡%,.2f", dato.getValue().calcularTotal())));

        colEstado.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getEstado().toString()));

        cmbVehiculo.valueProperty().addListener((observable, vehiculoAnterior, vehiculoNuevo) -> {actualizarTarifaDiaria();});

    }

    /*
     * Qué hace: Recibe y almacena los servicios necesarios para trabajar con clientes, vehículos y alquileres, y posteriormente carga en la pantalla la información disponible
     * Recibe: El servicio de clientes, el servicio de vehículos y el servicio de alquileres
     * Retorna: No retorna ningún valor
     */
    public void setServicios(ClienteServicio clienteServicio, VehiculoServicio vehiculoServicio, AlquilerServicio alquilerServicio) {
        this.clienteServicio = clienteServicio;
        this.vehiculoServicio = vehiculoServicio;
        this.alquilerServicio = alquilerServicio;

        cargarDatos();
    }

    /*
     * Qué hace: Carga en los ComboBox los clientes y vehículos disponibles, actualiza la tabla con los alquileres registrados y muestra el depósito de garantía establecido por el sistema
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    private void cargarDatos() {
        cmbCliente.setItems(FXCollections.observableArrayList(clienteServicio.obtenerTodos()));

        cmbVehiculo.setItems(FXCollections.observableArrayList(vehiculoServicio.obtenerDisponibles()));

        tablaAlquileres.setItems(FXCollections.observableArrayList(alquilerServicio.obtenerTodos()));

        lblDeposito.setText(String.format("₡%,.2f", alquilerServicio.getDepositoGarantia()));
    }

    /*
     * Qué hace: Actualiza el label que muestra la tarifa diaria del vehículo seleccionado para que el usuario pueda conocer su precio antes de realizar el cálculo del alquiler
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    private void actualizarTarifaDiaria() {

        Vehiculo vehiculo = cmbVehiculo.getValue();

        if (vehiculo == null) {
            lblTarifaDiaria.setText("₡0.00");
            return;
        }

        lblTarifaDiaria.setText(String.format("₡%,.2f", vehiculo.getTarifaDiaria()));
    }


    /*
     * Qué hace: Calcula de manera previa el subtotal, el depósito de garantía y el monto total del alquiler utilizando el vehículo seleccionado y la cantidad de días indicada por el usuario
     * Recibe: No recibe parámetros directamente, utiliza los datos ingresados y seleccionados en el formulario
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void calcularAlquiler(){

        try {

            Vehiculo vehiculo = cmbVehiculo.getValue();

            if(vehiculo == null){
                throw new IllegalArgumentException("Debe seleccionar un vehículo.");
            }

            int dias = Integer.parseInt(txtCantidadDias.getText().trim());

            if(dias <= 0){
                throw new IllegalArgumentException("La cantidad de días debe ser mayor a cero.");
            }

            double subtotal = vehiculo.getTarifaDiaria()*dias;

            double deposito = alquilerServicio.getDepositoGarantia();

            double total = subtotal + deposito;

            lblSubtotal.setText(String.format("₡%,.2f",subtotal));

            lblDeposito.setText(String.format("₡%,.2f",deposito));

            lblTotal.setText(String.format("₡%,.2f",total));
        }catch(NumberFormatException e){

            mostrarError("La cantidad de días debe de ser un numero entero.");
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }

    }

    /*
     * Qué hace: Registra definitivamente un nuevo alquiler utilizando el cliente, vehículo, fecha y cantidad de días seleccionados, y después limpia y actualiza la pantalla
     * Recibe: No recibe parámetros directamente, utiliza los datos ingresados y seleccionados en el formulario
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void confirmarAlquiler(){
        try {

            Cliente cliente = cmbCliente.getValue();

            Vehiculo vehiculo = cmbVehiculo.getValue();

            LocalDate fecha = dpFechaInicio.getValue();

            if (cliente == null) {
                throw new IllegalArgumentException("Debe seleccionar un cliente.");
            }

            if (vehiculo == null) {
                throw new IllegalArgumentException("Debe seleccionar un vehículo.");
            }

            int dias = Integer.parseInt(txtCantidadDias.getText().trim());

            Alquiler alquiler = alquilerServicio.crearAlquiler(cliente.getCedula(), vehiculo.getPlaca(), fecha, dias);

            mostrarInformacion("Alquiler #" + alquiler.getNumeroAlquiler() + " registrado correctamente.");

            limpiarFormulario();
            cargarDatos();

        }catch (NumberFormatException e){
            mostrarError("La cantidad de dias debe de ser un número entero.");
        }catch (IllegalArgumentException e){
            mostrarError(e.getMessage());
        }

    }

    /*
     * Qué hace: Limpia todos los campos y selecciones del formulario de alquiler, restablece la fecha actual y devuelve los valores mostrados a su estado inicial
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void limpiarFormulario(){

        cmbCliente.getSelectionModel().clearSelection();
        cmbVehiculo.getSelectionModel().clearSelection();

        dpFechaInicio.setValue(LocalDate.now());

        txtCantidadDias.clear();

        lblSubtotal.setText("₡0.00");

        if (alquilerServicio != null) {

            lblDeposito.setText(String.format("₡%,.2f", alquilerServicio.getDepositoGarantia()));
        }else {
            lblDeposito.setText("₡0.00");
        }

        lblTotal.setText("₡0.00");
    }

    /*
     * Qué hace: Muestra una ventana de alerta de tipo error para informar al usuario sobre un problema ocurrido durante alguna operación de alquiler
     * Recibe: El mensaje de error que se desea mostrar en pantalla
     * Retorna: No retorna ningún valor
     */
    private void mostrarError(String mensaje){
        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /*
     * Qué hace: Muestra una ventana informativa para comunicar al usuario que una operación relacionada con los alquileres se realizó correctamente
     * Recibe: El mensaje informativo que se desea mostrar en pantalla
     * Retorna: No retorna ningún valor
     */
    private void mostrarInformacion(String mensaje){
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

}
