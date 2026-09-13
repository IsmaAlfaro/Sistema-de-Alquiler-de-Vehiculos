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

    /*
     * Qué hace: Inicializa la pantalla de devoluciones configurando la fecha actual, las columnas de la tabla y los eventos que permiten seleccionar alquileres tanto desde el ComboBox como desde la tabla
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
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

    /*
     * Qué hace: Asigna el servicio encargado de administrar los alquileres y carga inmediatamente los alquileres activos disponibles para devolución
     * Recibe: El servicio de alquileres que utilizará el controlador
     * Retorna: No retorna ningún valor
     */
    public void setAlquilerServicio(AlquilerServicio alquilerServicio) {
        this.alquilerServicio = alquilerServicio;
        cargarDatos();
    }

    /*
     * Qué hace: Obtiene todos los alquileres activos y los carga tanto en el ComboBox como en la tabla para que puedan ser seleccionados por el usuario
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    private void cargarDatos() {
        var activos = alquilerServicio.obtenerActivos();
        cmbAlquiler.setItems(FXCollections.observableArrayList(activos));
        tablaAlquileresActivos.setItems(FXCollections.observableArrayList(activos));
    }

    /*
     * Qué hace: Obtiene el alquiler seleccionado en el ComboBox y muestra sus datos principales en la pantalla de devolución
     * Recibe: No recibe parámetros directamente, utiliza el alquiler seleccionado en el ComboBox
     * Retorna: No retorna ningún valor
     */
    private void cargarAlquilerSeleccionado() {
        Alquiler alquiler = cmbAlquiler.getValue();
        if (alquiler == null) {
            return;
        }
        mostrarDatosAlquiler(alquiler);
    }

    /*
     * Qué hace: Obtiene el alquiler seleccionado directamente desde la tabla, lo coloca también en el ComboBox y muestra sus datos correspondientes
     * Recibe: No recibe parámetros directamente, utiliza la selección actual de la tabla
     * Retorna: No retorna ningún valor
     */
    private void cargarDesdeTabla() {
        Alquiler alquiler = tablaAlquileresActivos.getSelectionModel().getSelectedItem();
        if (alquiler == null) {
            return;
        }
        cmbAlquiler.setValue(alquiler);
        mostrarDatosAlquiler(alquiler);
    }

    /*
     * Qué hace: Muestra en los labels de la pantalla la información principal del alquiler seleccionado y reinicia los resultados de los cálculos de devolución
     * Recibe: El alquiler cuya información se desea mostrar
     * Retorna: No retorna ningún valor
     */
    private void mostrarDatosAlquiler(Alquiler alquiler) {
        lblCliente.setText(alquiler.getCliente().getNombreCompleto());
        lblVehiculo.setText(alquiler.getVehiculo().toString());
        lblFechaEsperada.setText(alquiler.getFechaDevolucionEsperada().toString());
        reiniciarResultados();
    }

    /*
     * Qué hace: Calcula de forma previa los días de atraso, la multa, el depósito que debe devolverse y cualquier saldo pendiente utilizando la fecha de devolución seleccionada
     * Recibe: No recibe parámetros directamente, utiliza el alquiler y la fecha seleccionados en la pantalla
     * Retorna: No retorna ningún valor
     */
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

    /*
     * Qué hace: Registra definitivamente la devolución del alquiler seleccionado, obtiene los resultados calculados por el servicio y muestra al usuario el resumen completo de la devolución
     * Recibe: No recibe parámetros directamente, utiliza el alquiler y la fecha seleccionados
     * Retorna: No retorna ningún valor
     */
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

    /*
     * Qué hace: Limpia las selecciones y datos mostrados en la pantalla de devolución, restablece la fecha actual y reinicia todos los resultados calculados
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
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

    /*
     * Qué hace: Restablece los valores visuales correspondientes a atraso, multa, depósito devuelto y saldo pendiente a sus valores iniciales
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    private void reiniciarResultados() {
        lblDiasAtraso.setText("0");
        lblMulta.setText("₡0.00");
        lblDepositoDevuelto.setText("₡0.00");
        lblSaldoPendiente.setText("₡0.00");
    }

    /*
     * Qué hace: Muestra una ventana de alerta de tipo error para informar al usuario sobre un problema ocurrido durante el proceso de devolución
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
     * Qué hace: Muestra una ventana informativa para comunicar al usuario el resultado o la confirmación de una operación de devolución
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


