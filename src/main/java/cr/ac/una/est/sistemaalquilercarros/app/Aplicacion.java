package cr.ac.una.est.sistemaalquilercarros.app;

import cr.ac.una.est.sistemaalquilercarros.controlador.PrincipalController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Aplicacion extends Application {

    /*
     * Qué hace: Inicia la aplicación JavaFX creando el contexto general del sistema, cargando la vista principal desde su archivo FXML, conectando el controlador principal con los servicios de la aplicación y configurando la ventana que será mostrada al usuario
     * Recibe: El Stage principal proporcionado por JavaFX donde se mostrará la interfaz de la aplicación
     * Retorna: No retorna ningún valor
     */
    @Override
    public void start(Stage stage) throws IOException {

        ContextoAplicacion contexto = new ContextoAplicacion();

        FXMLLoader fxmlLoader = new FXMLLoader(Aplicacion.class.getResource("/cr/ac/una/est/sistemaalquilercarros/fxml/principal.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);

        PrincipalController controller = fxmlLoader.getController();

        controller.setContexto(contexto);

        stage.setTitle("Sistema de Alquiler de Carros");

        stage.setScene(scene);
        stage.show();
    }

    /*
     * Qué hace: Funciona como punto de entrada principal del programa y solicita a JavaFX que inicie el ciclo de ejecución de la aplicación
     * Recibe: Un arreglo de cadenas que puede contener argumentos enviados al programa al momento de ejecutarlo
     * Retorna: No retorna ningún valor
     */
    public static void main(String[] args) {
        launch();
    }

}
