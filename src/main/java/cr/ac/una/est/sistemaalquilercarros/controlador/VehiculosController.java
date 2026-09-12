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

//    public void setVehiculoServicio(VehiculoServicio vehiculoServicio) {
//
//        this.vehiculoServicio = vehiculoServicio;
//
//        actualizarTabla();
//    }

    public void setServicios(VehiculoServicio vehiculoServicio, TipoVehiculoServicio tipoVehiculoServicio) {

        this.vehiculoServicio = vehiculoServicio;
        this.tipoVehiculoServicio = tipoVehiculoServicio;

        cargarTipos();
        actualizarTabla();
    }

    private void cargarTipos() {

        cmbTipo.setItems(
                FXCollections.observableArrayList(
                        tipoVehiculoServicio.obtenerTodos()
                )
        );
    }

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

    private void actualizarTabla() {

        if (vehiculoServicio == null) {
            return;
        }

        tablaVehiculos.setItems(FXCollections.observableArrayList(vehiculoServicio.obtenerTodos()));
    }

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