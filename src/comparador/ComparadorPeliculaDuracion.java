package comparador;

import java.util.Comparator;
import modelo.Pelicula;

public class ComparadorPeliculaDuracion implements Comparator<Pelicula> {
    @Override
    public int compare(Pelicula p1, Pelicula p2) {
        long duracion1 = p1.getDuracion().toMinutes();
        long duracion2 = p2.getDuracion().toMinutes();
        return Long.compare(duracion1, duracion2);
    }
}