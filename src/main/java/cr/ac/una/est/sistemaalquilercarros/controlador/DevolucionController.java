package cr.ac.una.est.sistemaalquilercarros.controlador;
import cr.ac.una.est.sistemaalquilercarros.modelo.Alquiler;
import cr.ac.una.est.sistemaalquilercarros.modelo.Alquiler;
import cr.ac.una.est.sistemaalquilercarros.modelo.ResultadoDevolucion;
import cr.ac.una.est.sistemaalquilercarros.servicio.AlquilerServicio;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

public class DevolucionController {
    @FXML
    private ComboBox<Alquiler>cmbAlquiler;
    @FXML
    private DatePicker dpFechaDevolucion;
    @FXML     private Label lblCliente;
    @FXML     private Label lblVehiculo;
    @FXML     private Label lblFechaEsperada;
    @FXML     private Label lblDiasAtraso;
    @FXML     private Label lblMulta;
    @FXML     private Label lblDepositoDevuelto;
    @FXML     private Label lblSaldoPendiente;
    @FXML     private TableView<Alquiler> tablaAlquileresActivos;
    @FXML     private TableColumn<Alquiler, Integer> colNumero;
    @FXML     private TableColumn<Alquiler, String> colCliente;
    @FXML     private TableColumn<Alquiler, String> colVehiculo;
    @FXML     private TableColumn<Alquiler, LocalDate> colFechaInicio;
    @FXML     private TableColumn<Alquiler, LocalDate> colFechaEsperada;
    @FXML     private TableColumn<Alquiler, String> colEstado;
    private AlquilerServicio alquilerServicio;
    @FXML     private void initialize() {
        dpFechaDevolucion.setValue(LocalDate.now());
        colNumero.setCellValueFactory(dato -> new ReadOnlyObjectWrapper<>(dato.getValue().getNumeroAlquiler()));
        colCliente.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getCliente().getNombreCompleto()));
        colVehiculo.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getVehiculo().toString()));
        colFechaInicio.setCellValueFactory(dato -> new ReadOnlyObjectWrapper<>(dato.getValue().getFechaInicio()));
        colFechaEsperada.setCellValueFactory(dato -> new ReadOnlyObjectWrapper<>(dato.getValue().getFechaDevolucionEsperada()));
        colEstado.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getEstado().toString()));
        cmbAlquiler.setOnAction(evento -> cargarAlquilerSeleccionado());          tablaAlquileresActivos.setOnMouseClicked(evento -> cargarDesdeTabla());
    }
    public void setAlquilerServicio(AlquilerServicio alquilerServicio) {
        this.alquilerServicio = alquilerServicio;
        cargarDatos();
    }
    private void cargarDatos() {
        var activos = alquilerServicio.obtenerActivos();
        cmbAlquiler.setItems(FXCollections.observableArrayList(activos));
        tablaAlquileresActivos.setItems(FXCollections.observableArrayList(activos));
    }
    private void cargarAlquilerSeleccionado() {
        Alquiler alquiler = cmbAlquiler.getValue();
        if (alquiler == null) {
            return;
        }
        mostrarDatosAlquiler(alquiler);
    }
    private void cargarDesdeTabla() {
        Alquiler alquiler = tablaAlquileresActivos.getSelectionModel().getSelectedItem();
        if (alquiler == null) {
            return;
        }
        cmbAlquiler.setValue(alquiler);
        mostrarDatosAlquiler(alquiler);
    }
    private void mostrarDatosAlquiler(Alquiler alquiler) {
        lblCliente.setText(alquiler.getCliente().getNombreCompleto());
        lblVehiculo.setText(alquiler.getVehiculo().toString());
        lblFechaEsperada.setText(alquiler.getFechaDevolucionEsperada().toString());
        reiniciarResultados();
    }
    @FXML
    private void calcularDevolucion() {
        try {
            Alquiler alquiler = cmbAlquiler.getValue();
            if (alquiler == null) {
                throw new IllegalArgumentException("Debe seleccionar un alquiler activo.");
            }
            LocalDate fecha = dpFechaDevolucion.getValue();
            if (fecha == null) {
                throw new IllegalArgumentException("Debe seleccionar la fecha de devolución.");
            }
            if (fecha.isBefore(alquiler.getFechaInicio())) {
                throw new IllegalArgumentException("La fecha de devolución no puede ser anterior al inicio.");
            }
            long diasAtraso = alquiler.calcularDiasAtraso(fecha);
            double multa = alquiler.calcularMulta(fecha);
            double deposito = alquiler.calcularDepositoADevolver(fecha);
            double saldo = alquiler.calcularSaldoPendiente(fecha);
            lblDiasAtraso.setText(String.valueOf(diasAtraso));
            lblMulta.setText(String.format("₡%,.2f", multa));
            lblDepositoDevuelto.setText(String.format("₡%,.2f", deposito));
            lblSaldoPendiente.setText(String.format("₡%,.2f", saldo));
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }
    @FXML
    private void confirmarDevolucion() {
        try {
            Alquiler alquiler = cmbAlquiler.getValue();
            if (alquiler == null) {
                throw new IllegalArgumentException("Debe seleccionar un alquiler activo.");
            }
            LocalDate fecha = dpFechaDevolucion.getValue();
            ResultadoDevolucion resultado = alquilerServicio.devolverAlquiler(alquiler.getNumeroAlquiler(), fecha);
            mostrarInformacion("Devolución registrada correctamente.\n\n" + "Días de atraso: " + resultado.getDiasAtraso() + "\nMulta: " + String.format("₡%,.2f", resultado.getMulta()) + "\nDepósito devuelto: " + String.format("₡%,.2f", resultado.getDepositoDevuelto()) + "\nSaldo pendiente: " + String.format("₡%,.2f", resultado.getSaldoPendiente()));

            limpiarFormulario();
            cargarDatos();
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }
    @FXML
    private void limpiarFormulario() {
        cmbAlquiler.getSelectionModel().clearSelection();
        tablaAlquileresActivos.getSelectionModel().clearSelection();
        dpFechaDevolucion.setValue(LocalDate.now());
        lblCliente.setText("-");
        lblVehiculo.setText("-");
        lblFechaEsperada.setText("-");
        reiniciarResultados();
    }
    private void reiniciarResultados() {
        lblDiasAtraso.setText("0");
        lblMulta.setText("₡0.00");
        lblDepositoDevuelto.setText("₡0.00");
        lblSaldoPendiente.setText("₡0.00");
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


