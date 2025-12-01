package comparador;

import java.util.Comparator;
import modelo.Pelicula;

/**
 * Comparador de películas por duración en orden ascendente.
 */
public class ComparadorPeliculaDuracion implements Comparator<Pelicula> {
    /**
     * Compara dos películas por su duración.
     */
    @Override
    public int compare(Pelicula p1, Pelicula p2) {
        long duracion1 = p1.getDuracion().toMinutes();
        long duracion2 = p2.getDuracion().toMinutes();
        return Long.compare(duracion1, duracion2);
    }
}