package DAO;

import java.util.List;  
import Catalogo.Pelicula;

public interface PeliculaDAO {
    void guardar(Pelicula pelicula);
    List<Pelicula> listarTodas();
    boolean existePelicula(int id);
    void eliminar(int id);
    void actualizar(Pelicula pelicula);
}