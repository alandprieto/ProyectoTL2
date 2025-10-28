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

public class AppImple implements IAppServicio {
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
    public void registrarCliente(Scanner scanner) {
        System.out.println("\n--- Registro de Nuevo Cliente ---");
        Usuario datosUsuario = solicitarDatosUsuario(scanner);
        if (datosUsuario == null) return;

        Cliente cliente = new Cliente(datosUsuario.getDNI(), datosUsuario.getNombre(), datosUsuario.getApellido(), datosUsuario.getEmail(), datosUsuario.getContrasena());
        if (this.usuarioDAO.guardar(cliente)) {
            System.out.println("Cliente registrado exitosamente.");
        } else {
            System.out.println("No se pudo registrar al cliente (el email ya podría existir).");
        }
    }

    @Override
    public void registrarAdmin(Scanner scanner, String TokenAdm) {
        System.out.print("Ingrese Token de validacion: ");
        String TokenValido = scanner.nextLine();
        if (!TokenValido.equals(TokenAdm)) { 
            System.out.println("Error: Token no valido"); 
            return;
        }
        else{
            System.out.println("\n--- Registro de Nuevo Administrador ---");
            Usuario datosUsuario = solicitarDatosUsuario(scanner);
            if (datosUsuario == null) return;

            Administrador admin = new Administrador(datosUsuario.getDNI(), datosUsuario.getNombre(), datosUsuario.getApellido(), datosUsuario.getEmail(), datosUsuario.getContrasena());
            if (this.usuarioDAO.guardar(admin)) {
                System.out.println("Administrador registrado exitosamente.");
            } else {
                System.out.println("No se pudo registrar al administrador (el email ya podría existir).");
            }
        }
    }

    private Usuario solicitarDatosUsuario(Scanner scanner) {
        long DNI = 0;
        while (true) {
            System.out.print("Ingrese DNI (sin puntos ni comas): ");
            try {
                DNI = Long.parseLong(scanner.nextLine());
                String dniStr = String.valueOf(DNI);
                if (DNI > 0 && dniStr.length() >= 7 && dniStr.length() <= 8) {
                    break;
                }
                System.out.println("Error: El DNI debe ser un número positivo de 7 u 8 dígitos.");
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, ingrese un número válido para el DNI.");
            }
        }

        String nombre;
        do {
            System.out.print("Ingrese nombre: ");
            nombre = scanner.nextLine();
            if (!nombre.matches("[a-zA-Z ]+")) {
                System.out.println("Error: El nombre solo debe contener letras y espacios.");
            }
        } while (!nombre.matches("[a-zA-Z ]+"));

        String apellido;
        do {
            System.out.print("Ingrese apellido: ");
            apellido = scanner.nextLine();
            if (!apellido.matches("[a-zA-Z ]+")) {
                System.out.println("Error: El apellido solo debe contener letras y espacios.");
            }
        } while (!apellido.matches("[a-zA-Z ]+"));

        String email;
        do {
            System.out.print("Ingrese email: ");
            email = scanner.nextLine();
            if (!(email.endsWith("@gmail.com") || email.endsWith("@hotmail.com") || email.endsWith("@outlook.com") || email.endsWith("@yahoo.com"))) {
                System.out.println("Error: El email debe tener una terminación válida (@gmail, @hotmail, @outlook, @yahoo).");
            }
        } while (!(email.endsWith("@gmail.com") || email.endsWith("@hotmail.com") || email.endsWith("@outlook.com") || email.endsWith("@yahoo.com")));

        System.out.print("Ingrese contraseña: ");
        String contrasena = scanner.nextLine();

        return new Cliente(DNI, nombre, apellido, email, contrasena);
    }

    @Override
    public void cargarPelicula(Scanner scanner) {
        Usuario admin = autenticarUsuarioPorRol(scanner, Administrador.class);
        if (admin == null) {
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
                return;
            }
            this.peliculaDAO.guardar(pelicula);
    }

    @Override
    public void listarPeliculas(Scanner scanner) {
        List<Pelicula> peliculas = this.peliculaDAO.listarTodas();
        if (peliculas.isEmpty()) {
            System.out.println("No hay películas registradas.");
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
    }

    @Override
    public void listarUsuarios(Scanner scanner) {
        List<Usuario> usuarios = this.usuarioDAO.listarTodos();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
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
    }

    @Override
    public void registrarResena(Scanner scanner) {
        Usuario usuario = autenticarUsuarioPorRol(scanner, Cliente.class);
        if (usuario == null) {
            return; 
        }

        System.out.println("\n--- Registro de Nueva Reseña ---");
            List<Pelicula> peliculas = this.peliculaDAO.listarTodas();
            if (peliculas.isEmpty()) {
                System.out.println("No hay películas cargadas en el sistema.");
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
                return;
            }  
            this.reseñaDAO.guardar(reseña);
    }

    @Override
    public void aprobarResena(Scanner scanner) {
        Usuario admin = autenticarUsuarioPorRol(scanner, Administrador.class);
        if (admin == null) {
            return; 
        }

        System.out.println("\n--- Aprobación de Reseñas ---");
            List<Reseña> reseniasNoAprobadas = this.reseñaDAO.listarNoAprobadas();
            if (reseniasNoAprobadas.isEmpty()) {
                System.out.println("No hay reseñas pendientes de aprobación.");
                return;
            }
            System.out.println("Reseñas pendientes:");
            for (Reseña r : reseniasNoAprobadas) {
                System.out.printf("ID: %d | Usuario: %s (ID: %d) | Película ID: %d | Comentario: %s | Puntaje: %d\n", r.getID(), r.getUsuario().getNombre(), r.getUsuario().getID(), r.getIDContenido(), r.getComentario(), r.getCalificacion());
            }
            System.out.print("Ingrese ID de la reseña a aprobar: ");
            int resenaID = Integer.parseInt(scanner.nextLine());
            this.reseñaDAO.aprobarResenia(resenaID);
    }

    private Usuario autenticarUsuarioPorRol(Scanner scanner, Class<? extends Usuario> tipoUsuarioEsperado) {
        System.out.println("\n--- Autenticación de Usuario Requerida ---");
        System.out.print("Ingrese su email: ");
        String email = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String contrasena = scanner.nextLine();
        Usuario usuario = this.usuarioDAO.autenticar(email, contrasena);
        if (usuario == null) {
            System.out.println("Error: Credenciales incorrectas.");
            return null;
        }
        if (!tipoUsuarioEsperado.isInstance(usuario)) {
            System.out.println("Error: Permisos insuficientes para esta acción.");
            return null;
        }
        System.out.println("Autenticación exitosa. ¡Bienvenido, " + usuario.getNombre() + "!");
        return usuario;
    }
}