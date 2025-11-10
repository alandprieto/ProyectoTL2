package servicio;

import java.util.List;

import comparador.ComparadorPeliculaDuracion;
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
import modelo.Administrador;
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

    public void registrarAdmin(Administrador admin) {
        System.out.println("\n--- Registrando Administrador ---");
        if (this.usuarioDAO.guardar(admin)) {
            System.out.println("Administrador registrado exitosamente.");
        } else {
            System.out.println("No se pudo registrar al administrador (el email ya podría existir).");
        }
    }

    
    public void cargarPelicula(Pelicula pelicula) {
        this.peliculaDAO.guardar(pelicula);
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
            case 3:
                peliculas.sort(new ComparadorPeliculaDuracion());
            break;
        }
        System.out.println("\n--- Listado de Películas ---");
        for (Pelicula p : peliculas) {
            System.out.printf("Título: %s | Género: %s | Duración: %d min\n", p.getTitulo(), p.getGenero(), p.getDuracion().toMinutes());
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

    public void listarResenias() {
        List<Reseña> reseñas = this.reseñaDAO.listarNoAprobadas();
        if (reseñas.isEmpty()) {
            System.out.println("No hay reseñas registradas.");
            return;
        }
        System.out.println("\n--- Listado de Reseñas ---");
        for (Reseña r : reseñas) {
            System.out.printf("ID: %d | Usuario: %s | Película ID: %d | Comentario: %s | Puntaje: %d\n", r.getID(), r.getUsuario().getNombre(), r.getIDContenido(), r.getComentario(), r.getCalificacion());
        }
    }
    
    
    public void aprobarResena(int resenaID) {
        this.reseñaDAO.aprobarResenia(resenaID);
    }

    public boolean verificarDNI(long dni) {
        return this.usuarioDAO.dniExiste(dni);
    }

}