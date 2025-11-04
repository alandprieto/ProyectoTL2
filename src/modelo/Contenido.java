package modelo;
import java.util.List;

import enums.GeneroPelicula;

import java.nio.file.Path;

public abstract class Contenido {
    private String titulo;
    private GeneroPelicula genero;
    private String sinopsis;
    private Staff director;
    private double puntaje;
    private int vistas;
    private Path video;
    private List<Reseña> reseñas;
    private int ID;

    public Contenido() {
    }

    public Contenido(String titulo, GeneroPelicula genero, String sinopsis, Staff director, Double puntaje, int vistas, Path video, List<Reseña> reseñas, int ID) {
        this.titulo = titulo;
        this.genero = genero;
        this.sinopsis = sinopsis;
        this.director = director;
        this.puntaje = puntaje;
        this.vistas = vistas;
        this.video = video;
        this.reseñas = reseñas;
        this.ID = ID;
    }

    // Getters y Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getSinopsis() { return sinopsis; }
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }

    //public List<Staff> getElenco() { return elenco; }
    //public void setElenco(List<Staff> elenco) { this.elenco = elenco; }

    public Staff getDirector() { return director; }
    public void setDirector(Staff director) { this.director = director; }

    // public Audio[] getIdioma() { return idioma; }
    // public void setIdioma(Audio[] idioma) { this.idioma = idioma; }

    // public List<Subtitulos> getSubtitulosDisponibles() { return subtitulosDisponibles; }
    // public void setSubtitulosDisponibles(List<Subtitulos> subtitulosDisponibles) { this.subtitulosDisponibles = subtitulosDisponibles; }

    public double getPuntaje() { return puntaje; }
    public void setPuntaje() {
        if (reseñas == null || reseñas.isEmpty()) {
            this.puntaje = 0.0;
            return;
        }
        double sum = 0;
        for (Reseña reseña : reseñas) {
            sum += reseña.getCalificacion();
        }
        this.puntaje = sum / reseñas.size();
    }

    public int getVistas() { return vistas; }
    public void setVistas(int vistas) { this.vistas = vistas; }

    public Path getVideo() { return video; }
    public void setVideo(Path video) { this.video = video; }

    public List<Reseña> getReseñas() { return reseñas; }
    public void setReseñas(List<Reseña> reseñas) { this.reseñas = reseñas; }

    public GeneroPelicula getGenero() { return genero; }
    public void setGenero(GeneroPelicula genero) { this.genero = genero; }

    public int getID() { return ID; }
    public void setID(int iD) { ID = iD; }
    
}