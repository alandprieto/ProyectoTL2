package modelo;

public class Staff {
    private String nombre;
    private String rol; 

    /**
     * Constructor con parámetros de Staff.
     */
    public Staff(String nombre, String rol) {
        this.nombre = nombre;
        this.rol = rol;
    }

    /**
     * Obtiene el nombre del Staff.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del Staff.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el rol del Staff.
     */
    public String getRol() {
        return rol;
    }

    /**
     * Establece el rol del Staff.
     */
    public void setRol(String rol) {
        this.rol = rol;
    }
}
