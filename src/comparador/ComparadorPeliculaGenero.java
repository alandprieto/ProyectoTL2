package comparador;

import java.util.Comparator;
import modelo.Pelicula;

/**
 * Comparador de películas por género en orden alfabético.
 */
public class ComparadorPeliculaGenero implements Comparator<Pelicula> {
    /**
     * Compara dos películas por su género.
     */
    @Override
    public int compare(Pelicula p1, Pelicula p2) {
        return p1.getGenero().name().compareTo(p2.getGenero().name());
    }
}