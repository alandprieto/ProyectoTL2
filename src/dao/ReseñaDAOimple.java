package dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import database.ConexionBD;
import modelo.Reseña;

public class ReseñaDAOimple implements ReseñaDAO {

    @Override
    public void guardar(Reseña resenia) {
        // CORREGIDO: Tabla 'Resena' (sin tilde)
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
        } catch (SQLException e) { return false; }
    }

    // Stubs
    @Override public List<Reseña> listarNoAprobadas() { return new ArrayList<>(); }
    @Override public void aprobarResenia(int id) { }
    @Override public void eliminar(int id) { }
}