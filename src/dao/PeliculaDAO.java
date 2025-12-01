package dao;

import java.util.List;

import modelo.Pelicula;

/**
 * Interfaz DAO para operaciones de Pelicula.
 */
public interface PeliculaDAO {
    /**
     * Guarda una nueva película en la base de datos.
     */
    void guardar(Pelicula pelicula);

    /**
     * Lista todas las películas de la base de datos.
     */
    List<Pelicula> listarTodas();

    /**
     * Verifica si una película existe por su ID.
     */
    boolean existePelicula(int id);

    /**
     * Elimina una película por su ID.
     */
    void eliminar(int id);

    /**
     * Actualiza una película en la base de datos.
     */
    void actualizar(Pelicula pelicula);

    /**
     * Busca películas por título.
     */
    List<Pelicula> buscarPorTitulo(String titulo);
}