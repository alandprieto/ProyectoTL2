package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class CargaDatosPrueba {

    public static void cargarDatos() {
        Connection conn = ConexionBD.getConnection();
        if (conn == null) {
            System.err.println("No se pudo cargar datos de prueba. Conexión nula.");
            return;
        }

        try (Statement stmt = conn.createStatement()) {
            
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Usuario");
            int userCount = 0;
            if (rs.next()) {
                userCount = rs.getInt(1);
            }
            rs.close(); 

            if (userCount == 0) {
                System.out.println("Base de datos vacía. Cargando datos de prueba...");

                stmt.execute("INSERT INTO Usuario (DNI, Nombre, Apellido, Email, Contrasena, TipoUsuario) VALUES (11111111, 'Admin', 'Uno', 'admin1@streaming.com', 'admin123', 'Administrador');");
                stmt.execute("INSERT INTO Usuario (DNI, Nombre, Apellido, Email, Contrasena, TipoUsuario) VALUES (22222222, 'Super', 'Visor', 'admin2@streaming.com', 'admin123', 'Administrador');");
                stmt.execute("INSERT INTO Usuario (DNI, Nombre, Apellido, Email, Contrasena, TipoUsuario) VALUES (33333333, 'Carlos', 'Gomez', 'carlos@gmail.com', 'pass123', 'Cliente');");
                stmt.execute("INSERT INTO Usuario (DNI, Nombre, Apellido, Email, Contrasena, TipoUsuario) VALUES (44444444, 'Ana', 'Perez', 'ana@hotmail.com', 'pass123', 'Cliente');");
                stmt.execute("INSERT INTO Usuario (DNI, Nombre, Apellido, Email, Contrasena, TipoUsuario) VALUES (55555555, 'Lucia', 'Diaz', 'lucia@yahoo.com', 'pass123', 'Cliente');");
                System.out.println("-> 5 usuarios cargados.");
                
                stmt.execute("INSERT INTO Pelicula (Genero, Titulo, Director, DuracionMinutos) VALUES ('CIENCIA_FICCION', 'Inception', 'Christopher Nolan', 148);");
                stmt.execute("INSERT INTO Pelicula (Genero, Titulo, Director, DuracionMinutos) VALUES ('DRAMA', 'The Shawshank Redemption', 'Frank Darabont', 142);");
                stmt.execute("INSERT INTO Pelicula (Genero, Titulo, Director, DuracionMinutos) VALUES ('ACCION', 'The Dark Knight', 'Christopher Nolan', 152);");
                stmt.execute("INSERT INTO Pelicula (Genero, Titulo, Director, DuracionMinutos) VALUES ('COMEDIA', 'Pulp Fiction', 'Quentin Tarantino', 154);");
                stmt.execute("INSERT INTO Pelicula (Genero, Titulo, Director, DuracionMinutos) VALUES ('CIENCIA_FICCION', 'The Matrix', 'Wachowskis', 136);");
                System.out.println("-> 5 películas cargadas.");

                // Reseña de Carlos (ID 3) para Inception (ID 1) - Aprobada
                stmt.execute("INSERT INTO Resena (Puntaje, Comentario, Aprobada, FechaHora, UsuarioID, PeliculaID) " +
                             "VALUES (5, 'Una obra maestra, te vuela la cabeza.', 1, '2024-05-20T10:00:00', 3, 1);");
                
                // Reseña de Ana (ID 4) para The Dark Knight (ID 3) - Aprobada
                stmt.execute("INSERT INTO Resena (Puntaje, Comentario, Aprobada, FechaHora, UsuarioID, PeliculaID) " +
                             "VALUES (5, 'La mejor película de superhéroes.', 1, '2024-05-21T11:30:00', 4, 3);");

                // Reseña de Lucia (ID 5) para The Matrix (ID 5) - Pendiente de aprobación
                stmt.execute("INSERT INTO Resena (Puntaje, Comentario, Aprobada, FechaHora, UsuarioID, PeliculaID) " +
                             "VALUES (4, 'Revolucionaria para su época.', 0, '2024-05-22T15:00:00', 5, 5);");

                // Reseña de Carlos (ID 3) para Pulp Fiction (ID 4) - Pendiente de aprobación
                stmt.execute("INSERT INTO Resena (Puntaje, Comentario, Aprobada, FechaHora, UsuarioID, PeliculaID) " +
                             "VALUES (3, 'Un poco rara pero buenos diálogos.', 0, '2024-05-23T18:45:00', 3, 4);");

                // Reseña de Ana (ID 4) para Inception (ID 1) - Aprobada
                stmt.execute("INSERT INTO Resena (Puntaje, Comentario, Aprobada, FechaHora, UsuarioID, PeliculaID) " +
                             "VALUES (4, 'Tuve que verla dos veces para entenderla. Genial!', 1, '2024-05-24T09:00:00', 4, 1);");
                System.out.println("-> 5 reseñas cargadas.");

                System.out.println("Carga de datos iniciales completada.");
                
            } else {
                System.out.println("La base de datos ya contiene datos. No se cargan datos de prueba.");
            }

        } catch (SQLException e) {
            System.err.println("Error al cargar datos de prueba: " + e.getMessage());
        }
    }
}