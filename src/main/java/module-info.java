module cr.ac.una.est.sistemaalquilercarros {
    requires javafx.controls;
    requires javafx.fxml;


    opens cr.ac.una.est.sistemaalquilercarros to javafx.fxml;
    exports cr.ac.una.est.sistemaalquilercarros;
}