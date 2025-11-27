package dao;

import java.util.List;

import modelo.Reseña;

public interface ReseñaDAO {
    void guardar(Reseña resenia);
    List<Reseña> listarNoAprobadas();
    void aprobarResenia(int id);
    void eliminar(int idResenia);
    boolean existeResena(int idUsuario, int idPelicula);
}
