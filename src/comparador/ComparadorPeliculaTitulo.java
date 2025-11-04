package comparador;

import java.util.Comparator;
import modelo.Pelicula;

public class ComparadorPeliculaTitulo implements Comparator<Pelicula> {
    @Override
    public int compare(Pelicula p1, Pelicula p2) {
        return p1.getTitulo().compareTo(p2.getTitulo());
    }
}