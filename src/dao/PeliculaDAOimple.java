package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import enums.GeneroPelicula;
import modelo.Pelicula;
import modelo.Staff;
import database.ConexionBD;

import java.time.Duration;


public class PeliculaDAOimple implements PeliculaDAO {

    @Override
    public void guardar(Pelicula pelicula) {
        String sql = "INSERT INTO PELICULA (GENERO, TITULO, DIRECTOR, DURACION) VALUES (?, ?, ?, ?)";
        Connection conn = ConexionBD.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, pelicula.getGenero().name());
            pstmt.setString(2, pelicula.getTitulo());
            pstmt.setString(3, pelicula.getDirector().getNombre());
            pstmt.setLong(4, pelicula.getDuracion().toMinutes());

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pelicula.setID(generatedKeys.getInt(1));
                        System.out.println(
                                "--> Película '" + pelicula.getTitulo() + "' guardada con ID: " + pelicula.getID());
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al guardar la película: " + e.getMessage());
        }
    }

    
    @Override
    public List<Pelicula> listarTodas() {
        List<Pelicula> peliculas = new ArrayList<>();
        String sql = "SELECT * FROM PELICULA";
        Connection conn = ConexionBD.getConnection();

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pelicula pelicula = new Pelicula();
                pelicula.setID(rs.getInt("ID"));
                pelicula.setTitulo(rs.getString("TITULO"));
                pelicula.setDirector(new Staff(rs.getString("DIRECTOR"), "Director"));
                int duracionMinutos = rs.getInt("DURACION");
                pelicula.setDuracion(Duration.ofMinutes(duracionMinutos));
                pelicula.setGenero(GeneroPelicula.valueOf(rs.getString("GENERO")));

                peliculas.add(pelicula);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar las películas: " + e.getMessage());
        }
        return peliculas;
    }

    @Override
    public boolean existePelicula(int peliculaID) {
        String sql = "SELECT COUNT(*) AS total FROM PELICULA WHERE ID = ?";
        Connection conn = ConexionBD.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, peliculaID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar la existencia de la película: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void eliminar(int peliculaID) {
        String sql = "DELETE FROM PELICULA WHERE ID = ?";
        Connection conn = ConexionBD.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, peliculaID);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("--> Película con ID " + peliculaID + " eliminada.");
            } else {
                System.out.println("--> No se encontró la película con ID " + peliculaID + ".");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar la película: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(Pelicula pelicula) {
        String sql = "UPDATE PELICULA SET GENERO = ?, TITULO = ?, DIRECTOR = ?, DURACION = ? WHERE ID = ?";
        Connection conn = ConexionBD.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pelicula.getGenero().name());
            pstmt.setString(2, pelicula.getTitulo());
            pstmt.setString(3, pelicula.getDirector().getNombre());
            pstmt.setLong(4, pelicula.getDuracion().toMinutes());
            pstmt.setInt(5, pelicula.getID());

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("--> Película con ID " + pelicula.getID() + " actualizada.");
            } else {
                System.out.println("--> No se encontró la película con ID " + pelicula.getID() + ".");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar la película: " + e.getMessage());
        }
    }
}