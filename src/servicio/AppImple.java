package servicio;

import java.time.Duration;
import java.util.List;
import java.util.Scanner;

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
import enums.GeneroPelicula;
import modelo.Administrador;
import modelo.Cliente;
import modelo.Pelicula;
import modelo.Reseña;
import modelo.Staff;
import modelo.Usuario;


public class AppImple implements AppServicio {
    private UsuarioDAO usuarioDAO;
    private PeliculaDAO peliculaDAO;
    private ReseñaDAO reseñaDAO;
    public AppImple() {
        this.usuarioDAO = new UsuarioDAOimple();
        this.peliculaDAO = new PeliculaDAOimple();
        this.reseñaDAO = new ReseñaDAOimple();
        ConexionBD.getConnection();
    }
    
    @Override
    public void registrarCliente(Cliente cliente) {
        System.out.println("\n--- Registrando Cliente ---");
        if (this.usuarioDAO.guardar(cliente)) {
            System.out.println("Cliente registrado exitosamente.");
        } else {
            System.out.println("No se pudo registrar al cliente (el email ya podría existir).");
        }
    }

    @Override
    public void registrarAdmin(Administrador admin) {
        System.out.println("\n--- Registrando Administrador ---");
        if (this.usuarioDAO.guardar(admin)) {
            System.out.println("Administrador registrado exitosamente.");
        } else {
            System.out.println("No se pudo registrar al administrador (el email ya podría existir).");
        }
    }

    @Override
    public void cargarPelicula() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n--- Carga de Nueva Película ---");
        System.out.print("Ingrese título de la película: ");
        String titulo = scanner.nextLine();
        System.out.print("Ingrese género (1: ACCION, 2: COMEDIA, 3: DRAMA, 4: CIENCIA_FICCION): ");
        int generoInput = Integer.parseInt(scanner.nextLine());
        System.out.print("Ingrese nombre del director: ");
        String directorNombre = scanner.nextLine();
        Staff director = new Staff(directorNombre, "Director");
        GeneroPelicula genero = GeneroPelicula.values()[generoInput - 1];
        System.out.print("Ingrese duración en minutos: ");
        Duration duracion = Duration.ofMinutes(Integer.parseInt(scanner.nextLine()));
        Pelicula pelicula = new Pelicula(titulo, genero, "", director, 0.0, 0, null, null, 0, duracion);
        System.out.printf("Titulo: %s | Género: %s | Director: %s | Duración: %d min\n", pelicula.getTitulo(), pelicula.getGenero(), pelicula.getDirector().getNombre(), pelicula.getDuracion().toMinutes());
        System.out.println("¿La Película es correcta? [Y/N] "); 
        String confirmacion = scanner.nextLine().trim().toUpperCase();
        if (!confirmacion.equals("Y")) {
            System.out.println("Operación cancelada. La película no fue cargada.");
            scanner.close();
            return;
        }
        this.peliculaDAO.guardar(pelicula);
        scanner.close();
    }

    @Override
    public void listarPeliculas() {
        Scanner scanner = new Scanner(System.in);
        List<Pelicula> peliculas = this.peliculaDAO.listarTodas();
        if (peliculas.isEmpty()) {
            System.out.println("No hay películas registradas.");
            scanner.close();
            return;
        }
        System.out.print("Ordenar por (1: Título, 2: Género, 3: Duración): ");
        int orden = Integer.parseInt(scanner.nextLine());
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
        scanner.close();
    }

    @Override
    public void listarUsuarios() {
        Scanner scanner = new Scanner(System.in);
        List<Usuario> usuarios = this.usuarioDAO.listarTodos();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            scanner.close();
            return;
        }
        System.out.print("Ordenar por (1: Nombre, 2: Email): ");
        int orden = Integer.parseInt(scanner.nextLine());
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
        scanner.close();
    }

    @Override
    public void registrarResena(Usuario usuario) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Registro de Nueva Reseña ---");
        List<Pelicula> peliculas = this.peliculaDAO.listarTodas();
        if (peliculas.isEmpty()) {
            System.out.println("No hay películas cargadas en el sistema.");
            scanner.close();
            return;
        }
        for (Pelicula p : peliculas) {
            System.out.printf("ID: %d - Título: %s\n", p.getID(), p.getTitulo());
        }
        int peliculaID;
        do {
            System.out.print("Ingrese un ID de pelicula valido para resenar: ");
            peliculaID = Integer.parseInt(scanner.nextLine());
        } while (!this.peliculaDAO.existePelicula(peliculaID));
        System.out.print("Ingrese comentario: ");
        String comentario = scanner.nextLine();
        int puntaje;
        do {
            System.out.print("Ingrese puntaje (1-5): ");
            puntaje = Integer.parseInt(scanner.nextLine());
            if (puntaje < 1 || puntaje > 5) {
                System.out.println("Error: El puntaje debe ser un número entre 1 y 5.");
            }
        } while (puntaje < 1 || puntaje > 5);
        Reseña reseña = new Reseña(usuario, peliculaID, comentario, puntaje);
        System.out.println("Reseña a registrar:");
        System.out.printf("ID: %d | Usuario ID: %d | Película ID: %d | Comentario: %s | Puntaje: %d\n", reseña.getID(), reseña.getUsuario(), reseña.getIDContenido(), reseña.getComentario(), reseña.getCalificacion());
        System.out.println("¿La Reseña es correcta? [Y/N] ");
        String confirmacion = scanner.nextLine().trim().toUpperCase();
        if (!confirmacion.equals("Y")) {
            System.out.println("Operación cancelada. La reseña no fue registrada.");
            scanner.close();
            return;
        }
        this.reseñaDAO.guardar(reseña);
        scanner.close();
    }

    @Override
    public void aprobarResena() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Aprobación de Reseñas ---");
        List<Reseña> reseniasNoAprobadas = this.reseñaDAO.listarNoAprobadas();
        if (reseniasNoAprobadas.isEmpty()) {
            System.out.println("No hay reseñas pendientes de aprobación.");
            scanner.close();
            return;
        }
        System.out.println("Reseñas pendientes:");
        for (Reseña r : reseniasNoAprobadas) {
            System.out.printf("ID: %d | Usuario: %s (ID: %d) | Película ID: %d | Comentario: %s | Puntaje: %d\n", r.getID(), r.getUsuario().getNombre(), r.getUsuario().getID(), r.getIDContenido(), r.getComentario(), r.getCalificacion());
        }
        System.out.print("Ingrese ID de la reseña a aprobar: ");
        int resenaID = Integer.parseInt(scanner.nextLine());
        this.reseñaDAO.aprobarResenia(resenaID);
        scanner.close();
    }

}