package excepciones;

/**
 * Excepción lanzada cuando un dato ingresado es inválido.
 */
public class DatoInvalidoException extends Exception {
    /**
     * Constructor con mensaje de error.
     */
    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}