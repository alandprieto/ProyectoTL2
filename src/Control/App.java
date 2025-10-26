package Control;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import Catalogo.Pelicula;
import Catalogo.Reseña;
import Catalogo.Staff;
import DAO.PeliculaDAOimple;
import DAO.ReseñaDAOimple;
import DAO.UsuarioDAOimple;
import ENUM.GeneroPelicula;
import Usuario.Administrador;
import Usuario.Cliente;
import Usuario.Usuario; 
import DataBase.ConexionBD;

public class App {
    public static void main(String[] args) {
        
        UsuarioDAOimple usuarioDAO = new UsuarioDAOimple();
        PeliculaDAOimple peliculaDAO = new PeliculaDAOimple();
        ReseñaDAOimple reseñaDAO = new ReseñaDAOimple();

        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n===== MENÚ PRINCIPAL =====");
            System.out.println("1. Registrar un nuevo Cliente");
            System.out.println("2. Registrar un nuevo Administrador");
            System.out.println("3. Cargar una nueva Película (requiere ser admin)");
            System.out.println("4. Listar todas las Películas");
            System.out.println("5. Listar todos los Usuarios");
            System.out.println("6. Registrar una Reseña");
            System.out.println("7. Aprobar una Reseña");
            System.out.println("0. Salir");
            System.out.print("--> Seleccione una opción: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                case 1:
                    registrarCliente(scanner, usuarioDAO); // Registrar un nuevo Cliente
                break;
                case 2:
                    registrarAdmin(scanner, usuarioDAO); // Registrar un nuevo Administrador
                break;
                case 3:
                    cargarPelicula(scanner, peliculaDAO, usuarioDAO); // Cargar una nueva Película (requiere ser admin)
                break;
                case 4:
                    listarPeliculas(scanner, peliculaDAO); // Listar todas las Películas
                break;
                case 5:
                    listarUsuarios(scanner, usuarioDAO); // Listar todos los Usuarios
                break;
                case 6:
                    registrarResena(scanner, reseñaDAO, peliculaDAO, usuarioDAO); // Registrar una Reseña
                break;
                case 7:
                    aprobarResena(scanner, reseñaDAO, usuarioDAO); // Aprobar una Reseña
                break;
                case 0:
                    ConexionBD.cerrarConexion();
                break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, ingrese un número.");
            }
        }
        System.out.println("Saliendo de la aplicación...");
        scanner.close();
    }

    //case 1: Registrar un nuevo Cliente
    private static void registrarCliente(Scanner scanner, UsuarioDAOimple usuarioDAO) {
        Boolean firstTime = true;
        Long DNI;
        String nombre;
        String apellido;
        String email;
        String contrasena;

        do {
            if (!firstTime) { System.out.println("Error: El DNI debe ser un número válido. Intente de nuevo."); }
            System.out.print("Ingrese DNI (sin puntos ni comas): ");
            DNI = Long.parseLong(scanner.nextLine());
            firstTime = false;
        } while((DNI <= 0) && (DNI.toString().length() >= 7)); // Validar que sea un número positivo y tenga el largo correcto

        firstTime = true;

        do {
            if (!firstTime) { System.out.println("Error: El nombre solo debe contener letras. Intente de nuevo."); }
            System.out.print("Ingrese nombre: ");
            nombre = scanner.nextLine();
            firstTime = false;
        } while(!nombre.matches("[a-zA-Z ]+")); // Validar que solo contenga letras y espacios

        firstTime = true;

        do {
            if (!firstTime) { System.out.println("Error: El apellido solo debe contener letras. Intente de nuevo."); }
            System.out.print("Ingrese apellido: ");
            apellido = scanner.nextLine();
            firstTime = false;
        } while(!apellido.matches("[a-zA-Z ]+")); // Validar que solo contenga letras y espacios

        firstTime = true;
        
        do {
            if (!firstTime) { System.out.println("Error: El email debe tener una terminacion valida. Intente de nuevo."); }
            System.out.print("Ingrese email: ");
            email = scanner.nextLine();
            firstTime = false;
        } while(!(email.endsWith("@gmail.com") || email.endsWith("@hotmail.com") || email.endsWith("@outlook.com"))); // Validar terminaciones comunes

        System.out.print("Ingrese contraseña: ");
        contrasena = scanner.nextLine();

        Cliente cliente = new Cliente(DNI, nombre, apellido, email, contrasena);
        if (usuarioDAO.guardar(cliente)) {
            System.out.println("Cliente registrado exitosamente.");
        } else {
            System.out.println("No se pudo registrar al cliente (el email ya podría existir).");
        }
    }
    
    //case 2: Registrar un nuevo Administrador
    private static void registrarAdmin(Scanner scanner, UsuarioDAOimple usuarioDAO) {
        Boolean firstTime = true;
        Long DNI;
        String nombre;
        String apellido;
        String email;
        String contrasena;

        do {
            if (!firstTime) { System.out.println("Error: El DNI debe ser un número válido. Intente de nuevo."); }
            System.out.print("Ingrese DNI (sin puntos ni comas): ");
            DNI = Long.parseLong(scanner.nextLine());
            firstTime = false;
        } while((DNI <= 0) && (DNI.toString().length() >= 7)); // Validar que sea un número positivo y tenga el largo correcto

        firstTime = true;

        do {
            if (!firstTime) { System.out.println("Error: El nombre solo debe contener letras. Intente de nuevo."); }
            System.out.print("Ingrese nombre: ");
            nombre = scanner.nextLine();
            firstTime = false;
        } while(!nombre.matches("[a-zA-Z ]+"));

        firstTime = true;

        do {
            if (!firstTime) { System.out.println("Error: El apellido solo debe contener letras. Intente de nuevo."); }
            System.out.print("Ingrese apellido: ");
            apellido = scanner.nextLine();
            firstTime = false;
        } while(!apellido.matches("[a-zA-Z ]+"));

        firstTime = true;
        
        do {
            if (!firstTime) { System.out.println("Error: El email debe tener una terminacion valida. Intente de nuevo."); }
            System.out.print("Ingrese email: ");
            email = scanner.nextLine();
            firstTime = false;
        } while(!(email.endsWith("@gmail.com") || email.endsWith("@hotmail.com") || email.endsWith("@outlook.com")));

        System.out.print("Ingrese contraseña: ");
        contrasena = scanner.nextLine();

        Administrador admin = new Administrador(DNI, nombre, apellido, email, contrasena);
        if (usuarioDAO.guardar(admin)) {
            System.out.println("Administrador registrado exitosamente.");
        } else {
            System.out.println("No se pudo registrar al administrador (el email ya podría existir).");
        }
    }

    //case 3: Cargar una nueva Película (requiere ser admin)
    private static void cargarPelicula(Scanner scanner, PeliculaDAOimple peliculaDAO, UsuarioDAOimple usuarioDAO) {
        // Valida si la persona que va a cargar la Película es administrador        
        System.out.println("\n--- Autenticación de Administrador Requerida ---");
        System.out.print("Ingrese su email de administrador: ");
        String email = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String contrasena = scanner.nextLine();

        Usuario usuario = usuarioDAO.autenticar(email, contrasena);
        
        if (!(usuario instanceof Administrador) || usuario == null) {
            System.out.println("Error: Solo los administradores pueden cargar películas.");
            return;
        } else {
            System.out.println("Autenticación exitosa. Puede proceder a cargar la película.");
            System.out.println();

            System.out.print("Ingrese título de la película: ");
            String titulo = scanner.nextLine();
            System.out.print("Ingrese género (1: ACCION, 2: COMEDIA, 3: DRAMA, 4: CIENCIA FICCION): ");
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

            peliculaDAO.guardar(pelicula);
            System.out.println("Película cargada exitosamente.");
        }
    }

    //case 4: Listar todas las Películas
    private static void listarPeliculas(Scanner scanner, PeliculaDAOimple peliculaDAO) {
        List<Pelicula> peliculas = peliculaDAO.listarTodas();
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
            System.out.printf("Título: %s | Género: %s | Duración: %d min\n", 
            p.getTitulo(), p.getGenero(), p.getDuracion().toMinutes());
        }
    }

    //case 5: Listar todos los Usuarios
    private static void listarUsuarios(Scanner scanner, UsuarioDAOimple usuarioDAO) {
        List<Usuario> usuarios = usuarioDAO.listarTodos();
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

    //case 6: Registrar una Reseña
    private static void registrarResena(Scanner scanner, ReseñaDAOimple reseñaDAO, PeliculaDAOimple peliculaDAO, UsuarioDAOimple usuarioDAO) {
        System.out.println("\n--- Autenticación de Usuario Requerida ---");
        System.out.print("Ingrese su email: ");
        String email = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String contrasena = scanner.nextLine();

        Usuario usuario = usuarioDAO.autenticar(email, contrasena);
        
        if (!(usuario instanceof Cliente) || usuario == null) {
            System.out.println("Error: Solo los clientes pueden registrar reseñas.");
            return;
        } else {
            System.out.println("Autenticación exitosa. Puede proceder a registrar la reseña.");

            System.out.println("\n--- Películas Disponibles para Reseñar ---");
            List<Pelicula> peliculas = peliculaDAO.listarTodas();
            if (peliculas.isEmpty()) {
                System.out.println("No hay películas cargadas en el sistema.");
                return;
            }
            for (Pelicula p : peliculas) {
                System.out.printf("ID: %d - Título: %s\n", p.getID(), p.getTitulo());
            }

            int peliculaID;
            do {
                System.out.print("Ingrese un ID de película válido para reseñar: ");
                peliculaID = Integer.parseInt(scanner.nextLine());
            } while (!peliculaDAO.existePelicula(peliculaID));

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
            reseñaDAO.guardar(reseña);
            System.out.println("Reseña registrada exitosamente.");
        }
    }

    //case 7: Aprobar una Reseña (requiere ser admin)
    private static void aprobarResena(Scanner scanner, ReseñaDAOimple reseñaDAO, UsuarioDAOimple usuarioDAO) {
        System.out.println("\n--- Autenticación de Administrador Requerida ---");
        System.out.print("Ingrese su email de administrador: ");
        String email = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String contrasena = scanner.nextLine();

        Usuario usuario = usuarioDAO.autenticar(email, contrasena);
        
        if (!(usuario instanceof Administrador) || usuario == null) {
            System.out.println("Error: Solo los administradores pueden aprobar reseñas");
            return;
        } else {
            System.out.println("Autenticación exitosa. Puede proceder a aprobar reseñas.");

            List<Reseña> reseniasNoAprobadas = reseñaDAO.listarNoAprobadas();
            if (reseniasNoAprobadas.isEmpty()) {
                System.out.println("No hay reseñas pendientes de aprobación.");
                return;
            }
            for (Reseña r : reseniasNoAprobadas) {
                System.out.printf("ID: %d | Usuario ID: %d | Película ID: %d | Comentario: %s | Puntaje: %d\n", r.getID(), r.getUsuario(), r.getIDContenido(), r.getComentario(), r.getCalificacion());
            }

            System.out.print("Ingrese ID de la reseña a aprobar: ");
            int resenaID = Integer.parseInt(scanner.nextLine());
            reseñaDAO.aprobarResenia(resenaID);
        }
    }
}