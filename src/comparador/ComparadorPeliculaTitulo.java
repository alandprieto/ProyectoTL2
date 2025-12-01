package comparador;

import java.util.Comparator;
import modelo.Pelicula;

/**
 * Comparador de películas por título en orden alfabético.
 */
public class ComparadorPeliculaTitulo implements Comparator<Pelicula> {
    /**
     * Compara dos películas por su título.
     */
    @Override
    public int compare(Pelicula p1, Pelicula p2) {
        return p1.getTitulo().compareTo(p2.getTitulo());
    }
}