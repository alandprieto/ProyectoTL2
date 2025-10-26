package DAO;

import java.util.List;  
import Catalogo.Reseña;

public interface ReseñaDAO {
    void guardar(Reseña resenia);
    List<Reseña> listarNoAprobadas();
    void aprobarResenia(int id);
    void eliminar(int idResenia);
}
