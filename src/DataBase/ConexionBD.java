package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {

    // Define el nombre del archivo de la base de datos. Se creará en la raíz del proyecto.
    private static final String URL_SQLITE = "jdbc:sqlite:streaming.db";
    private static Connection connection = null;

    /**
     * Constructor privado para evitar que se creen instancias de esta clase.
     */
    private ConexionBD() {}

    /**
     * Obtiene una conexión a la base de datos. Si no existe, la crea.
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL_SQLITE);
                System.out.println("Conexión a SQLite establecida con éxito.");
                creacionDeTablasEnBD(connection);
            } catch (SQLException e) {
                System.err.println("Error al conectar con la base de datos: " + e.getMessage());
                return null;
            }
        }
        return connection;
    }

    /**
     * Cierra la conexión a la base de datos si está abierta.
     */
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

    private static void creacionDeTablasEnBD(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            System.out.println("Verificando y creando tablas si es necesario...");

            String sqlUsuario = "CREATE TABLE IF NOT EXISTS USUARIO (" +
            "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "DNI INTEGER NOT NULL UNIQUE, " +
            "NOMBRE TEXT NOT NULL, " +
            "APELLIDO TEXT NOT NULL, " +
            "EMAIL TEXT NOT NULL UNIQUE, " +
            "CONTRASENA TEXT NOT NULL, " +
            "ROL TEXT NOT NULL" + 
            ");";
            stmt.executeUpdate(sqlUsuario);

            String sqlPelicula = "CREATE TABLE IF NOT EXISTS PELICULA (" +
                    "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "GENERO TEXT(50) NOT NULL, " +
                    "TITULO TEXT(100) NOT NULL, " +
                    "DIRECTOR TEXT(100), " + 
                    "DURACION INTEGER NOT NULL" +
                    ");";
            stmt.executeUpdate(sqlPelicula);

            String sqlResenia = "CREATE TABLE IF NOT EXISTS RESENIA (" +
                    "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "CALIFICACION INTEGER NOT NULL, " +
                    "COMENTARIO TEXT(500), " +
                    "APROBADO INTEGER DEFAULT 0 NOT NULL, " +
                    "FECHA_HORA DATETIME NOT NULL, " +
                    "ID_USUARIO INTEGER NOT NULL, " +
                    "ID_PELICULA INTEGER NOT NULL, " +
                    "FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO(ID), " +
                    "FOREIGN KEY (ID_PELICULA) REFERENCES PELICULA(ID)" +
                    ");";
            stmt.executeUpdate(sqlResenia);
            
            System.out.println("Tablas listas para ser usadas.");
        }
    }
}
