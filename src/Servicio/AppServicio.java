package Servicio;

import Usuario.Administrador;
import Usuario.Cliente;

public interface AppServicio {

    void registrarCliente(Cliente cliente);
    
    void registrarAdmin(Administrador admin);
    
    void cargarPelicula();
    
    void listarPeliculas();
    
    void listarUsuarios();
    
    void registrarResena();
    
    void aprobarResena();
}