package cr.ac.una.est.sistemaalquilercarros.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/sistema_alquiler_carros";

    private static final String USUARIO = "";

    private static final String CONTRASENA = "";

    public Connection obtenerConexion() throws SQLException {

        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);

    }
}
