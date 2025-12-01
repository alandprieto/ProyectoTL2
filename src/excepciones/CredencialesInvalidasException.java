package excepciones;

/**
 * Excepción lanzada cuando las credenciales de usuario son inválidas.
 */
public class CredencialesInvalidasException extends Exception {
    /**
     * Constructor con mensaje de error.
     */
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}