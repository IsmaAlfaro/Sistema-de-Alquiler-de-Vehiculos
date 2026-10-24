package cr.ac.una.est.sistemaalquilercarros.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/sistema_alquiler_carros";

    private static final String USUARIO = "root";

    private static final String CONTRASENA = "1I2s3m4a5e6l";

    public Connection obtenerConexion() throws SQLException {

        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);

    }
}
