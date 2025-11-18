package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SetupBD {

    private static final String CREATE_USUARIO = "CREATE TABLE IF NOT EXISTS Usuario (" +
            "  ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  DNI LONG NOT NULL UNIQUE," +
            "  Nombre VARCHAR(100) NOT NULL," +
            "  Apellido VARCHAR(100) NOT NULL," +
            "  Email VARCHAR(150) NOT NULL UNIQUE," +
            "  Contrasena VARCHAR(100) NOT NULL," +
            "  TipoUsuario VARCHAR(20) NOT NULL" +
            ");";

    private static final String CREATE_PELICULA = "CREATE TABLE IF NOT EXISTS Pelicula (" +
            "  ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  Titulo VARCHAR(255) NOT NULL," +
            "  Genero VARCHAR(50)," +
            "  Director VARCHAR(150)," +
            "  DuracionMinutos INT," +
            "  Anio INT," +
            "  RatingPromedio FLOAT," +
            "  PosterURL VARCHAR(255)" +
            ");";

    private static final String CREATE_RESENA = "CREATE TABLE IF NOT EXISTS Resena (" +
            "  ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  UsuarioID INT NOT NULL," +
            "  PeliculaID INT NOT NULL," +
            "  Comentario TEXT," +
            "  Puntaje INT NOT NULL," +
            "  Aprobada BOOLEAN DEFAULT 0," +
            "  FechaHora TEXT," +
            "  FOREIGN KEY(UsuarioID) REFERENCES Usuario(ID)," +
            "  FOREIGN KEY(PeliculaID) REFERENCES Pelicula(ID)" +
            ");";

    public static void crearTablas() {
        Connection conn = ConexionBD.getConnection();
        if (conn == null) {
            System.err.println("No se pudo crear tablas. Conexión nula.");
            return;
        }

        try (Statement stmt = conn.createStatement()) {
            System.out.println("Verificando/creando tablas...");
            stmt.execute(CREATE_USUARIO);
            stmt.execute(CREATE_PELICULA);
            stmt.execute(CREATE_RESENA);
            System.out.println("Tablas creadas o ya existentes.");

        } catch (SQLException e) {
            System.err.println("Error al crear las tablas: " + e.getMessage());
        }
    }
}