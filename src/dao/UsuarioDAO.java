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

    boolean emailExiste(String email);

    // Marca si el usuario ya vio el Top10 inicial
    boolean haVistoTop10(int id);

    // Marca que el usuario ya vio el Top10
    void marcarVioTop10(int id);
}
