package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import Catalogo.Reseña;
import DataBase.ConexionBD;
import Usuario.Usuario;

public class ReseñaDAOimple implements ReseñaDAO {

    @Override
    public void guardar(Reseña resenia) {
        String sql = "INSERT INTO RESENIA (CALIFICACION, COMENTARIO, FECHA_HORA, USUARIO, ID_PELICULA) VALUES (?, ?, ?, ?, ?)";
        Connection conn = ConexionBD.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, resenia.getCalificacion());
            pstmt.setString(2, resenia.getComentario());

            // Guardamos la fecha y hora actual en formato de texto estándar
            pstmt.setString(3, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

            // Obtenemos los IDs directamente del objeto Reseña
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
    public List<Reseña> listarNoAprobadas() {
        List<Reseña> resenias = new ArrayList<>();
        String sql = "SELECT * FROM RESENIA WHERE APROBADO = 0";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            UsuarioDAOimple usuarioDAO = new UsuarioDAOimple();

            while (rs.next()) {
                Reseña resenia = new Reseña();
                resenia.setID(rs.getInt("ID"));
                resenia.setCalificacion(rs.getInt("CALIFICACION"));
                resenia.setComentario(rs.getString("COMENTARIO"));
                resenia.setIDContenido(rs.getInt("ID_PELICULA"));

                int idUsuario = rs.getInt("ID_USUARIO");
                Usuario usuario = usuarioDAO.buscarPorId(idUsuario);
                resenia.setUsuario(usuario);

                resenias.add(resenia);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reseñas no aprobadas: " + e.getMessage());
        }
        return resenias;
    }

    @Override
    public void aprobarResenia(int idResenia) {
        String sql = "UPDATE RESENIA SET APROBADO = 1 WHERE ID = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idResenia);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("--> Reseña con ID " + idResenia + " aprobada correctamente.");
            } else {
                System.out.println("No se encontró una reseña con el ID " + idResenia + " para aprobar.");
            }

        } catch (SQLException e) {
            System.err.println("Error al aprobar la reseña: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int idResenia) {
        String sql = "DELETE FROM RESENIA WHERE ID = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
}