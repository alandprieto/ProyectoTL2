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
        String sql = "INSERT INTO Pelicula (Genero, Titulo, Director, DuracionMinutos, Anio, RatingPromedio, PosterURL) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = ConexionBD.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, pelicula.getGenero() != null ? pelicula.getGenero().name() : null);
            pstmt.setString(2, pelicula.getTitulo());
            pstmt.setString(3, pelicula.getDirector() != null ? pelicula.getDirector().getNombre() : null);
            pstmt.setLong(4, pelicula.getDuracion() != null ? pelicula.getDuracion().toMinutes() : 0L);
            pstmt.setInt(5, pelicula.getAnio());
            pstmt.setDouble(6, pelicula.getRatingPromedio());
            pstmt.setString(7, pelicula.getPosterURL());

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
        String sql = "SELECT * FROM Pelicula";
        Connection conn = ConexionBD.getConnection();

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pelicula pelicula = new Pelicula();
                pelicula.setID(rs.getInt("ID"));
                pelicula.setTitulo(rs.getString("Titulo"));
                pelicula.setDirector(new Staff(rs.getString("Director"), "Director"));
                int duracionMinutos = rs.getInt("DuracionMinutos");
                pelicula.setDuracion(Duration.ofMinutes(duracionMinutos));
                String generoStr = rs.getString("Genero");
                if (generoStr != null && !generoStr.isEmpty()) {
                    pelicula.setGenero(GeneroPelicula.valueOf(generoStr));
                }
                pelicula.setAnio(rs.getInt("Anio"));
                pelicula.setRatingPromedio(rs.getDouble("RatingPromedio"));
                pelicula.setPosterURL(rs.getString("PosterURL"));

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
        String sql = "UPDATE Pelicula SET Genero = ?, Titulo = ?, Director = ?, DuracionMinutos = ?, Anio = ?, RatingPromedio = ?, PosterURL = ? WHERE ID = ?";
        Connection conn = ConexionBD.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pelicula.getGenero() != null ? pelicula.getGenero().name() : null);
            pstmt.setString(2, pelicula.getTitulo());
            pstmt.setString(3, pelicula.getDirector() != null ? pelicula.getDirector().getNombre() : null);
            pstmt.setLong(4, pelicula.getDuracion() != null ? pelicula.getDuracion().toMinutes() : 0L);
            pstmt.setInt(5, pelicula.getAnio());
            pstmt.setDouble(6, pelicula.getRatingPromedio());
            pstmt.setString(7, pelicula.getPosterURL());
            pstmt.setInt(8, pelicula.getID());

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

    @Override
    public List<Pelicula> buscarPorTitulo(String titulo) {
        List<Pelicula> peliculas = new ArrayList<>();
        String sql = "SELECT ID, Genero, Titulo, Director, DuracionMinutos, Anio, RatingPromedio, PosterURL FROM Pelicula WHERE Titulo LIKE ?";
        Connection conn = ConexionBD.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + titulo + "%"); // El % es el comodín de SQL
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Pelicula pelicula = new Pelicula();
                    pelicula.setID(rs.getInt("ID"));
                    pelicula.setTitulo(rs.getString("Titulo"));
                    String generoStr = rs.getString("Genero");
                    if (generoStr != null && !generoStr.isEmpty()) {
                        pelicula.setGenero(GeneroPelicula.valueOf(generoStr));
                    }
                    pelicula.setDirector(new Staff(rs.getString("Director"), "Director"));
                    pelicula.setDuracion(Duration.ofMinutes(rs.getLong("DuracionMinutos")));
                    pelicula.setAnio(rs.getInt("Anio"));
                    pelicula.setRatingPromedio(rs.getDouble("RatingPromedio"));
                    pelicula.setPosterURL(rs.getString("PosterURL"));
                    peliculas.add(pelicula);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar películas por título: " + e.getMessage());
        }
        return peliculas;
    }
}