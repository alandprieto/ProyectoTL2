package Catalogo;
import java.util.List;

import ENUM.GeneroPelicula;

import java.nio.file.Path;

/**
 * Representa una pieza de contenido audiovisual genérico y abstracto.
 * Funciona como clase base para tipos de contenido más específicos como series o películas.
 *
 * @author Alan Prieto/Natalia Priore
 * @version 1.1
 */
public abstract class Contenido {
    /** Título del contenido. */
    private String titulo;
    /** Género del contenido (específico para películas). */
    private GeneroPelicula genero;
    /** Breve descripción o resumen de la trama. */
    private String sinopsis;
    /** Lista de personas que forman parte del elenco. */
    //private List<Staff> elenco;
    /** La persona que dirigió el contenido. */
    private Staff director;
    /** Array de los idiomas de audio disponibles. */
    // private Audio []idioma; // Comentado temporalmente
    /** Lista de los subtítulos disponibles para el contenido. */
    // private List<Subtitulos> subtitulosDisponibles; // Comentado temporalmente
    /** Puntuación promedio del contenido, en una escala de 1 a 5. */
    private double puntaje; // {1..5}
    /** Contador del número de reproducciones. */
    private int vistas;
    /** Ruta al archivo de video del contenido. */
    private Path video;
    /** Lista de reseñas escritas por los usuarios. */
    private List<Reseña> reseñas;
    /** ID del contenido en la base de datos. */
    private int ID;

    public Contenido() {
        // Constructor vacío
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
    public void setPuntaje(double puntaje) { this.puntaje = puntaje; }

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

    /**
     * Devuelve la puntuación actual del contenido.
     * @return El puntaje promedio del contenido.
     */
    public double obtenerPuntaje() { return puntaje; }
    
    /**
     * Busca y devuelve una reseña específica.
     * (Nota: La implementación actual es un prototipo).
     * @param r La reseña a buscar.
     * @return La reseña encontrada o null si no existe.
     */
    // public Reseña obtenerReseña(Reseña r) { /* implementación */ return null; }
}