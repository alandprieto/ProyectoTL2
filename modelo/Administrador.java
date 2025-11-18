package modelo;

public class Administrador extends Usuario {

    public Administrador() {
        super();
    }

    public Administrador(Long DNI, String nombre, String apellido, String email, String contrasena) {
        super(DNI, nombre, apellido, email, contrasena);
    }

}
