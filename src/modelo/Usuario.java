package modelo;

public abstract class Usuario {
    private int ID;
    private Long DNI;
    private String nombre;
    private String apellido;
    private String email;
    private String contrasena;
    private boolean vistoTop10 = false;

    public Usuario() {
    }

    public Usuario(Long DNI, String nombre, String apellido, String email, String contrasena) {
        this.DNI = DNI;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Long getDNI() {
        return DNI;
    }

    public void setDNI(Long DNI) {
        this.DNI = DNI;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getApellido() {
        return apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getContrasena() {
        return contrasena;
    }

    public boolean isVistoTop10() {
        return vistoTop10;
    }

    public void setVistoTop10(boolean vistoTop10) {
        this.vistoTop10 = vistoTop10;
    }

}
