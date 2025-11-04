package servicio;

import modelo.Usuario;
import modelo.Administrador;
import modelo.Cliente;

public interface AppServicio {

    void registrarCliente(Cliente cliente);
    
    void registrarAdmin(Administrador admin);
    
    void cargarPelicula();
    
    void listarPeliculas();
    
    void listarUsuarios();
    
    void registrarResena(Usuario cliente);
    
    void aprobarResena();
}