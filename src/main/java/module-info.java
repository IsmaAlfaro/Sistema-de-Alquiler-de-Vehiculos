module cr.ac.una.est.sistemaalquilercarros {

    requires javafx.controls;
    requires javafx.fxml;
    exports cr.ac.una.est.sistemaalquilercarros.app;
    exports cr.ac.una.est.sistemaalquilercarros.modelo;
    exports cr.ac.una.est.sistemaalquilercarros.servicio;
    exports cr.ac.una.est.sistemaalquilercarros.repositorio;
    exports cr.ac.una.est.sistemaalquilercarros.persistencia;
    exports cr.ac.una.est.sistemaalquilercarros.interfaces;

    opens cr.ac.una.est.sistemaalquilercarros.controlador to javafx.fxml;

}