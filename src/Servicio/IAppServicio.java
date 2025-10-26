package Servicio;

import java.util.Scanner;

public interface IAppServicio {

    void registrarCliente(Scanner scanner);
    
    void registrarAdmin(Scanner scanner);
    
    void cargarPelicula(Scanner scanner);
    
    void listarPeliculas(Scanner scanner);
    
    void listarUsuarios(Scanner scanner);
    
    void registrarResena(Scanner scanner);
    
    void aprobarResena(Scanner scanner);
}