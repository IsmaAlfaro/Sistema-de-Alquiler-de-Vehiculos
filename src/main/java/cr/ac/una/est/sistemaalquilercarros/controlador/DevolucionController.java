package cr.ac.una.est.sistemaalquilercarros.controlador;

import cr.ac.una.est.sistemaalquilercarros.modelo.Alquiler;
import cr.ac.una.est.sistemaalquilercarros.modelo.MetodoPago;
import cr.ac.una.est.sistemaalquilercarros.modelo.ResultadoDevolucion;
import cr.ac.una.est.sistemaalquilercarros.servicio.AlquilerServicio;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class DevolucionController {

    @FXML
    private TextField txtBuscarDevolucion;

    @FXML
    private ComboBox<Alquiler> cmbAlquiler;

    @FXML
    private DatePicker dpFechaDevolucion;

    @FXML
    private Label lblCliente;

    @FXML
    private Label lblVehiculo;

    @FXML
    private Label lblFechaEsperada;

    @FXML
    private Label lblDiasAtraso;

    @FXML
    private Label lblMulta;

    @FXML
    private Label lblDepositoDevuelto;

    @FXML
    private Label lblSaldoPendiente;

    @FXML
    private ComboBox<MetodoPago> cmbMetodoPago;

    @FXML
    private TextField txtMontoRecibido;

    @FXML
    private Label lblVuelto;

    @FXML
    private TableView<Alquiler> tablaAlquileresActivos;

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
    private TableColumn<Alquiler, String> colEstado;

    private AlquilerServicio alquilerServicio;

    private ObservableList<Alquiler> listaAlquileresActivos;

    private FilteredList<Alquiler> alquileresFiltrados;

    /*
     * Qué hace: Inicializa la pantalla de devoluciones configurando la fecha actual, las columnas de la tabla, las formas de pago disponibles y los eventos que permiten seleccionar y buscar alquileres activos
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void initialize() {

        dpFechaDevolucion.setValue(LocalDate.now());

        cmbMetodoPago.setItems(FXCollections.observableArrayList(MetodoPago.values()));

        cmbMetodoPago.setDisable(true);

        txtMontoRecibido.setDisable(true);

        colNumero.setCellValueFactory(dato -> new ReadOnlyObjectWrapper<>(dato.getValue().getNumeroAlquiler()));

        colCliente.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getCliente().getNombreCompleto()));

        colVehiculo.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getVehiculo().toString()));

        colFechaInicio.setCellValueFactory(dato -> new ReadOnlyObjectWrapper<>(dato.getValue().getFechaInicio()));

        colFechaEsperada.setCellValueFactory(dato -> new ReadOnlyObjectWrapper<>(dato.getValue().getFechaDevolucionEsperada()));

        colEstado.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getEstado().toString()));

        cmbAlquiler.setOnAction(evento -> cargarAlquilerSeleccionado());

        cmbMetodoPago.setOnAction(evento -> actualizarFormaPago());

        tablaAlquileresActivos.setOnMouseClicked(evento -> cargarDesdeTabla());

        txtBuscarDevolucion.textProperty().addListener((observable, anterior, nuevo) -> filtrarAlquileres());

        txtMontoRecibido.textProperty().addListener((observable, anterior, nuevo) -> {

            if (!nuevo.matches("\\d*")) {
                txtMontoRecibido.setText(nuevo.replaceAll("[^\\d]", ""));
            }

            calcularVuelto();

        });
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

        listaAlquileresActivos = FXCollections.observableArrayList(alquilerServicio.obtenerActivos());

        alquileresFiltrados = new FilteredList<>(listaAlquileresActivos, alquiler -> true);

        cmbAlquiler.setItems(alquileresFiltrados);

        tablaAlquileresActivos.setItems(alquileresFiltrados);

        filtrarAlquileres();
    }

    /*
     * Qué hace: Filtra los alquileres activos permitiendo buscar de forma exacta por número de alquiler y de forma general por número, nombre o cédula del cliente y placa, marca o modelo del vehículo
     * Recibe: No recibe parámetros directamente, utiliza el texto ingresado en el buscador
     * Retorna: No retorna ningún valor
     */
    private void filtrarAlquileres() {

        if (alquileresFiltrados == null) {
            return;
        }

        String textoBusqueda =
                txtBuscarDevolucion.getText();

        String busqueda =
                textoBusqueda == null
                        ? ""
                        : textoBusqueda
                        .toLowerCase()
                        .trim();

        boolean busquedaPorNumero =
                busqueda.startsWith("#");

        Integer numeroAlquiler =
                null;

        if (busquedaPorNumero) {

            String textoNumero =
                    busqueda
                            .substring(1)
                            .trim();

            if (textoNumero.matches("\\d+")) {

                try {

                    numeroAlquiler =
                            Integer.parseInt(
                                    textoNumero
                            );

                } catch (NumberFormatException e) {

                    numeroAlquiler =
                            null;
                }
            }
        }

        final Integer numeroExacto =
                numeroAlquiler;

        alquileresFiltrados.setPredicate(alquiler -> {

            if (busqueda.isEmpty()) {

                return true;
            }

            if (busquedaPorNumero) {

                return numeroExacto != null
                        && alquiler.getNumeroAlquiler()
                        == numeroExacto;
            }

            return String.valueOf(
                    alquiler.getNumeroAlquiler()
            ).contains(busqueda)

                    || alquiler.getCliente()
                    .getCedula()
                    .toLowerCase()
                    .contains(busqueda)

                    || alquiler.getCliente()
                    .getNombreCompleto()
                    .toLowerCase()
                    .contains(busqueda)

                    || alquiler.getVehiculo()
                    .getPlaca()
                    .toLowerCase()
                    .contains(busqueda)

                    || alquiler.getVehiculo()
                    .getMarca()
                    .toLowerCase()
                    .contains(busqueda)

                    || alquiler.getVehiculo()
                    .getModelo()
                    .toLowerCase()
                    .contains(busqueda);
        });

        cmbAlquiler.getSelectionModel()
                .clearSelection();

        tablaAlquileresActivos
                .getSelectionModel()
                .clearSelection();

        lblCliente.setText("-");
        lblVehiculo.setText("-");
        lblFechaEsperada.setText("-");

        reiniciarResultados();
        reiniciarPago();
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
        reiniciarPago();
    }

    /*
     * Qué hace: Calcula de forma previa los días de atraso, la multa, el depósito que debe devolverse y cualquier saldo pendiente utilizando la fecha de devolución seleccionada, además habilita la forma de pago únicamente cuando existe un saldo adicional
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

            if (saldo > 0) {

                cmbMetodoPago.setDisable(false);

            } else {

                reiniciarPago();
            }

            actualizarFormaPago();

        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    /*
     * Qué hace: Ajusta los controles de pago dependiendo de la forma seleccionada y del saldo pendiente, permitiendo ingresar un monto únicamente cuando existe un saldo adicional y el pago se realiza en efectivo
     * Recibe: No recibe parámetros directamente, utiliza la forma de pago seleccionada y el saldo pendiente calculado
     * Retorna: No retorna ningún valor
     */
    private void actualizarFormaPago() {

        Alquiler alquiler = cmbAlquiler.getValue();

        LocalDate fecha = dpFechaDevolucion.getValue();

        if (alquiler == null || fecha == null) {

            cmbMetodoPago.setDisable(true);

            txtMontoRecibido.setDisable(true);

            lblVuelto.setText("₡0.00");

            return;
        }

        double saldo = alquiler.calcularSaldoPendiente(fecha);

        if (saldo <= 0) {

            cmbMetodoPago.setDisable(true);

            txtMontoRecibido.setDisable(true);

            cmbMetodoPago.getSelectionModel().clearSelection();

            txtMontoRecibido.clear();

            lblVuelto.setText("₡0.00");

            return;
        }

        cmbMetodoPago.setDisable(false);

        MetodoPago metodoPago = cmbMetodoPago.getValue();

        boolean esEfectivo = metodoPago == MetodoPago.EFECTIVO;

        txtMontoRecibido.setDisable(!esEfectivo);

        if (!esEfectivo) {

            txtMontoRecibido.clear();

            lblVuelto.setText("₡0.00");

            return;
        }

        calcularVuelto();
    }

    /*
     * Qué hace: Calcula de forma visual el vuelto correspondiente cuando existe un saldo pendiente y el pago se realiza en efectivo utilizando el monto recibido
     * Recibe: No recibe parámetros directamente, utiliza el alquiler, la fecha de devolución y el monto ingresado
     * Retorna: No retorna ningún valor
     */
    private void calcularVuelto() {

        if (cmbMetodoPago.getValue() != MetodoPago.EFECTIVO) {

            lblVuelto.setText("₡0.00");

            return;

        }

        Alquiler alquiler = cmbAlquiler.getValue();

        LocalDate fecha = dpFechaDevolucion.getValue();

        if (alquiler == null || fecha == null || txtMontoRecibido.getText().trim().isEmpty()) {

            lblVuelto.setText("₡0.00");

            return;

        }

        try {

            double saldo = alquiler.calcularSaldoPendiente(fecha);

            double recibido = Double.parseDouble(txtMontoRecibido.getText().trim());

            double vuelto = Math.max(0, recibido - saldo);

            lblVuelto.setText(String.format("₡%,.2f", vuelto));

        } catch (NumberFormatException e) {
            lblVuelto.setText("₡0.00");
        }
    }

    /*
     * Qué hace: Registra definitivamente la devolución del alquiler seleccionado, solicita una forma de pago únicamente cuando existe un saldo pendiente, obtiene los resultados calculados por el servicio y muestra al usuario el resumen completo de la devolución
     * Recibe: No recibe parámetros directamente, utiliza el alquiler, la fecha y, cuando sea necesario, la forma de pago y el monto recibido seleccionados
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

            if (fecha == null) {
                throw new IllegalArgumentException("Debe seleccionar la fecha de devolución.");
            }

            if (fecha.isBefore(alquiler.getFechaInicio())) {
                throw new IllegalArgumentException("La fecha de devolución no puede ser anterior al inicio.");
            }

            double saldo = alquiler.calcularSaldoPendiente(fecha);

            MetodoPago metodoPago = null;

            Double montoRecibido = null;

            if (saldo > 0) {

                metodoPago = cmbMetodoPago.getValue();

                if (metodoPago == null) {
                    throw new IllegalArgumentException("Debe seleccionar una forma de pago para cancelar el saldo pendiente.");
                }

                if (metodoPago == MetodoPago.EFECTIVO) {

                    String textoMonto = txtMontoRecibido.getText().trim();

                    if (textoMonto.isEmpty()) {
                        throw new IllegalArgumentException("Debe ingresar el monto recibido en efectivo.");
                    }

                    try {

                        montoRecibido = Double.parseDouble(textoMonto);

                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("El monto recibido debe ser un número válido.");
                    }

                    if (montoRecibido % 10 != 0) {
                        throw new IllegalArgumentException("El monto recibido debe ser múltiplo de ₡10.");
                    }

                    if (montoRecibido < saldo) {
                        throw new IllegalArgumentException("El monto recibido es menor al saldo pendiente.");
                    }
                }
            }

            ResultadoDevolucion resultado = alquilerServicio.devolverAlquiler(alquiler.getNumeroAlquiler(), fecha, metodoPago, montoRecibido);

            String informacionPago = "";

            if (resultado.getSaldoPendiente() > 0) {

                informacionPago = "\nForma de pago del saldo: " + metodoPago;

                if (metodoPago == MetodoPago.EFECTIVO) {

                    double recibido = montoRecibido == null ? 0 : montoRecibido;

                    double vuelto = recibido - resultado.getSaldoPendiente();

                    informacionPago += "\nMonto recibido: " + String.format("₡%,.2f", recibido) + "\nVuelto: " + String.format("₡%,.2f", vuelto);

                }
            }

            mostrarInformacion("Devolución registrada correctamente.\n\n" + "Días de atraso: " + resultado.getDiasAtraso() + "\nMulta: " + String.format("₡%,.2f", resultado.getMulta()) + "\nDepósito devuelto: " + String.format("₡%,.2f", resultado.getDepositoDevuelto()) + "\nSaldo pendiente: " + String.format("₡%,.2f", resultado.getSaldoPendiente()) + informacionPago);

            limpiarFormulario();

            cargarDatos();

        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
    }

    /*
     * Qué hace: Limpia las selecciones y datos mostrados en la pantalla de devolución, restablece la fecha actual, limpia la información de pago y reinicia todos los resultados calculados
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void limpiarFormulario() {

        cmbAlquiler.getSelectionModel().clearSelection();

        tablaAlquileresActivos.getSelectionModel().clearSelection();

        txtBuscarDevolucion.clear();

        dpFechaDevolucion.setValue(LocalDate.now());

        lblCliente.setText("-");
        lblVehiculo.setText("-");
        lblFechaEsperada.setText("-");

        reiniciarResultados();
        reiniciarPago();
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
     * Qué hace: Restablece los controles relacionados con la forma de pago, el monto recibido y el vuelto y mantiene deshabilitado el pago hasta que exista un saldo pendiente
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    private void reiniciarPago() {

        cmbMetodoPago.getSelectionModel().clearSelection();

        cmbMetodoPago.setDisable(true);

        txtMontoRecibido.clear();

        txtMontoRecibido.setDisable(true);

        lblVuelto.setText("₡0.00");
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