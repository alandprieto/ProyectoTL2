package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import database.ConexionBD;
import modelo.Reseña;

public class ReseñaDAOimple implements ReseñaDAO {

    @Override
    public void guardar(Reseña resenia) {
        String sql = "INSERT INTO Resena (Puntaje, Comentario, FechaHora, UsuarioID, PeliculaID) VALUES (?, ?, ?, ?, ?)";
        Connection conn = ConexionBD.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, resenia.getCalificacion());
            pstmt.setString(2, resenia.getComentario());
            pstmt.setString(3, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            pstmt.setInt(4, resenia.getUsuario().getID());
            pstmt.setInt(5, resenia.getIDContenido());
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        resenia.setID(generatedKeys.getInt(1));
                        System.out.println("--> Reseña guardada con ID: " + resenia.getID());
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al guardar la reseña: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int idResenia) {
        String sql = "DELETE FROM RESENIA WHERE ID = ?";
        Connection conn = ConexionBD.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idResenia);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("--> Reseña con ID " + idResenia + " eliminada correctamente.");
            } else {
                System.out.println("No se encontró una reseña con el ID " + idResenia + " para eliminar.");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar la reseña: " + e.getMessage());
        }
    }

    @Override
    public boolean existenResenasPorCliente(int clienteID) {
        String sql = "SELECT COUNT(*) AS total FROM RESENIA R JOIN USUARIO U ON R.UsuarioID = U.ID WHERE U.ID = ?";
        Connection conn = ConexionBD.getConnection();
        boolean existenResenas = false;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clienteID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    existenResenas = rs.getInt("total") > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar reseñas por cliente: " + e.getMessage());
        }
        return existenResenas;
    }
}