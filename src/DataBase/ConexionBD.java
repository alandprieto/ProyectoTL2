package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL_SQLITE = "jdbc:sqlite:streaming.db";
    private static Connection connection = null;

    private ConexionBD() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL_SQLITE);
                System.out.println("Conexión a SQLite establecida con éxito.");
            } catch (SQLException e) {
                System.err.println("Error al conectar con la base de datos: " + e.getMessage());
                return null;
            }
        }
        return connection;
    }

    public static void cerrarConexion() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión a SQLite cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión a la base de datos: " + e.getMessage());
            }
        }
    }

}
