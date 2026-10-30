package cr.ac.una.est.sistemaalquilercarros.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/sistema_alquiler_carros";

    private static final String USUARIO = "root";

    private static final String CONTRASENA = "admin";

    /*
     * Qué hace: Establece y retorna una nueva conexión con la base de datos MySQL utilizando la dirección, el usuario y la contraseña configurados para el sistema
     * Recibe: No recibe parámetros
     * Retorna: Una conexión activa con la base de datos
     */
    public Connection obtenerConexion() throws SQLException {

        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);

    }
}