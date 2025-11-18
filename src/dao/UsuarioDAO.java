package dao;

import java.util.List;

import modelo.Usuario;

public interface UsuarioDAO {
    boolean guardar(Usuario usuario);
    Usuario buscarPorId(int id);
    List<Usuario> listarTodos();
    void eliminar(int id);
    Usuario autenticar(String email, String contrasena);
    boolean dniExiste(long dni);
}
