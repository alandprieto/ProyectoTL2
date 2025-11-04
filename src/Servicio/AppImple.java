package Servicio;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import Catalogo.Pelicula;
import Catalogo.Reseña;
import Catalogo.Staff;

import ENUM.GeneroPelicula;

import Usuario.Administrador;
import Usuario.Cliente;
import Usuario.Usuario;

import DataBase.ConexionBD;

import DAO.PeliculaDAO;
import DAO.ReseñaDAO;
import DAO.UsuarioDAO;
import DAO.PeliculaDAOimple;
import DAO.ReseñaDAOimple;
import DAO.UsuarioDAOimple;


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

    // --- MÉTODOS MODIFICADOS ---
    
    @Override
    // Ahora recibe el objeto Cliente directamente
    public void registrarCliente(Cliente cliente) {
        System.out.println("\n--- Registrando Cliente ---");
        // La lógica de solicitar datos se fue. Solo se guarda.
        if (this.usuarioDAO.guardar(cliente)) {
            System.out.println("Cliente registrado exitosamente.");
        } else {
            System.out.println("No se pudo registrar al cliente (el email ya podría existir).");
        }
    }

    @Override
    // Ahora recibe el objeto Administrador directamente
    public void registrarAdmin(Administrador admin) {
        System.out.println("\n--- Registrando Administrador ---");
        // La lógica de solicitar token y datos se fue. Solo se guarda.
        if (this.usuarioDAO.guardar(admin)) {
            System.out.println("Administrador registrado exitosamente.");
        } else {
            System.out.println("No se pudo registrar al administrador (el email ya podría existir).");
        }
    }

    // Se elimina el método duplicado private Usuario solicitarDatosUsuario(Scanner scanner)
    // Esa lógica ahora está solo en AppCargaDatos.java

    
    // --- MÉTODOS SIN CAMBIOS ---
    // (Estos métodos mantienen su propia lógica de solicitud de datos por ahora)

    @Override
    public void cargarPelicula() {
        Scanner scanner = new Scanner(System.in);
        Usuario admin = autenticarUsuarioPorRol(Administrador.class);
        if (admin == null) {
            scanner.close();
            return; 
        }

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
                peliculas.sort(Comparator.comparing(Pelicula::getTitulo));
            break;
            case 2:
                peliculas.sort(Comparator.comparing(p -> p.getGenero().name()));
            break;
            case 3:
                peliculas.sort(Comparator.comparingInt(p -> (int) p.getDuracion().toMinutes()));
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
                usuarios.sort(Comparator.comparing(Usuario::getNombre));
            break;
            case 2:
                usuarios.sort(Comparator.comparing(Usuario::getEmail));
            break;
        }
        System.out.println("\n--- Listado de Usuarios ---");
        for (Usuario u : usuarios) {
            System.out.printf("Nombre: %s | Email: %s\n", u.getNombre(), u.getEmail());
        }
        scanner.close();
    }

    @Override
    public void registrarResena() {
        Scanner scanner = new Scanner(System.in);
        Usuario usuario = autenticarUsuarioPorRol(Cliente.class);
        if (usuario == null) {
            scanner.close();
            return; 
        }

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
        Usuario admin = autenticarUsuarioPorRol(Administrador.class);
        if (admin == null) {
            scanner.close();
            return; 
        }

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

    private Usuario autenticarUsuarioPorRol(Class<? extends Usuario> tipoUsuarioEsperado) { 
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n--- Autenticación de Usuario Requerida ---");
        System.out.print("Ingrese su email: ");
        String email = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String contrasena = scanner.nextLine();
        Usuario usuario = this.usuarioDAO.autenticar(email, contrasena);
        if (usuario == null) {
            System.out.println("Error: Credenciales incorrectas.");
            scanner.close();
            return null;
        }
        if (!tipoUsuarioEsperado.isInstance(usuario)) {
            System.out.println("Error: Permisos insuficientes para esta acción.");
            scanner.close();
            return null;
        }
        System.out.println("Autenticación exitosa. ¡Bienvenido, " + usuario.getNombre() + "!");
        scanner.close();
        return usuario;
    }
}