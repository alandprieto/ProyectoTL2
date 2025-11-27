package dao;

import java.util.List;

import modelo.Pelicula;

public interface PeliculaDAO {
    void guardar(Pelicula pelicula);

    List<Pelicula> listarTodas();

    boolean existePelicula(int id);

    void eliminar(int id);

    void actualizar(Pelicula pelicula);

    List<Pelicula> buscarPorTitulo(String titulo);
}