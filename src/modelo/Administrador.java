package modelo;

public class Administrador extends Usuario {

    /**
     * Constructor vacío.
     */
    public Administrador() {
        super();
    }

    /**
     * Constructor con parámetros de administrador.
     */
    public Administrador(Long DNI, String nombre, String apellido, String email, String contrasena) {
        super(DNI, nombre, apellido, email, contrasena);
    }
}
