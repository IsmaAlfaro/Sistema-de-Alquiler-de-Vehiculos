package cr.ac.una.est.sistemaalquilercarros.controlador;

import cr.ac.una.est.sistemaalquilercarros.modelo.CategoriaVehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.TipoVehiculo;
import cr.ac.una.est.sistemaalquilercarros.servicio.TipoVehiculoServicio;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class TiposVehiculoController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtBuscar;

    @FXML
    private ComboBox<CategoriaVehiculo> cmbCategoria;

    @FXML
    private CheckBox chkPasajeros;

    @FXML
    private CheckBox chkTraccion;

    @FXML
    private CheckBox chkCapacidadCarga;

    @FXML
    private TableView<TipoVehiculo> tablaTipos;

    @FXML
    private TableColumn<TipoVehiculo, String> colNombre;

    @FXML
    private TableColumn<TipoVehiculo, String> colCategoria;

    @FXML
    private TableColumn<TipoVehiculo, String> colPasajeros;

    @FXML
    private TableColumn<TipoVehiculo, String> colTraccion;

    @FXML
    private TableColumn<TipoVehiculo, String> colCarga;

    private TipoVehiculoServicio tipoVehiculoServicio;

    private ObservableList<TipoVehiculo> listaTipos;

    private FilteredList<TipoVehiculo> tiposFiltrados;

    /*
     * Qué hace: Inicializa la pantalla de tipos de vehículo cargando las categorías disponibles, configurando las columnas de la tabla y preparando el buscador de tipos registrados
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void initialize() {

        cmbCategoria.setItems(FXCollections.observableArrayList(CategoriaVehiculo.values()));

        colNombre.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getNombre()));

        colCategoria.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getCategoria().toString()));

        colPasajeros.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().isRequierePasajeros() ? "Sí" : "No"));

        colTraccion.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().isRequiereTraccion() ? "Sí" : "No"));

        colCarga.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().isRequiereCapacidadCarga() ? "Sí" : "No"));

        txtBuscar.textProperty().addListener((observable, anterior, nuevo) -> filtrarTipos(nuevo));

        tablaTipos.setOnMouseClicked(evento -> cargarTipoSeleccionado());

    }

    /*
     * Qué hace: Asigna el servicio encargado de administrar los tipos de vehículo y actualiza inmediatamente la tabla con la información disponible
     * Recibe: El servicio de tipos de vehículo que utilizará el controlador
     * Retorna: No retorna ningún valor
     */
    public void setTipoVehiculoServicio(TipoVehiculoServicio tipoVehiculoServicio) {

        this.tipoVehiculoServicio = tipoVehiculoServicio;

        actualizarTabla();

    }

    /*
     * Qué hace: Crea un nuevo tipo de vehículo utilizando el nombre, la categoría y las características seleccionadas en la pantalla, y solicita su registro al servicio correspondiente
     * Recibe: No recibe parámetros directamente, utiliza los valores ingresados y seleccionados en el formulario
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void registrarTipo() {

        try {

            String nombre = txtNombre.getText().trim();

            CategoriaVehiculo categoria = cmbCategoria.getValue();

            TipoVehiculo tipo = new TipoVehiculo(nombre, categoria, chkPasajeros.isSelected(), chkTraccion.isSelected(), chkCapacidadCarga.isSelected());

            tipoVehiculoServicio.registrarTipo(tipo);

            actualizarTabla();

            limpiarFormulario();

            mostrarInformacion("Tipo de vehículo registrado correctamente.");

        } catch (IllegalArgumentException e) {

            mostrarError(e.getMessage());

        } catch (RuntimeException e) {

            mostrarError(e.getMessage());

        }

    }

    /*
     * Qué hace: Carga en el formulario los datos del tipo de vehículo seleccionado en la tabla para permitir su modificación
     * Recibe: No recibe parámetros directamente, utiliza el tipo seleccionado actualmente en la tabla
     * Retorna: No retorna ningún valor
     */
    private void cargarTipoSeleccionado() {

        TipoVehiculo seleccionado = tablaTipos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {

            return;

        }

        txtNombre.setText(seleccionado.getNombre());

        cmbCategoria.setValue(seleccionado.getCategoria());

        chkPasajeros.setSelected(seleccionado.isRequierePasajeros());

        chkTraccion.setSelected(seleccionado.isRequiereTraccion());

        chkCapacidadCarga.setSelected(seleccionado.isRequiereCapacidadCarga());

    }

    /*
     * Qué hace: Modifica los datos del tipo de vehículo seleccionado utilizando los valores actuales del formulario y actualiza la información almacenada en la base de datos
     * Recibe: No recibe parámetros directamente, utiliza el tipo seleccionado en la tabla y los valores ingresados en el formulario
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void modificarTipo() {

        TipoVehiculo seleccionado = tablaTipos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {

            mostrarError("Debe seleccionar un tipo de vehículo.");

            return;

        }

        try {

            String nombre = txtNombre.getText().trim();

            CategoriaVehiculo categoria = cmbCategoria.getValue();

            if (!seleccionado.getNombre().equalsIgnoreCase(nombre)) {

                throw new IllegalArgumentException("El nombre del tipo de vehículo no se puede modificar.");

            }

            TipoVehiculo tipoModificado = new TipoVehiculo(nombre, categoria, chkPasajeros.isSelected(), chkTraccion.isSelected(), chkCapacidadCarga.isSelected());

            tipoVehiculoServicio.modificarTipo(tipoModificado);

            actualizarTabla();

            limpiarFormulario();

            mostrarInformacion("Tipo de vehículo modificado correctamente.");

        } catch (IllegalArgumentException e) {

            mostrarError(e.getMessage());

        } catch (RuntimeException e) {

            mostrarError(e.getMessage());

        }

    }

    /*
     * Qué hace: Elimina del sistema el tipo de vehículo seleccionado en la tabla después de comprobar que exista una selección válida
     * Recibe: No recibe parámetros directamente, utiliza el tipo seleccionado en la tabla
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void eliminarTipo() {

        TipoVehiculo seleccionado = tablaTipos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {

            mostrarError("Debe seleccionar un tipo de vehículo.");

            return;

        }

        try {

            tipoVehiculoServicio.eliminarTipo(seleccionado.getNombre());

            actualizarTabla();

            limpiarFormulario();

            mostrarInformacion("Tipo de vehículo eliminado correctamente.");

        } catch (IllegalArgumentException e) {

            mostrarError(e.getMessage());

        } catch (RuntimeException e) {

            mostrarError(e.getMessage());

        }

    }

    /*
     * Qué hace: Limpia el nombre, la categoría, las características seleccionadas y cualquier selección existente en la tabla de tipos de vehículo
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void limpiarFormulario() {

        txtNombre.clear();

        cmbCategoria.getSelectionModel().clearSelection();

        chkPasajeros.setSelected(false);

        chkTraccion.setSelected(false);

        chkCapacidadCarga.setSelected(false);

        tablaTipos.getSelectionModel().clearSelection();

    }

    /*
     * Qué hace: Actualiza la tabla de tipos de vehículo cargando todos los registros disponibles actualmente desde el servicio correspondiente y prepara la lista utilizada por el buscador
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    private void actualizarTabla() {

        if (tipoVehiculoServicio == null) {

            return;

        }

        try {

            listaTipos = FXCollections.observableArrayList(tipoVehiculoServicio.obtenerTodos());

            tiposFiltrados = new FilteredList<>(listaTipos, tipo -> true);

            tablaTipos.setItems(tiposFiltrados);

            filtrarTipos(txtBuscar.getText());

        } catch (RuntimeException e) {

            mostrarError(e.getMessage());

        }

    }

    /*
     * Qué hace: Filtra los tipos de vehículo mostrados en la tabla utilizando el nombre, la categoría o las características registradas
     * Recibe: El texto utilizado para realizar la búsqueda
     * Retorna: No retorna ningún valor
     */
    private void filtrarTipos(String textoBusqueda) {

        if (tiposFiltrados == null) {

            return;

        }

        tiposFiltrados.setPredicate(tipo -> {

            if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {

                return true;

            }

            String busqueda = textoBusqueda.toLowerCase().trim();

            String pasajeros = tipo.isRequierePasajeros() ? "sí" : "no";

            String traccion = tipo.isRequiereTraccion() ? "sí" : "no";

            String carga = tipo.isRequiereCapacidadCarga() ? "sí" : "no";

            return tipo.getNombre().toLowerCase().contains(busqueda)
                    || tipo.getCategoria().toString().toLowerCase().contains(busqueda)
                    || pasajeros.contains(busqueda)
                    || traccion.contains(busqueda)
                    || carga.contains(busqueda);

        });

    }

    /*
     * Qué hace: Muestra una ventana de alerta de tipo error para informar al usuario sobre un problema ocurrido durante una operación con tipos de vehículo
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
     * Qué hace: Muestra una ventana informativa para comunicar al usuario que una operación relacionada con los tipos de vehículo finalizó correctamente
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