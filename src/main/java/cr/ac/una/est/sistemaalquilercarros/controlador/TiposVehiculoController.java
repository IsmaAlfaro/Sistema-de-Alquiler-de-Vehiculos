package cr.ac.una.est.sistemaalquilercarros.controlador;

import cr.ac.una.est.sistemaalquilercarros.modelo.CategoriaVehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.TipoVehiculo;
import cr.ac.una.est.sistemaalquilercarros.servicio.TipoVehiculoServicio;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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

    @FXML
    private void initialize() {

        cmbCategoria.setItems(FXCollections.observableArrayList(CategoriaVehiculo.values()));

        colNombre.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getNombre()));

        colCategoria.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getCategoria().toString()));

        colPasajeros.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().isRequierePasajeros() ? "Sí" : "No"));

        colTraccion.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().isRequiereTraccion() ? "Sí" : "No"));

        colCarga.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().isRequiereCapacidadCarga() ? "Sí" : "No"));
    }

    public void setTipoVehiculoServicio(TipoVehiculoServicio tipoVehiculoServicio) {

        this.tipoVehiculoServicio = tipoVehiculoServicio;

        actualizarTabla();
    }

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
        }
    }

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
        }
    }

    @FXML
    private void limpiarFormulario() {

        txtNombre.clear();

        cmbCategoria.getSelectionModel().clearSelection();

        chkPasajeros.setSelected(false);
        chkTraccion.setSelected(false);
        chkCapacidadCarga.setSelected(false);

        tablaTipos.getSelectionModel().clearSelection();
    }

    private void actualizarTabla() {

        if (tipoVehiculoServicio == null) {
            return;
        }

        tablaTipos.setItems(FXCollections.observableArrayList(tipoVehiculoServicio.obtenerTodos()));
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