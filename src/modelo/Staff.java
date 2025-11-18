package modelo;

public class Staff {
    private String nombre;
    private String rol; 

    public Staff(String nombre, String rol) {
        this.nombre = nombre;
        this.rol = rol;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
