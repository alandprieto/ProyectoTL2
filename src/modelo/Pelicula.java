package modelo;

import java.time.Duration;
import java.util.List;

import enums.GeneroPelicula;

import java.nio.file.Path;

public class Pelicula extends Contenido {
    private Duration duracion;

    public Pelicula(String titulo, GeneroPelicula genero, String sinopsis, Staff director, double puntaje, int vistas, Path video, List<Reseña> reseñas, int ID, Duration duracion) {
        super(titulo, genero, sinopsis, director, puntaje, vistas, video, reseñas, ID);
        this.duracion = duracion;
    }

    public Pelicula() {
        super();
    }

    // Getters y Setters
    public Duration getDuracion() {
        return duracion;
    }

    public void setDuracion(Duration duracion) {
        this.duracion = duracion;
    }
}