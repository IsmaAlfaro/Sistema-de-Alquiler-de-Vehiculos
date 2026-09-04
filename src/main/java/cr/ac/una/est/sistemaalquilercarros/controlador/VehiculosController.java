package cr.ac.una.est.sistemaalquilercarros.controlador;

import cr.ac.una.est.sistemaalquilercarros.modelo.Pickup;
import cr.ac.una.est.sistemaalquilercarros.modelo.SUV;
import cr.ac.una.est.sistemaalquilercarros.modelo.Sedan;
import cr.ac.una.est.sistemaalquilercarros.modelo.Vehiculo;
import cr.ac.una.est.sistemaalquilercarros.servicio.VehiculoServicio;

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
    private ComboBox<String> cmbTipo;

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

    @FXML
    private void initialize() {

        cmbTipo.setItems(FXCollections.observableArrayList("Sedán", "SUV", "Pickup"));

        cmbTraccion.setItems(FXCollections.observableArrayList("Delantera", "Trasera", "AWD", "4x4"));

        colPlaca.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getPlaca()));

        colMarca.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getMarca()));

        colModelo.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getModelo()));

        colAnio.setCellValueFactory(dato -> new ReadOnlyObjectWrapper<>(dato.getValue().getAnio()));

        colTipo.setCellValueFactory(dato -> new SimpleStringProperty(obtenerTipo(dato.getValue())));

        colTarifa.setCellValueFactory(dato -> new SimpleStringProperty(String.format("₡%,.2f", dato.getValue().getTarifaDiaria())));

        colEstado.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getEstado().toString()));

        cmbTipo.setOnAction(evento -> actualizarCamposPorTipo());

        tablaVehiculos.setOnMouseClicked(evento -> cargarVehiculoSeleccionado());

        actualizarCamposPorTipo();
    }

    public void setVehiculoServicio(VehiculoServicio vehiculoServicio) {

        this.vehiculoServicio = vehiculoServicio;

        actualizarTabla();
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

        actualizarCamposPorTipo();
    }

    private Vehiculo crearVehiculoDesdeFormulario() {

        String placa = txtPlaca.getText().trim();

        String marca = txtMarca.getText().trim();

        String modelo = txtModelo.getText().trim();

        int anio = Integer.parseInt(txtAnio.getText().trim());

        double tarifa = Double.parseDouble(txtTarifa.getText().trim());

        String tipo = cmbTipo.getValue();

        if (tipo == null) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de vehículo.");
        }

        switch (tipo) {

            case "Sedán" -> {

                int pasajeros = Integer.parseInt(txtPasajeros.getText().trim());

                return new Sedan(placa, marca, modelo, anio, tarifa, pasajeros);
            }

            case "SUV" -> {

                int pasajeros = Integer.parseInt(txtPasajeros.getText().trim());

                String traccion = cmbTraccion.getValue();

                if (traccion == null) {
                    throw new IllegalArgumentException("Debe seleccionar el tipo de tracción.");
                }

                return new SUV(placa, marca, modelo, anio, tarifa, pasajeros, traccion
                );
            }

            case "Pickup" -> {

                double capacidad = Double.parseDouble(txtCapacidadCarga.getText().trim());

                String traccion = cmbTraccion.getValue();

                if (traccion == null) {
                    throw new IllegalArgumentException("Debe seleccionar el tipo de tracción.");
                }

                return new Pickup(placa, marca, modelo, anio, tarifa, capacidad, traccion);
            }

            default -> throw new IllegalArgumentException("Tipo de vehículo no válido.");
        }
    }

    private void actualizarTabla() {

        if (vehiculoServicio == null) {
            return;
        }

        tablaVehiculos.setItems(FXCollections.observableArrayList(vehiculoServicio.obtenerTodos()));
    }

    private void actualizarCamposPorTipo() {

        String tipo = cmbTipo.getValue();

        txtPasajeros.setDisable(tipo == null || tipo.equals("Pickup"));

        txtCapacidadCarga.setDisable(tipo == null || !tipo.equals("Pickup"));

        cmbTraccion.setDisable(tipo == null || tipo.equals("Sedán"));
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

        if (vehiculo instanceof Sedan sedan) {

            cmbTipo.setValue("Sedán");

            txtPasajeros.setText(String.valueOf(sedan.getCantidaPasajeros()));

        } else if (vehiculo instanceof SUV suv) {

            cmbTipo.setValue("SUV");

            txtPasajeros.setText(String.valueOf(suv.getCantidadPasajeros()));

            cmbTraccion.setValue(suv.getTipoTraccion());

        } else if (vehiculo instanceof Pickup pickup) {

            cmbTipo.setValue("Pickup");

            txtCapacidadCarga.setText(String.valueOf(pickup.getCapacidadCarga()));

            cmbTraccion.setValue(pickup.getTipoTraccion());
        }

        actualizarCamposPorTipo();
    }

    private String obtenerTipo(Vehiculo vehiculo) {

        if (vehiculo instanceof Sedan) {
            return "Sedán";
        }

        if (vehiculo instanceof SUV) {
            return "SUV";
        }

        if (vehiculo instanceof Pickup) {
            return "Pickup";
        }

        return "Desconocido";
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