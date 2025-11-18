package comparador;

import java.util.Comparator;
import modelo.Usuario;

public class ComparadorUsuarioEmail implements Comparator<Usuario> {
    @Override
    public int compare(Usuario u1, Usuario u2) {
        return u1.getEmail().compareTo(u2.getEmail());
    }
}