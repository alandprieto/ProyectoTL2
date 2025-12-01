package excepciones;

/**
 * Excepción lanzada cuando se intenta registrar un usuario que ya existe.
 */
public class UsuarioYaExisteException extends Exception {
    /**
     * Constructor con mensaje de error.
     */
    public UsuarioYaExisteException(String mensaje) {
        super(mensaje);
    }
}