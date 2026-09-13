package cr.ac.una.est.sistemaalquilercarros.controlador;

import cr.ac.una.est.sistemaalquilercarros.modelo.Vehiculo;
import cr.ac.una.est.sistemaalquilercarros.servicio.VehiculoServicio;
import cr.ac.una.est.sistemaalquilercarros.modelo.TipoVehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.CategoriaVehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.VehiculoPasajeros;
import cr.ac.una.est.sistemaalquilercarros.modelo.VehiculoCarga;
import cr.ac.una.est.sistemaalquilercarros.servicio.TipoVehiculoServicio;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class VehiculosController {

    @FXML
    private TextField txtPlaca;

    @FXML
    private TextField txtMarca;

    @FXML
    private TextField txtModelo;

    @FXML
    private TextField txtAnio;

    @FXML
    private TextField txtTarifa;

    @FXML
    private TextField txtPasajeros;

    @FXML
    private TextField txtCapacidadCarga;

    @FXML
    private ComboBox<TipoVehiculo> cmbTipo;

    @FXML
    private ComboBox<String> cmbTraccion;

    @FXML
    private TableView<Vehiculo> tablaVehiculos;

    @FXML
    private TableColumn<Vehiculo, String> colPlaca;

    @FXML
    private TableColumn<Vehiculo, String> colMarca;

    @FXML
    private TableColumn<Vehiculo, String> colModelo;

    @FXML
    private TableColumn<Vehiculo, Integer> colAnio;

    @FXML
    private TableColumn<Vehiculo, String> colTipo;

    @FXML
    private TableColumn<Vehiculo, String> colTarifa;

    @FXML
    private TableColumn<Vehiculo, String> colEstado;

    private VehiculoServicio vehiculoServicio;

    private TipoVehiculoServicio tipoVehiculoServicio;

    /*
     * Qué hace: Inicializa la pantalla de vehículos configurando las opciones de tracción, las columnas de la tabla y los eventos que permiten actualizar los campos según el tipo de vehículo y cargar los datos del vehículo seleccionado
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void initialize() {

        cmbTraccion.setItems(FXCollections.observableArrayList("Delantera", "Trasera", "AWD", "4x4"));

        colPlaca.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getPlaca()));

        colMarca.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getMarca()));

        colModelo.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getModelo()));

        colAnio.setCellValueFactory(dato -> new ReadOnlyObjectWrapper<>(dato.getValue().getAnio()));

        colTipo.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getTipoVehiculo().getNombre()));

        colTarifa.setCellValueFactory(dato -> new SimpleStringProperty(String.format("₡%,.2f", dato.getValue().getTarifaDiaria())));

        colEstado.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getEstado().toString()));

        cmbTipo.setOnAction(evento -> actualizarCamposPorTipo());

        tablaVehiculos.setOnMouseClicked(evento -> cargarVehiculoSeleccionado());

        actualizarCamposPorTipo();
    }

    /*
     * Qué hace: Asigna los servicios necesarios para administrar vehículos y tipos de vehículo, y posteriormente carga los tipos disponibles y actualiza la tabla principal
     * Recibe: El servicio de vehículos y el servicio de tipos de vehículo
     * Retorna: No retorna ningún valor
     */
    public void setServicios(VehiculoServicio vehiculoServicio, TipoVehiculoServicio tipoVehiculoServicio) {

        this.vehiculoServicio = vehiculoServicio;
        this.tipoVehiculoServicio = tipoVehiculoServicio;

        cargarTipos();
        actualizarTabla();
    }

    /*
     * Qué hace: Obtiene todos los tipos de vehículo registrados en el sistema y los carga en el ComboBox utilizado para seleccionar el tipo al registrar o modificar un vehículo
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    private void cargarTipos() {

        cmbTipo.setItems(
                FXCollections.observableArrayList(
                        tipoVehiculoServicio.obtenerTodos()
                )
        );
    }

    /*
     * Qué hace: Crea un nuevo vehículo utilizando los datos ingresados en el formulario, solicita su registro al servicio correspondiente y luego actualiza la tabla y limpia la pantalla
     * Recibe: No recibe parámetros directamente, utiliza los valores ingresados y seleccionados en el formulario
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void registrarVehiculo() {

        try {

            Vehiculo vehiculo = crearVehiculoDesdeFormulario();

            vehiculoServicio.registrarVehiculo(vehiculo);

            actualizarTabla();
            limpiarFormulario();

            mostrarInformacion("Vehículo registrado correctamente.");

        } catch (NumberFormatException e) {
            mostrarError("Año, tarifa, pasajeros y capacidad deben contener valores numéricos válidos.");
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    /*
     * Qué hace: Modifica el vehículo seleccionado en la tabla utilizando los datos actuales del formulario, conservando la placa como identificador principal y validando la información antes de guardar los cambios
     * Recibe: No recibe parámetros directamente, utiliza el vehículo seleccionado y los datos ingresados en el formulario
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void modificarVehiculo() {

        Vehiculo seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarError("Debe seleccionar un vehículo.");
            return;
        }

        try {

            Vehiculo modificado = crearVehiculoDesdeFormulario();

            vehiculoServicio.modificarVehiculo(seleccionado.getPlaca(), modificado
            );

            actualizarTabla();
            limpiarFormulario();

            mostrarInformacion("Vehículo modificado correctamente.");

        } catch (NumberFormatException e) {

            mostrarError("Los campos numéricos contienen valores inválidos.");

        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    /*
     * Qué hace: Elimina del sistema el vehículo seleccionado en la tabla después de comprobar que exista una selección válida y que el servicio permita realizar la eliminación
     * Recibe: No recibe parámetros directamente, utiliza el vehículo seleccionado en la tabla
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void eliminarVehiculo() {

        Vehiculo seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {

            mostrarError("Debe seleccionar un vehículo.");

            return;
        }

        try {

            vehiculoServicio.eliminarVehiculo(seleccionado.getPlaca());

            actualizarTabla();
            limpiarFormulario();

            mostrarInformacion("Vehículo eliminado correctamente.");

        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    /*
     * Qué hace: Limpia todos los campos y selecciones del formulario de vehículos, vuelve a habilitar los controles correspondientes y restablece el estado visual según el tipo seleccionado
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void limpiarFormulario() {

        txtPlaca.clear();
        txtMarca.clear();
        txtModelo.clear();
        txtAnio.clear();
        txtTarifa.clear();
        txtPasajeros.clear();
        txtCapacidadCarga.clear();

        cmbTipo.getSelectionModel().clearSelection();
        cmbTraccion.getSelectionModel().clearSelection();

        tablaVehiculos.getSelectionModel().clearSelection();

        txtPlaca.setDisable(false);
        cmbTipo.setDisable(false);

        actualizarCamposPorTipo();
    }

    /*
     * Qué hace: Construye un objeto de tipo VehiculoPasajeros o VehiculoCarga utilizando los datos del formulario y la categoría del tipo de vehículo seleccionado
     * Recibe: No recibe parámetros directamente, obtiene los datos desde los campos y ComboBox de la pantalla
     * Retorna: Un objeto Vehiculo creado con la información correspondiente al tipo seleccionado
     */
    private Vehiculo crearVehiculoDesdeFormulario() {

        String placa = txtPlaca.getText().trim();
        String marca = txtMarca.getText().trim();
        String modelo = txtModelo.getText().trim();

        int anio = Integer.parseInt(txtAnio.getText().trim());

        double tarifa = Double.parseDouble(txtTarifa.getText().trim());

        TipoVehiculo tipo = cmbTipo.getValue();

        if (tipo == null) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de vehículo.");
        }

        String traccion = cmbTraccion.getValue();

        if (tipo.getCategoria() == CategoriaVehiculo.PASAJEROS) {

            int pasajeros = 0;

            if (tipo.isRequierePasajeros()) {
                pasajeros = Integer.parseInt(txtPasajeros.getText().trim());
            }

            return new VehiculoPasajeros(placa, marca, modelo, anio, tarifa, tipo, pasajeros, traccion);
        }

        if (tipo.getCategoria() == CategoriaVehiculo.CARGA) {

            double capacidad = 0;

            if (tipo.isRequiereCapacidadCarga()) {
                capacidad = Double.parseDouble(txtCapacidadCarga.getText().trim());
            }

            return new VehiculoCarga(placa, marca, modelo, anio, tarifa, tipo, capacidad, traccion);
        }

        throw new IllegalArgumentException("La categoría del vehículo no es válida.");
    }

    /*
     * Qué hace: Actualiza la tabla principal de vehículos cargando todos los registros disponibles actualmente desde el servicio de vehículos
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    private void actualizarTabla() {

        if (vehiculoServicio == null) {
            return;
        }

        tablaVehiculos.setItems(FXCollections.observableArrayList(vehiculoServicio.obtenerTodos()));
    }

    /*
     * Qué hace: Habilita o deshabilita dinámicamente los campos de pasajeros, capacidad de carga y tipo de tracción según las características requeridas por el tipo de vehículo seleccionado
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    private void actualizarCamposPorTipo() {

        TipoVehiculo tipo = cmbTipo.getValue();

        if (tipo == null) {
            txtPasajeros.setDisable(true);
            txtCapacidadCarga.setDisable(true);
            cmbTraccion.setDisable(true);

            return;
        }

        txtPasajeros.setDisable(!tipo.isRequierePasajeros());

        txtCapacidadCarga.setDisable(!tipo.isRequiereCapacidadCarga());

        cmbTraccion.setDisable(!tipo.isRequiereTraccion());
    }

    /*
     * Qué hace: Obtiene el vehículo seleccionado en la tabla y carga todos sus datos generales y específicos en el formulario para permitir su consulta o modificación
     * Recibe: No recibe parámetros directamente, utiliza la selección actual de la tabla
     * Retorna: No retorna ningún valor
     */
    private void cargarVehiculoSeleccionado() {

        Vehiculo vehiculo = tablaVehiculos.getSelectionModel().getSelectedItem();

        if (vehiculo == null) {
            return;
        }

        txtPlaca.setText(vehiculo.getPlaca());
        txtMarca.setText(vehiculo.getMarca());
        txtModelo.setText(vehiculo.getModelo());
        txtAnio.setText(String.valueOf(vehiculo.getAnio()));
        txtTarifa.setText(String.valueOf(vehiculo.getTarifaDiaria()));

        txtPlaca.setDisable(true);
        cmbTipo.setDisable(false);

        cmbTipo.setValue(vehiculo.getTipoVehiculo());

        if (vehiculo instanceof VehiculoPasajeros pasajeros) {

            txtPasajeros.setText(String.valueOf(pasajeros.getCantidadPasajeros()));

            cmbTraccion.setValue(pasajeros.getTipoTraccion());
        }

        if (vehiculo instanceof VehiculoCarga carga) {

            txtCapacidadCarga.setText(String.valueOf(carga.getCapacidadCarga()));

            cmbTraccion.setValue(carga.getTipoTraccion());
        }

        actualizarCamposPorTipo();
    }

    /*
     * Qué hace: Muestra una ventana de alerta de tipo error para informar al usuario sobre un problema ocurrido durante una operación con vehículos
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
     * Qué hace: Muestra una ventana informativa para comunicar al usuario que una operación relacionada con vehículos se realizó correctamente
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