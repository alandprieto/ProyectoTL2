package modelo;

public abstract class Usuario {
    private int ID;
    private Long DNI;
    private String nombre;
    private String apellido;
    private String email;
    private String contrasena;
    private boolean vistoTop10 = false;

    /**
     * Constructor vacío.
     */
    public Usuario() {
    }

    /**
     * Constructor con parámetros de usuario.
     */
    public Usuario(Long DNI, String nombre, String apellido, String email, String contrasena) {
        this.DNI = DNI;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
    }

    /**
     * Obtiene el ID del usuario.
     */
    public int getID() {
        return ID;
    }

    /**
     * Establece el ID del usuario.
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Obtiene el DNI del usuario.
     */
    public Long getDNI() {
        return DNI;
    }

    /**
     * Establece el DNI del usuario.
     */
    public void setDNI(Long DNI) {
        this.DNI = DNI;
    }

    /**
     * Establece el nombre del usuario.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el apellido del usuario.
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene el apellido del usuario.
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Establece el email del usuario.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el email del usuario.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece la contraseña del usuario.
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Obtiene la contraseña del usuario.
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Verifica si el usuario ya vio el Top 10.
     */
    public boolean isVistoTop10() {
        return vistoTop10;
    }

    /**
     * Establece si el usuario vio el Top 10.
     */
    public void setVistoTop10(boolean vistoTop10) {
        this.vistoTop10 = vistoTop10;
    }
}
