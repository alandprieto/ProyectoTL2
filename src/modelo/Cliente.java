package modelo;

public class Cliente extends Usuario {

    public Cliente() {
        super();
    }

    public Cliente(Long DNI, String nombre, String apellido, String email, String contrasena) {
        super(DNI, nombre, apellido, email, contrasena);
    }

    /*private Domicilio domicilio;

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    } */

}
