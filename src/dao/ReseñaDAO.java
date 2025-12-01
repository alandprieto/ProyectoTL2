package dao;

import java.util.List;

import modelo.Reseña;

/**
 * Interfaz DAO para operaciones de Reseña.
 */
public interface ReseñaDAO {
    /**
     * Guarda una nueva reseña en la base de datos.
     */
    void guardar(Reseña resenia);

    /**
     * Lista todas las reseñas no aprobadas.
     */
    List<Reseña> listarNoAprobadas();

    /**
     * Aprueba una reseña por su ID.
     */
    void aprobarResenia(int id);

    /**
     * Elimina una reseña por su ID.
     */
    void eliminar(int idResenia);

    /**
     * Verifica si existe una reseña de un usuario para una película.
     */
    boolean existeResena(int idUsuario, int idPelicula);
}
