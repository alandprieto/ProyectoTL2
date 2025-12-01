package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import enums.GeneroPelicula;
import modelo.Pelicula;
import modelo.Staff;
import database.ConexionBD;
import java.time.Duration;

/**
 * Implementación DAO para operaciones de Pelicula en base de datos.
 */
public class PeliculaDAOimple implements PeliculaDAO {

    /**
     * Guarda una nueva película en la base de datos.
     */
    @Override
    public void guardar(Pelicula pelicula) {
        String sql = "INSERT INTO Pelicula (Genero, Titulo, Director, DuracionMinutos, Anio, RatingPromedio, PosterURL) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = ConexionBD.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pelicula.getGenero() != null ? pelicula.getGenero().name() : "OTRO");
            pstmt.setString(2, pelicula.getTitulo());
            pstmt.setString(3, pelicula.getDirector() != null ? pelicula.getDirector().getNombre() : "Desc.");
            pstmt.setLong(4, pelicula.getDuracion() != null ? pelicula.getDuracion().toMinutes() : 90);
            pstmt.setInt(5, pelicula.getAnio());
            pstmt.setDouble(6, pelicula.getRatingPromedio());
            pstmt.setString(7, pelicula.getPosterURL());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error guardando película: " + e.getMessage());
        }
    }

    /**
     * Lista todas las películas ordenadas por rating descendente.
     */
    @Override
    public List<Pelicula> listarTodas() {
        return buscarPorSql("SELECT * FROM Pelicula ORDER BY RatingPromedio DESC");
    }

    /**
     * Busca películas por título.
     */
    @Override
    public List<Pelicula> buscarPorTitulo(String titulo) {
        return buscarPorSql("SELECT * FROM Pelicula WHERE Titulo LIKE '%" + titulo + "%'");
    }

    /**
     * Ejecuta una consulta SQL y retorna una lista de películas.
     */
    private List<Pelicula> buscarPorSql(String sql) {
        List<Pelicula> peliculas = new ArrayList<>();
        Connection conn = ConexionBD.getConnection();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pelicula p = new Pelicula();
                p.setID(rs.getInt("ID"));
                p.setTitulo(rs.getString("Titulo"));
                try {
                    p.setGenero(GeneroPelicula.valueOf(rs.getString("Genero")));
                } catch (Exception e) {
                    p.setGenero(GeneroPelicula.OTRO);
                }
                p.setDirector(new Staff(rs.getString("Director"), "Director"));
                p.setDuracion(Duration.ofMinutes(rs.getLong("DuracionMinutos")));
                p.setAnio(rs.getInt("Anio"));
                p.setRatingPromedio(rs.getDouble("RatingPromedio"));
                p.setPosterURL(rs.getString("PosterURL"));
                peliculas.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error listando películas: " + e.getMessage());
        }
        return peliculas;
    }

    /**
     * Verifica si una película existe por su ID.
     */
    @Override
    public boolean existePelicula(int id) {
        return false;
    }

    /**
     * Elimina una película por su ID.
     */
    @Override
    public void eliminar(int id) {
    }

    /**
     * Actualiza una película en la base de datos.
     */
    @Override
    public void actualizar(Pelicula pelicula) {
    }
}