package comparador;

import java.util.Comparator;
import modelo.Pelicula;

public class ComparadorPeliculaGenero implements Comparator<Pelicula> {
    @Override
    public int compare(Pelicula p1, Pelicula p2) {
        return p1.getGenero().name().compareTo(p2.getGenero().name());
    }
}