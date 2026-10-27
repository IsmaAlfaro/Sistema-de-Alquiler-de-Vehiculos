package cr.ac.una.est.sistemaalquilercarros.controlador;

import cr.ac.una.est.sistemaalquilercarros.modelo.Alquiler;
import cr.ac.una.est.sistemaalquilercarros.servicio.AlquilerServicio;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class HistorialController {

    @FXML
    private TextField txtBuscar;

    @FXML
    private ComboBox<String> cmbFiltroEstado;

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

    private ObservableList<Alquiler> listaAlquileres;

    private FilteredList<Alquiler> alquileresFiltrados;

    /*
     * Qué hace: Inicializa la tabla del historial configurando cada columna para mostrar los datos correspondientes de los alquileres registrados y prepara los filtros de búsqueda
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    @FXML
    private void initialize() {

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

        colFechaDevolucion.setCellValueFactory(
                dato -> new ReadOnlyObjectWrapper<>(
                        dato.getValue()
                                .getFechaDevolucionReal()
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

        cmbFiltroEstado.setItems(
                FXCollections.observableArrayList(
                        "TODOS",
                        "ACTIVO",
                        "FINALIZADO"
                )
        );

        cmbFiltroEstado.setValue("TODOS");

        txtBuscar.textProperty().addListener(
                (observable, anterior, nuevo) ->
                        filtrarHistorial()
        );

        cmbFiltroEstado.valueProperty().addListener(
                (observable, anterior, nuevo) ->
                        filtrarHistorial()
        );
    }

    /*
     * Qué hace: Asigna el servicio encargado de administrar los alquileres y actualiza inmediatamente la tabla del historial
     * Recibe: El servicio de alquileres que utilizará el controlador
     * Retorna: No retorna ningún valor
     */
    public void setAlquilerServicio(
            AlquilerServicio alquilerServicio) {

        this.alquilerServicio =
                alquilerServicio;

        actualizarTabla();
    }

    /*
     * Qué hace: Actualiza la tabla del historial cargando todos los alquileres registrados actualmente en el sistema
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    private void actualizarTabla() {

        listaAlquileres =
                FXCollections.observableArrayList(
                        alquilerServicio.obtenerTodos()
                );

        alquileresFiltrados =
                new FilteredList<>(
                        listaAlquileres,
                        alquiler -> true
                );

        tablaHistorial.setItems(
                alquileresFiltrados
        );

        filtrarHistorial();
    }

    /*
     * Qué hace: Filtra los alquileres mostrados en el historial utilizando el número, cliente, cédula, placa, marca, modelo o estado del alquiler
     * Recibe: No recibe parámetros directamente, utiliza el buscador y el filtro de estado
     * Retorna: No retorna ningún valor
     */
    private void filtrarHistorial() {

        if (alquileresFiltrados == null) {
            return;
        }

        String textoBusqueda =
                txtBuscar.getText();

        String estadoSeleccionado =
                cmbFiltroEstado.getValue();

        alquileresFiltrados.setPredicate(alquiler -> {

            boolean coincideBusqueda = true;

            boolean coincideEstado = true;

            if (textoBusqueda != null
                    && !textoBusqueda.trim().isEmpty()) {

                String busqueda =
                        textoBusqueda
                                .toLowerCase()
                                .trim();

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

            if (estadoSeleccionado != null
                    && !estadoSeleccionado.equals("TODOS")) {

                coincideEstado =
                        alquiler.getEstado()
                                .toString()
                                .equals(estadoSeleccionado);
            }

            return coincideBusqueda
                    && coincideEstado;
        });
    }
}