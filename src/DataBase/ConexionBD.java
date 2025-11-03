package DataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
                creacionDeTablasEnBD(connection);
                cargarDatosIniciales(connection); 
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

    private static void cargarDatosIniciales(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM USUARIO");
            if (rs.next() && rs.getInt(1) > 0) {
                return; 
            }

            System.out.println("Base de datos vacía. Cargando datos iniciales...");

            // --- Cargar Usuarios (2 Admins, 3 Clientes) ---
            stmt.executeUpdate("INSERT INTO USUARIO (DNI, NOMBRE, APELLIDO, EMAIL, CONTRASENA, ROL) VALUES (11111111, 'Admin', 'Uno', 'admin1@streaming.com', 'admin123', 'ADMIN');");
            stmt.executeUpdate("INSERT INTO USUARIO (DNI, NOMBRE, APELLIDO, EMAIL, CONTRASENA, ROL) VALUES (22222222, 'Super', 'Visor', 'admin2@streaming.com', 'admin123', 'ADMIN');");
            stmt.executeUpdate("INSERT INTO USUARIO (DNI, NOMBRE, APELLIDO, EMAIL, CONTRASENA, ROL) VALUES (33333333, 'Carlos', 'Gomez', 'carlos@gmail.com', 'pass123', 'CLIENTE');");
            stmt.executeUpdate("INSERT INTO USUARIO (DNI, NOMBRE, APELLIDO, EMAIL, CONTRASENA, ROL) VALUES (44444444, 'Ana', 'Perez', 'ana@hotmail.com', 'pass123', 'CLIENTE');");
            stmt.executeUpdate("INSERT INTO USUARIO (DNI, NOMBRE, APELLIDO, EMAIL, CONTRASENA, ROL) VALUES (55555555, 'Lucia', 'Diaz', 'lucia@yahoo.com', 'pass123', 'CLIENTE');");
            System.out.println("-> 5 usuarios cargados.");

            // --- Cargar Películas ---
            stmt.executeUpdate("INSERT INTO PELICULA (GENERO, TITULO, DIRECTOR, DURACION) VALUES ('CIENCIA_FICCION', 'Inception', 'Christopher Nolan', 148);");
            stmt.executeUpdate("INSERT INTO PELICULA (GENERO, TITULO, DIRECTOR, DURACION) VALUES ('DRAMA', 'The Shawshank Redemption', 'Frank Darabont', 142);");
            stmt.executeUpdate("INSERT INTO PELICULA (GENERO, TITULO, DIRECTOR, DURACION) VALUES ('ACCION', 'The Dark Knight', 'Christopher Nolan', 152);");
            stmt.executeUpdate("INSERT INTO PELICULA (GENERO, TITULO, DIRECTOR, DURACION) VALUES ('COMEDIA', 'Pulp Fiction', 'Quentin Tarantino', 154);");
            stmt.executeUpdate("INSERT INTO PELICULA (GENERO, TITULO, DIRECTOR, DURACION) VALUES ('CIENCIA_FICCION', 'The Matrix', 'Wachowskis', 136);");
            System.out.println("-> 5 películas cargadas.");

            // --- Cargar Reseñas ---
            // Reseña de Carlos (ID 3) para Inception (ID 1) - Aprobada
            stmt.executeUpdate("INSERT INTO RESENIA (CALIFICACION, COMENTARIO, APROBADO, FECHA_HORA, ID_USUARIO, ID_PELICULA) " + "VALUES (5, 'Una obra maestra, te vuela la cabeza.', 1, '2024-05-20T10:00:00', 3, 1);");
            
            // Reseña de Ana (ID 4) para The Dark Knight (ID 3) - Aprobada
            stmt.executeUpdate("INSERT INTO RESENIA (CALIFICACION, COMENTARIO, APROBADO, FECHA_HORA, ID_USUARIO, ID_PELICULA) " + "VALUES (5, 'La mejor película de superhéroes.', 1, '2024-05-21T11:30:00', 4, 3);");

            // Reseña de Lucia (ID 5) para The Matrix (ID 5) - Pendiente de aprobación
            stmt.executeUpdate("INSERT INTO RESENIA (CALIFICACION, COMENTARIO, APROBADO, FECHA_HORA, ID_USUARIO, ID_PELICULA) " + "VALUES (4, 'Revolucionaria para su época.', 0, '2024-05-22T15:00:00', 5, 5);");

            // Reseña de Carlos (ID 3) para Pulp Fiction (ID 4) - Pendiente de aprobación
            stmt.executeUpdate("INSERT INTO RESENIA (CALIFICACION, COMENTARIO, APROBADO, FECHA_HORA, ID_USUARIO, ID_PELICULA) " + "VALUES (3, 'Un poco rara pero buenos diálogos.', 0, '2024-05-23T18:45:00', 3, 4);");

            // Reseña de Ana (ID 4) para Inception (ID 1) - Aprobada
            stmt.executeUpdate("INSERT INTO RESENIA (CALIFICACION, COMENTARIO, APROBADO, FECHA_HORA, ID_USUARIO, ID_PELICULA) " + "VALUES (4, 'Tuve que verla dos veces para entenderla. Genial!', 1, '2024-05-24T09:00:00', 4, 1);");
            System.out.println("-> 5 reseñas cargadas.");

            System.out.println("Carga de datos iniciales completada.");
        }
    }
}
