package servicio;

import java.util.List;

import comparador.ComparadorPeliculaGenero;
import comparador.ComparadorPeliculaTitulo;
import comparador.ComparadorUsuarioEmail;
import comparador.ComparadorUsuarioNombre;
import dao.PeliculaDAO;
import dao.PeliculaDAOimple;
import dao.ReseñaDAO;
import dao.ReseñaDAOimple;
import dao.UsuarioDAO;
import dao.UsuarioDAOimple;
import database.ConexionBD;
import modelo.Cliente;
import modelo.Pelicula;
import modelo.Reseña;
import modelo.Usuario;


public class AppImple{
    private UsuarioDAO usuarioDAO;
    private PeliculaDAO peliculaDAO;
    private ReseñaDAO reseñaDAO;
    public AppImple() {
        this.usuarioDAO = new UsuarioDAOimple();
        this.peliculaDAO = new PeliculaDAOimple();
        this.reseñaDAO = new ReseñaDAOimple();
        ConexionBD.getConnection();
    }
    
    public void registrarCliente(Cliente cliente) {
        System.out.println("\n--- Registrando Cliente ---");
        if (this.usuarioDAO.guardar(cliente)) {
            System.out.println("Cliente registrado exitosamente.");
        } else {
            System.out.println("No se pudo registrar al cliente (el email ya podría existir).");
        }
    }

    public void listarPeliculas(int orden) {
        List<Pelicula> peliculas = this.peliculaDAO.listarTodas();
        if (peliculas.isEmpty()) {
            System.out.println("No hay películas registradas.");
            return;
        }
        switch(orden) {
            case 1:
                peliculas.sort(new ComparadorPeliculaTitulo());
            break;
            case 2:
                peliculas.sort(new ComparadorPeliculaGenero());
            break;
        }
        System.out.println("\n--- Listado de Películas ---");
        for (Pelicula p : peliculas) {
            System.out.printf("Título: %s | Género: %s \n", p.getTitulo(), p.getGenero());
        }
    }

    
    public void listarUsuarios(int orden) {
        List<Usuario> usuarios = this.usuarioDAO.listarTodos();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        switch(orden) {
            case 1:
                usuarios.sort(new ComparadorUsuarioNombre());
            break;
            case 2:
                usuarios.sort(new ComparadorUsuarioEmail());
            break;
        }
        System.out.println("\n--- Listado de Usuarios ---");
        for (Usuario u : usuarios) {
            System.out.printf("Nombre: %s | Email: %s\n", u.getNombre(), u.getEmail());
        }
    }

    
    public void registrarResena(Reseña reseña) {
        this.reseñaDAO.guardar(reseña);
    }
    
    public boolean verificarDNI(long dni) {
        return this.usuarioDAO.dniExiste(dni);
    }

    public Usuario login(String email, String password) {
       Usuario usuarioLogueado = usuarioDAO.autenticar(email, password);
        if (usuarioLogueado != null) {
            System.out.println("Login exitoso. Bienvenido " + usuarioLogueado.getNombre());
        } else {
            System.out.println("Error: email o contraseña incorrectos.");
        }
        return usuarioLogueado;
    }

    public List<Pelicula> peliculasParaBienvenida(Cliente cliente) {
        if (!clienteTieneResenas(cliente.getID())) {
            return this.peliculaDAO.peliculasTop10Rank();
        } else {
            return this.peliculaDAO.peliculas10Random();
        }
    }

    private boolean clienteTieneResenas(int clienteID) {
        return this.reseñaDAO.existenResenasPorCliente(clienteID);
    }   
}