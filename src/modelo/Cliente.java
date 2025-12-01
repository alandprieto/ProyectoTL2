package modelo;

public class Cliente extends Usuario {

    /**
     * Constructor vacío.
     */
    public Cliente() {
        super();
    }

    /**
     * Constructor con parámetros de cliente.
     */
    public Cliente(Long DNI, String nombre, String apellido, String email, String contrasena) {
        super(DNI, nombre, apellido, email, contrasena);
    }
}
