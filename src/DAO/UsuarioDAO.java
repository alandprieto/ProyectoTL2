package DAO;

import Usuario.Usuario;
import java.util.List;

public interface UsuarioDAO {
    boolean guardar(Usuario usuario);
    Usuario buscarPorId(int id);
    List<Usuario> listarTodos();
    void eliminar(int id);
    Usuario autenticar(String email, String contrasena);
}
