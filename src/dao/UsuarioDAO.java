package dao;

import java.util.List;

import modelo.Usuario;

/**
 * Interfaz DAO para operaciones de Usuario.
 */
public interface UsuarioDAO {
    /**
     * Guarda un nuevo usuario en la base de datos.
     */
    boolean guardar(Usuario usuario);

    /**
     * Busca un usuario por su ID.
     */
    Usuario buscarPorId(int id);

    /**
     * Lista todos los usuarios de la base de datos.
     */
    List<Usuario> listarTodos();

    /**
     * Elimina un usuario por su ID.
     */
    void eliminar(int id);

    /**
     * Autentica un usuario con email y contraseña.
     */
    Usuario autenticar(String email, String contrasena);

    /**
     * Verifica si un DNI ya existe en la base de datos.
     */
    boolean dniExiste(long dni);

    /**
     * Verifica si un email ya existe en la base de datos.
     */
    boolean emailExiste(String email);

    /**
     * Verifica si el usuario ya vio el Top 10 inicial.
     */
    boolean haVistoTop10(int id);

    /**
     * Marca que el usuario vio el Top 10.
     */
    void marcarVioTop10(int id);
}
