package cr.ac.una.est.sistemaalquilercarros.controlador;

import cr.ac.una.est.sistemaalquilercarros.app.ContextoAplicacion;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InicioController {

    @FXML
    private Label lblCantidadVehiculos;

    @FXML
    private Label lblVehiculosDisponibles;

    @FXML
    private Label lblCantidadClientes;

    @FXML
    private Label lblAlquileresActivos;

    private ContextoAplicacion contexto;


    /*
     * Qué hace: Recibe y almacena el contexto general de la aplicación para poder acceder a los distintos servicios y actualizar los datos mostrados en la pantalla de inicio
     * Recibe: El contexto general de la aplicación
     * Retorna: No retorna ningún valor
     */
    public void setContexto(ContextoAplicacion contexto) {

        this.contexto = contexto;

        actualizarDatos();
    }


    /*
     * Qué hace: Inicia en un hilo separado la consulta de las estadísticas principales del sistema para evitar que la interfaz gráfica se bloquee mientras se accede a la base de datos
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    private void actualizarDatos() {

        lblCantidadVehiculos.setText("...");
        lblVehiculosDisponibles.setText("...");
        lblCantidadClientes.setText("...");
        lblAlquileresActivos.setText("...");

        Task<int[]> tarea = new Task<>() {

                    @Override
                    protected int[] call() {

                        int cantidadVehiculos = contexto.getVehiculoServicio().cantidadVehiculos();

                        int vehiculosDisponibles = contexto.getVehiculoServicio().cantidadDisponibles();

                        int cantidadClientes = contexto.getClienteServicio().cantidadClientes();

                        int alquileresActivos = contexto.getAlquilerServicio().cantidadAlquileresActivos();

                        return new int[]{cantidadVehiculos, vehiculosDisponibles, cantidadClientes, alquileresActivos};

                    }

                };


        tarea.setOnSucceeded(evento -> {

                    int[] resultados = tarea.getValue();

                    lblCantidadVehiculos.setText(String.valueOf(resultados[0]));

                    lblVehiculosDisponibles.setText(String.valueOf(resultados[1]));

                    lblCantidadClientes.setText(String.valueOf(resultados[2]));

                    lblAlquileresActivos.setText(String.valueOf(resultados[3]));

                }
        );


        tarea.setOnFailed(evento -> {

                    lblCantidadVehiculos.setText("Error");

                    lblVehiculosDisponibles.setText("Error");

                    lblCantidadClientes.setText("Error");

                    lblAlquileresActivos.setText("Error");

                    Throwable error = tarea.getException();

                    if (error != null) {
                        error.printStackTrace();
                    }

                }
        );


        Thread hilo = new Thread(tarea);

        hilo.setDaemon(true);

        hilo.start();
    }
}