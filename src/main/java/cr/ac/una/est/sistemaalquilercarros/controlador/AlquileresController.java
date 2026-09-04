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

    @FXML
    private void initialize() {

        dpFechaInicio.setValue(LocalDate.now());

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
    }

public void setServicios(ClienteServicio clienteServicio, VehiculoServicio vehiculoServicio, AlquilerServicio alquilerServicio) {
        this.clienteServicio = clienteServicio;
        this.vehiculoServicio = vehiculoServicio;
        this.alquilerServicio = alquilerServicio;

        cargarDatos();
}

    private void cargarDatos() {
        cmbCliente.setItems(FXCollections.observableArrayList(clienteServicio.obtenerTodos()));

        cmbVehiculo.setItems(FXCollections.observableArrayList(vehiculoServicio.obtenerDisponibles()));

        tablaAlquileres.setItems(FXCollections.observableArrayList(alquilerServicio.obtenerTodos()));

        lblDeposito.setText(String.format("₡%,.2f", alquilerServicio.getDepositoGarantia()));
    }

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

    private void mostrarError(String mensaje){
        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarInformacion(String mensaje){
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

}
