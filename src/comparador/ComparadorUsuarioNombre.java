package comparador;

import java.util.Comparator;
import modelo.Usuario;

/**
 * Comparador de usuarios por nombre en orden alfabético.
 */
public class ComparadorUsuarioNombre implements Comparator<Usuario> {
    /**
     * Compara dos usuarios por su nombre.
     */
    @Override
    public int compare(Usuario u1, Usuario u2) {
        return u1.getNombre().compareTo(u2.getNombre());
    }
}