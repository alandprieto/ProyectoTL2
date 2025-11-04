package modelo;

public class Reseña {
    private int ID;
    private String comentario;
    private int calificacion; 
    private Usuario usuario; 
    private int IDContenido;

    public Reseña() { }

    public Reseña(Usuario usuario, int IDContenido, String comentario, int calificacion) {
        this.usuario = usuario;
        this.IDContenido = IDContenido;
        this.comentario = comentario;
        this.calificacion = calificacion;
    }
    
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getComentario() {
        return comentario;
    }
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getCalificacion() {
        return calificacion;
    }
    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getIDContenido() {
        return IDContenido;
    }
    public void setIDContenido(int iDContenido) {
        IDContenido = iDContenido;
    }
}
