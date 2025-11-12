package modelo;

import enums.GeneroPelicula;


public class Pelicula extends Contenido {
    //private Duration duracion;

    public Pelicula(String titulo, GeneroPelicula genero, String sinopsis, double puntaje, int ID, String poster, int anio) {
        super(titulo, genero, sinopsis, puntaje, ID, poster, anio);
        //this.duracion = duracion;
    }

    public Pelicula() {
        super();
    }

    // Getters y Setters
    //public Duration getDuracion() { return duracion }

    //public void setDuracion(Duration duracion) {this.duracion = duracion }
}