package comparador;

import java.util.Comparator;
import modelo.Usuario;

/**
 * Comparador de usuarios por email en orden alfabético.
 */
public class ComparadorUsuarioEmail implements Comparator<Usuario> {
    /**
     * Compara dos usuarios por su email.
     */
    @Override
    public int compare(Usuario u1, Usuario u2) {
        return u1.getEmail().compareTo(u2.getEmail());
    }
}