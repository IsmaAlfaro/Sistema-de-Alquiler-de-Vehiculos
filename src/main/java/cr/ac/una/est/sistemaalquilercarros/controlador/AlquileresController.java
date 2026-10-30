package cr.ac.una.est.sistemaalquilercarros.controlador;

import cr.ac.una.est.sistemaalquilercarros.modelo.Alquiler;
import cr.ac.una.est.sistemaalquilercarros.modelo.Cliente;
import cr.ac.una.est.sistemaalquilercarros.modelo.MetodoPago;
import cr.ac.una.est.sistemaalquilercarros.modelo.Vehiculo;
import cr.ac.una.est.sistemaalquilercarros.servicio.AlquilerServicio;
import cr.ac.una.est.sistemaalquilercarros.servicio.ClienteServicio;
import cr.ac.una.est.sistemaalquilercarros.servicio.VehiculoServicio;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class AlquileresController {

    @FXML
    private ComboBox<Cliente> cmbCliente;

    @FXML
    private ComboBox<Vehiculo> cmbVehiculo;

    @FXML
    private TextField txtBuscarCliente;

    @FXML
    private TextField txtBuscarVehiculo;

    @FXML
    private TextField txtBuscarAlquiler;

    @FXML
    private ComboBox<String> cmbFiltroEstado;

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
    private ComboBox<MetodoPago> cmbMetodoPago;

    @FXML
    private TextField txtMontoRecibido;

    @FXML
    private Label lblVuelto;

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

    private ObservableList<Cliente> listaClientes;

    private FilteredList<Cliente> clientesFiltrados;

    private ObservableList<Vehiculo> listaVehiculos;

    private FilteredList<Vehiculo> vehiculosFiltrados;

    private ObservableList<Alquiler> listaAlquileres;

    private FilteredList<Alquiler> alquileresFiltrados;

    /*
     * Qué hace: Inicializa la pantalla de alquileres configurando la fecha actual, los valores iniciales de los labels, las formas de pago disponibles, las columnas de la tabla y los eventos que actualizan automáticamente los filtros, la tarifa diaria y los datos del pago
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
        lblVuelto.setText("₡0.00");

        cmbMetodoPago.setItems(
                FXCollections.observableArrayList(
                        MetodoPago.values()
                )
        );

        txtMontoRecibido.setDisable(
                true
        );

        cmbFiltroEstado.setItems(
                FXCollections.observableArrayList(
                        "TODOS",
                        "ACTIVO",
                        "FINALIZADO"
                )
        );

        cmbFiltroEstado.setValue("TODOS");

        colNumero.setCellValueFactory(
                dato -> new ReadOnlyObjectWrapper<>(
                        dato.getValue().getNumeroAlquiler()
                )
        );

        colCliente.setCellValueFactory(
                dato -> new SimpleStringProperty(
                        dato.getValue()
                                .getCliente()
                                .getNombreCompleto()
                )
        );

        colVehiculo.setCellValueFactory(
                dato -> new SimpleStringProperty(
                        dato.getValue()
                                .getVehiculo()
                                .toString()
                )
        );

        colFechaInicio.setCellValueFactory(
                dato -> new ReadOnlyObjectWrapper<>(
                        dato.getValue().getFechaInicio()
                )
        );

        colFechaEsperada.setCellValueFactory(
                dato -> new ReadOnlyObjectWrapper<>(
                        dato.getValue()
                                .getFechaDevolucionEsperada()
                )
        );

        colDias.setCellValueFactory(
                dato -> new ReadOnlyObjectWrapper<>(
                        dato.getValue().getCantidadDias()
                )
        );

        colTotal.setCellValueFactory(
                dato -> new SimpleStringProperty(
                        String.format(
                                "₡%,.2f",
                                dato.getValue().calcularTotal()
                        )
                )
        );

        colEstado.setCellValueFactory(
                dato -> new SimpleStringProperty(
                        dato.getValue()
                                .getEstado()
                                .toString()
                )
        );

        cmbVehiculo.valueProperty().addListener(
                (observable, vehiculoAnterior, vehiculoNuevo) ->
                        actualizarTarifaDiaria()
        );

        cmbMetodoPago.valueProperty().addListener(
                (observable, anterior, nuevo) ->
                        actualizarFormaPago()
        );

        txtMontoRecibido.textProperty().addListener(
                (observable, anterior, nuevo) -> {

                    if (!nuevo.matches("\\d*")) {

                        txtMontoRecibido.setText(
                                nuevo.replaceAll(
                                        "[^\\d]",
                                        ""
                                )
                        );
                    }

                    calcularVuelto();
                }
        );

        txtBuscarCliente.textProperty().addListener(
                (observable, anterior, nuevo) ->
                        filtrarClientes(nuevo)
        );

        txtBuscarVehiculo.textProperty().addListener(
                (observable, anterior, nuevo) ->
                        filtrarVehiculos(nuevo)
        );

        txtBuscarAlquiler.textProperty().addListener(
                (observable, anterior, nuevo) ->
                        filtrarAlquileres()
        );

        cmbFiltroEstado.valueProperty().addListener(
                (observable, anterior, nuevo) ->
                        filtrarAlquileres()
        );
    }

    /*
     * Qué hace: Recibe y almacena los servicios necesarios para trabajar con clientes, vehículos y alquileres, y posteriormente carga en la pantalla la información disponible
     * Recibe: El servicio de clientes, el servicio de vehículos y el servicio de alquileres
     * Retorna: No retorna ningún valor
     */
    public void setServicios(
            ClienteServicio clienteServicio,
            VehiculoServicio vehiculoServicio,
            AlquilerServicio alquilerServicio) {

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

        listaClientes = FXCollections.observableArrayList(
                clienteServicio.obtenerTodos()
        );

        clientesFiltrados = new FilteredList<>(
                listaClientes,
                cliente -> true
        );

        cmbCliente.setItems(clientesFiltrados);

        listaVehiculos = FXCollections.observableArrayList(
                vehiculoServicio.obtenerDisponibles()
        );

        vehiculosFiltrados = new FilteredList<>(
                listaVehiculos,
                vehiculo -> true
        );

        cmbVehiculo.setItems(vehiculosFiltrados);

        listaAlquileres = FXCollections.observableArrayList(
                alquilerServicio.obtenerTodos()
        );

        alquileresFiltrados = new FilteredList<>(
                listaAlquileres,
                alquiler -> true
        );

        tablaAlquileres.setItems(
                alquileresFiltrados
        );

        lblDeposito.setText(
                String.format(
                        "₡%,.2f",
                        alquilerServicio.getDepositoGarantia()
                )
        );

        filtrarClientes(
                txtBuscarCliente.getText()
        );

        filtrarVehiculos(
                txtBuscarVehiculo.getText()
        );

        filtrarAlquileres();
    }

    /*
     * Qué hace: Filtra los clientes disponibles utilizando la cédula o el nombre escrito en el buscador
     * Recibe: El texto utilizado para buscar clientes
     * Retorna: No retorna ningún valor
     */
    private void filtrarClientes(String textoBusqueda) {

        if (clientesFiltrados == null) {
            return;
        }

        clientesFiltrados.setPredicate(cliente -> {

            if (textoBusqueda == null
                    || textoBusqueda.trim().isEmpty()) {

                return true;
            }

            String busqueda =
                    textoBusqueda
                            .toLowerCase()
                            .trim();

            return cliente.getCedula()
                    .toLowerCase()
                    .contains(busqueda)

                    || cliente.getNombreCompleto()
                    .toLowerCase()
                    .contains(busqueda);
        });

        cmbCliente.getSelectionModel()
                .clearSelection();
    }

    /*
     * Qué hace: Filtra los vehículos disponibles utilizando la placa, marca o modelo escrito en el buscador
     * Recibe: El texto utilizado para buscar vehículos
     * Retorna: No retorna ningún valor
     */
    private void filtrarVehiculos(String textoBusqueda) {

        if (vehiculosFiltrados == null) {
            return;
        }

        vehiculosFiltrados.setPredicate(vehiculo -> {

            if (textoBusqueda == null
                    || textoBusqueda.trim().isEmpty()) {

                return true;
            }

            String busqueda =
                    textoBusqueda
                            .toLowerCase()
                            .trim();

            return vehiculo.getPlaca()
                    .toLowerCase()
                    .contains(busqueda)

                    || vehiculo.getMarca()
                    .toLowerCase()
                    .contains(busqueda)

                    || vehiculo.getModelo()
                    .toLowerCase()
                    .contains(busqueda);
        });

        cmbVehiculo.getSelectionModel()
                .clearSelection();

        actualizarTarifaDiaria();
    }

    /*
     * Qué hace: Filtra los alquileres registrados permitiendo buscar de forma exacta por número de alquiler y de forma general por número, cliente, cédula, placa, marca o modelo, además permite filtrar según el estado del alquiler
     * Recibe: No recibe parámetros directamente, utiliza el buscador general y el estado seleccionado
     * Retorna: No retorna ningún valor
     */
    private void filtrarAlquileres() {

        if (alquileresFiltrados == null) {
            return;
        }

        String textoBusqueda =
                txtBuscarAlquiler.getText();

        String estadoSeleccionado =
                cmbFiltroEstado.getValue();

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

            boolean coincideBusqueda =
                    true;

            boolean coincideEstado =
                    true;

            if (!busqueda.isEmpty()) {

                if (busquedaPorNumero) {

                    coincideBusqueda =
                            numeroExacto != null
                                    && alquiler.getNumeroAlquiler()
                                    == numeroExacto;

                } else {

                    coincideBusqueda =
                            String.valueOf(
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
                }
            }

            if (estadoSeleccionado != null
                    && !estadoSeleccionado.equals(
                    "TODOS"
            )) {

                coincideEstado =
                        alquiler.getEstado()
                                .toString()
                                .equals(
                                        estadoSeleccionado
                                );
            }

            return coincideBusqueda
                    && coincideEstado;
        });
    }

    /*
     * Qué hace: Actualiza el label que muestra la tarifa diaria del vehículo seleccionado para que el usuario pueda conocer su precio antes de realizar el cálculo del alquiler
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    private void actualizarTarifaDiaria() {

        Vehiculo vehiculo =
                cmbVehiculo.getValue();

        if (vehiculo == null) {

            lblTarifaDiaria.setText(
                    "₡0.00"
            );

            return;
        }

        lblTarifaDiaria.setText(
                String.format(
                        "₡%,.2f",
                        vehiculo.getTarifaDiaria()
                )
        );
    }

    /*
     * Qué hace: Calcula de manera previa el subtotal, el depósito de garantía y el monto total del alquiler utilizando el vehículo seleccionado y la cantidad de días indicada por el usuario
     * Recibe: No recibe parámetros directamente, utiliza los datos ingresados y seleccionados en el formulario
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void calcularAlquiler() {

        try {

            Vehiculo vehiculo =
                    cmbVehiculo.getValue();

            if (vehiculo == null) {

                throw new IllegalArgumentException(
                        "Debe seleccionar un vehículo."
                );
            }

            int dias =
                    Integer.parseInt(
                            txtCantidadDias
                                    .getText()
                                    .trim()
                    );

            if (dias <= 0) {

                throw new IllegalArgumentException(
                        "La cantidad de días debe ser mayor a cero."
                );
            }

            double subtotal =
                    vehiculo.getTarifaDiaria()
                            * dias;

            double deposito =
                    alquilerServicio
                            .getDepositoGarantia();

            double total =
                    subtotal + deposito;

            lblSubtotal.setText(
                    String.format(
                            "₡%,.2f",
                            subtotal
                    )
            );

            lblDeposito.setText(
                    String.format(
                            "₡%,.2f",
                            deposito
                    )
            );

            lblTotal.setText(
                    String.format(
                            "₡%,.2f",
                            total
                    )
            );

            calcularVuelto();

        } catch (NumberFormatException e) {

            mostrarError(
                    "La cantidad de días debe de ser un numero entero."
            );

        } catch (IllegalArgumentException e) {

            mostrarError(
                    e.getMessage()
            );
        }
    }

    /*
     * Qué hace: Ajusta los controles relacionados con el pago dependiendo del método seleccionado, permitiendo ingresar el monto recibido únicamente cuando el pago se realiza en efectivo
     * Recibe: No recibe parámetros directamente, utiliza el método de pago seleccionado
     * Retorna: No retorna ningún valor
     */
    private void actualizarFormaPago() {

        MetodoPago metodoPago =
                cmbMetodoPago.getValue();

        boolean efectivo =
                metodoPago == MetodoPago.EFECTIVO;

        txtMontoRecibido.setDisable(
                !efectivo
        );

        if (!efectivo) {

            txtMontoRecibido.clear();

            lblVuelto.setText(
                    "₡0.00"
            );

            return;
        }

        calcularVuelto();
    }

    /*
     * Qué hace: Calcula visualmente el vuelto correspondiente utilizando el total actual del alquiler y el monto recibido cuando el método de pago seleccionado es efectivo
     * Recibe: No recibe parámetros directamente, utiliza el vehículo, la cantidad de días y el monto recibido
     * Retorna: No retorna ningún valor
     */
    private void calcularVuelto() {

        if (cmbMetodoPago.getValue()
                != MetodoPago.EFECTIVO) {

            lblVuelto.setText(
                    "₡0.00"
            );

            return;
        }

        Vehiculo vehiculo =
                cmbVehiculo.getValue();

        if (vehiculo == null
                || txtCantidadDias.getText().trim().isEmpty()
                || txtMontoRecibido.getText().trim().isEmpty()) {

            lblVuelto.setText(
                    "₡0.00"
            );

            return;
        }

        try {

            int dias =
                    Integer.parseInt(
                            txtCantidadDias
                                    .getText()
                                    .trim()
                    );

            double total =
                    vehiculo.getTarifaDiaria()
                            * dias
                            + alquilerServicio
                            .getDepositoGarantia();

            double recibido =
                    Double.parseDouble(
                            txtMontoRecibido
                                    .getText()
                                    .trim()
                    );

            double vuelto =
                    Math.max(
                            0,
                            recibido - total
                    );

            lblVuelto.setText(
                    String.format(
                            "₡%,.2f",
                            vuelto
                    )
            );

        } catch (NumberFormatException e) {

            lblVuelto.setText(
                    "₡0.00"
            );
        }
    }

    /*
     * Qué hace: Registra definitivamente un nuevo alquiler utilizando el cliente, vehículo, fecha, cantidad de días y forma de pago seleccionados, valida el pago correspondiente y después limpia y actualiza la pantalla
     * Recibe: No recibe parámetros directamente, utiliza los datos ingresados y seleccionados en el formulario
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void confirmarAlquiler() {

        try {

            Cliente cliente =
                    cmbCliente.getValue();

            Vehiculo vehiculo =
                    cmbVehiculo.getValue();

            LocalDate fecha =
                    dpFechaInicio.getValue();

            if (cliente == null) {

                throw new IllegalArgumentException(
                        "Debe seleccionar un cliente."
                );
            }

            if (vehiculo == null) {

                throw new IllegalArgumentException(
                        "Debe seleccionar un vehículo."
                );
            }

            int dias =
                    Integer.parseInt(
                            txtCantidadDias
                                    .getText()
                                    .trim()
                    );

            MetodoPago metodoPago =
                    cmbMetodoPago.getValue();

            if (metodoPago == null) {

                throw new IllegalArgumentException(
                        "Debe seleccionar una forma de pago."
                );
            }

            double total =
                    vehiculo.getTarifaDiaria()
                            * dias
                            + alquilerServicio
                            .getDepositoGarantia();

            Double montoRecibido =
                    null;

            if (metodoPago
                    == MetodoPago.EFECTIVO) {

                String textoMonto =
                        txtMontoRecibido
                                .getText()
                                .trim();

                if (textoMonto.isEmpty()) {

                    throw new IllegalArgumentException(
                            "Debe ingresar el monto recibido en efectivo."
                    );
                }

                montoRecibido =
                        Double.parseDouble(
                                textoMonto
                        );

                if (montoRecibido % 10 != 0) {

                    throw new IllegalArgumentException(
                            "El monto recibido debe ser múltiplo de ₡10."
                    );
                }

                if (montoRecibido < total) {

                    throw new IllegalArgumentException(
                            "El monto recibido es menor al total del alquiler."
                    );
                }
            }

            Alquiler alquiler = alquilerServicio.crearAlquiler(cliente.getCedula(), vehiculo.getPlaca(), fecha, dias, metodoPago, montoRecibido);

            String informacionPago =
                    "\nForma de pago: "
                            + metodoPago;

            if (metodoPago
                    == MetodoPago.EFECTIVO) {

                double vuelto =
                        montoRecibido - total;

                informacionPago +=
                        "\nMonto recibido: "
                                + String.format(
                                "₡%,.2f",
                                montoRecibido
                        )

                                + "\nVuelto: "
                                + String.format(
                                "₡%,.2f",
                                vuelto
                        );
            }

            mostrarInformacion(
                    "Alquiler #"
                            + alquiler.getNumeroAlquiler()
                            + " registrado correctamente.\n\n"
                            + "Total pagado: "
                            + String.format(
                            "₡%,.2f",
                            total
                    )
                            + informacionPago
            );

            limpiarFormulario();

            cargarDatos();

        } catch (NumberFormatException e) {

            mostrarError(
                    "La cantidad de dias y el monto recibido deben ser números válidos."
            );

        } catch (IllegalArgumentException e) {

            mostrarError(
                    e.getMessage()
            );

        } catch (RuntimeException e) {

            mostrarError(
                    e.getMessage()
            );
        }
    }

    /*
     * Qué hace: Limpia todos los campos y selecciones del formulario de alquiler, restablece la fecha actual, limpia la información del pago y devuelve los valores mostrados a su estado inicial
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void limpiarFormulario() {

        cmbCliente.getSelectionModel()
                .clearSelection();

        cmbVehiculo.getSelectionModel()
                .clearSelection();

        txtBuscarCliente.clear();

        txtBuscarVehiculo.clear();

        dpFechaInicio.setValue(
                LocalDate.now()
        );

        txtCantidadDias.clear();

        cmbMetodoPago
                .getSelectionModel()
                .clearSelection();

        txtMontoRecibido.clear();

        txtMontoRecibido.setDisable(
                true
        );

        lblVuelto.setText(
                "₡0.00"
        );

        lblTarifaDiaria.setText(
                "₡0.00"
        );

        lblSubtotal.setText(
                "₡0.00"
        );

        if (alquilerServicio != null) {

            lblDeposito.setText(
                    String.format(
                            "₡%,.2f",
                            alquilerServicio
                                    .getDepositoGarantia()
                    )
            );

        } else {

            lblDeposito.setText(
                    "₡0.00"
            );
        }

        lblTotal.setText(
                "₡0.00"
        );
    }

    /*
     * Qué hace: Muestra una ventana de alerta de tipo error para informar al usuario sobre un problema ocurrido durante alguna operación de alquiler
     * Recibe: El mensaje de error que se desea mostrar en pantalla
     * Retorna: No retorna ningún valor
     */
    private void mostrarError(String mensaje) {

        Alert alerta =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alerta.setTitle(
                "Error"
        );

        alerta.setHeaderText(
                null
        );

        alerta.setContentText(
                mensaje
        );

        alerta.showAndWait();
    }

    /*
     * Qué hace: Muestra una ventana informativa para comunicar al usuario que una operación relacionada con los alquileres se realizó correctamente
     * Recibe: El mensaje informativo que se desea mostrar en pantalla
     * Retorna: No retorna ningún valor
     */
    private void mostrarInformacion(
            String mensaje) {

        Alert alerta =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alerta.setTitle(
                "Información"
        );

        alerta.setHeaderText(
                null
        );

        alerta.setContentText(
                mensaje
        );

        alerta.showAndWait();
    }
}