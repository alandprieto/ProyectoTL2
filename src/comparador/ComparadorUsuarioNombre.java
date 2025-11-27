package comparador;

import java.util.Comparator;
import modelo.Usuario;

public class ComparadorUsuarioNombre implements Comparator<Usuario> {
    @Override
    public int compare(Usuario u1, Usuario u2) {
        return u1.getNombre().compareTo(u2.getNombre());
    }
}