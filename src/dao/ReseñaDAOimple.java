package dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import database.ConexionBD;
import modelo.Reseña;

/**
 * Implementación DAO para operaciones de Reseña en base de datos.
 */
public class ReseñaDAOimple implements ReseñaDAO {

    /**
     * Guarda una nueva reseña en la base de datos.
     */
    @Override
    public void guardar(Reseña resenia) {
        String sql = "INSERT INTO Resena (UsuarioID, PeliculaID, Comentario, Puntaje, Aprobada, FechaHora) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = ConexionBD.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, resenia.getUsuario().getID());
            pstmt.setInt(2, resenia.getIDContenido());
            pstmt.setString(3, resenia.getComentario());
            pstmt.setInt(4, resenia.getCalificacion());
            pstmt.setBoolean(5, true); 
            pstmt.setString(6, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar reseña: " + e.getMessage());
        }
    }

    /**
     * Verifica si existe una reseña de un usuario para una película.
     */
    @Override
    public boolean existeResena(int idUsuario, int idPelicula) {
        String sql = "SELECT 1 FROM Resena WHERE UsuarioID = ? AND PeliculaID = ?";
        Connection conn = ConexionBD.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idPelicula);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) { 
            return false; 
        }
    }

    /**
     * Lista todas las reseñas no aprobadas.
     */
    @Override 
    public List<Reseña> listarNoAprobadas() { 
        return new ArrayList<>(); 
    }

    /**
     * Aprueba una reseña por su ID.
     */
    @Override 
    public void aprobarResenia(int id) { 
    }

    /**
     * Elimina una reseña por su ID.
     */
    @Override 
    public void eliminar(int id) { 
    }
}