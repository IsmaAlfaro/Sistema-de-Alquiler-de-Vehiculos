package cr.ac.una.est.sistemaalquilercarros.app;

import cr.ac.una.est.sistemaalquilercarros.controlador.PrincipalController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Aplicacion extends Application {

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

    public static void main(String[] args) {
        launch();
    }

}
