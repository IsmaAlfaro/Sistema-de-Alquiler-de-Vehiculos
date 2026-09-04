package cr.ac.una.est.sistemaalquilercarros.controlador;
import cr.ac.una.est.sistemaalquilercarros.modelo.Alquiler;
import cr.ac.una.est.sistemaalquilercarros.servicio.AlquilerServicio;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.time.LocalDate;
public class HistorialController {
    @FXML
    private TableView<Alquiler> tablaHistorial;
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
    private TableColumn<Alquiler, LocalDate> colFechaDevolucion;
    @FXML
    private TableColumn<Alquiler, Integer> colDias;
    @FXML
    private TableColumn<Alquiler, String> colTotal;
    @FXML
    private TableColumn<Alquiler, String> colEstado;
    private AlquilerServicio alquilerServicio;
    @FXML
    private void initialize() {
        colNumero.setCellValueFactory(dato -> new ReadOnlyObjectWrapper<>(dato.getValue().getNumeroAlquiler()));
        colCliente.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getCliente().getNombreCompleto()));
        colVehiculo.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getVehiculo().toString()));
        colFechaInicio.setCellValueFactory(dato -> new ReadOnlyObjectWrapper<>(dato.getValue().getFechaInicio()));
        colFechaEsperada.setCellValueFactory(dato -> new ReadOnlyObjectWrapper<>(dato.getValue().getFechaDevolucionEsperada()));
        colFechaDevolucion.setCellValueFactory(dato -> new ReadOnlyObjectWrapper<>(dato.getValue().getFechaDevolucionReal()));
        colDias.setCellValueFactory(dato -> new ReadOnlyObjectWrapper<>(dato.getValue().getCantidadDias()));
        colTotal.setCellValueFactory(dato -> new SimpleStringProperty(String.format("₡%,.2f", dato.getValue().calcularTotal())));
        colEstado.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getEstado().toString()));
    }
    public void setAlquilerServicio(AlquilerServicio alquilerServicio) {
        this.alquilerServicio = alquilerServicio;
        actualizarTabla();
    }
    private void actualizarTabla() {
        tablaHistorial.setItems(FXCollections.observableArrayList(alquilerServicio.obtenerTodos()));
    }
}
