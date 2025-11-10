package control;

import java.time.Duration;
import java.util.Scanner;

import database.CargaDatosPrueba;
import database.ConexionBD;
import database.SetupBD;
import enums.GeneroPelicula;
import modelo.Usuario;
import modelo.Administrador;
import modelo.Cliente;
import modelo.Pelicula;
import modelo.Reseña;
import servicio.AppImple;
import dao.UsuarioDAO;
import dao.UsuarioDAOimple;
import modelo.Staff;
import dao.PeliculaDAO;
import dao.PeliculaDAOimple;
import java.util.List;

public class App {
    private UsuarioDAO usuarioDAO;
    private PeliculaDAO peliculaDAO;
    public static Scanner scanner = new Scanner(System.in);
    private AppImple servicio;


    public static void main(String[] args) {
        SetupBD.crearTablas();
        CargaDatosPrueba.cargarDatos();
        
        App app = new App();
        app.usuarioDAO = new UsuarioDAOimple();
        app.peliculaDAO = new PeliculaDAOimple();
        app.servicio = new AppImple();
        
        String TokenAdm = "zlra4142";
        int opcion = -1;

        try {
            while (opcion != 0) {
                System.out.println("\n===== MENÚ PRINCIPAL =====");
                System.out.println("1. Registrar un nuevo Cliente");
                System.out.println("2. Registrar un nuevo Administrador");
                System.out.println("3. Cargar una nueva Película (requiere ser admin)");
                System.out.println("4. Listar todas las Peliculas");
                System.out.println("5. Listar todos los Usuarios");
                System.out.println("6. Registrar una Resena");
                System.out.println("7. Aprobar una Resena");
                System.out.println("0. Salir");
                System.out.print("--> Seleccione una opcion: ");
                try {
                    opcion = Integer.parseInt(scanner.nextLine());
                    switch (opcion) {
                        case 1:
                            Cliente nuevoCliente = app.solicitarDatosCliente();
                            if (nuevoCliente != null) {
                                app.servicio.registrarCliente(nuevoCliente);
                            }
                            break;
                        case 2:
                            Administrador nuevoAdmin = app.solicitarDatosAdmin(TokenAdm);
                            if (nuevoAdmin != null) {
                                app.servicio.registrarAdmin(nuevoAdmin);
                            }
                            break;
                        case 3:
                            Usuario Admin = app.autenticarUsuarioPorRol(Administrador.class);
                            if (Admin != null) {
                                Pelicula nuevaPelicula = app.solicitarDatosPelicula();
                                if (nuevaPelicula != null) {
                                    app.servicio.cargarPelicula(nuevaPelicula);
                                }
                            }
                            break;
                        case 4:
                            System.out.print("Ordenar por (1: Título, 2: Género, 3: Duración): ");
                            int orden = Integer.parseInt(scanner.nextLine());
                            app.servicio.listarPeliculas(orden);
                            break;
                        case 5:
                            System.out.print("Ordenar por (1: Nombre, 2: Email): ");
                            int ordenUsuarios = Integer.parseInt(scanner.nextLine());
                            app.servicio.listarUsuarios(ordenUsuarios);
                            break;
                        case 6:
                            Usuario cliente = app.autenticarUsuarioPorRol(Cliente.class);
                            if (cliente != null) {
                                Reseña nuevaReseña = app.solicitarDatosResena(cliente);
                                if (nuevaReseña != null) {
                                    app.servicio.registrarResena(nuevaReseña);
                                }
                            }
                            break;
                        case 7:
                            Usuario admin = app.autenticarUsuarioPorRol(Administrador.class);
                            if (admin != null){
                                app.servicio.listarResenias();
                                System.out.print("Ingrese el ID de la reseña a aprobar: ");
                                int idResena = Integer.parseInt(scanner.nextLine());
                                app.servicio.aprobarResena(idResena);
                            }
                            break;
                        case 0:
                            break;
                        default:
                            System.out.println("Opcion no valida. Intente de nuevo.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Por favor, ingrese un numero.");
                } catch (Exception e) {
                    System.out.println("Ocurrio un error inesperado: " + e.getMessage());
                    e.printStackTrace(); 
                }
            }
            System.out.println("Saliendo de la aplicacion...");
        } finally {
            scanner.close(); 
            ConexionBD.cerrarConexion();
        }scanner.close(); 
    }


    public Cliente solicitarDatosCliente() {
        System.out.println("\n--- Registro de Nuevo Cliente ---");
        Usuario datosUsuario = solicitarDatosUsuario();
        if (datosUsuario == null) return null;
        
        Cliente cliente = new Cliente(datosUsuario.getDNI(), datosUsuario.getNombre(), datosUsuario.getApellido(), datosUsuario.getEmail(), datosUsuario.getContrasena());
        
        return cliente;
    }


    public Administrador solicitarDatosAdmin(String TokenAdm) {
        System.out.print("Ingrese Token de validacion: ");
        String TokenValido = scanner.nextLine();
        if (!TokenValido.equals(TokenAdm)) { 
            System.out.println("Error: Token no valido"); 
            return null; 
        }
        else{
            System.out.println("\n--- Registro de Nuevo Administrador ---");
            Usuario datosUsuario = solicitarDatosUsuario();
            if (datosUsuario == null) return null;

            Administrador admin = new Administrador(datosUsuario.getDNI(), datosUsuario.getNombre(), datosUsuario.getApellido(), datosUsuario.getEmail(), datosUsuario.getContrasena());
            
            return admin;
        }
    }


    private Usuario solicitarDatosUsuario() {
        long DNI = 0;
        while (true) {
            System.out.print("Ingrese DNI (sin puntos ni comas): ");
            try {
                DNI = Long.parseLong(scanner.nextLine());
                String dniStr = String.valueOf(DNI);
                if (DNI > 0 && dniStr.length() >= 7 && dniStr.length() <= 8) {
                    if (servicio.verificarDNI(DNI)) {
                        System.out.println("Error: El DNI ya está registrado. Por favor, ingrese un DNI diferente.");
                        continue;
                    }
                    break;
                }
                System.out.println("Error: El DNI debe ser un número positivo de 7 u 8 dígitos.");
            }
            catch (NumberFormatException e) {
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

        String contrasena;
        do {
            System.out.print("Ingrese contraseña: ");
            contrasena = scanner.nextLine();
            if (!validarContrasena(contrasena)) {
                System.out.println("Error: La contraseña debe tener al menos 8 caracteres y contener al menos un número.");
            }
        } while (!validarContrasena(contrasena));

        return new Cliente(DNI, nombre, apellido, email, contrasena);
    }


    private boolean validarContrasena(String contrasena) {
        if (contrasena == null || contrasena.length() < 8) {
            return false;
        }
        boolean tieneNumero = false;
        for (char c : contrasena.toCharArray()) {
            if (c >= '0' && c <= '9') {
                tieneNumero = true;
                break;
            }
        }
        
        return tieneNumero;
    }


    public Usuario autenticarUsuarioPorRol(Class<? extends Usuario> tipoUsuarioEsperado) { 
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


    public Pelicula solicitarDatosPelicula() {
        System.out.println("\n--- Carga de Nueva Película ---");
        System.out.print("Ingrese título de la película: ");
        String titulo = scanner.nextLine();

        int generoInput = 0;
        while (generoInput < 1 || generoInput > GeneroPelicula.values().length) {
            System.out.print("Ingrese género (1: ACCION, 2: COMEDIA, 3: DRAMA, 4: CIENCIA_FICCION): ");
            try {
                generoInput = Integer.parseInt(scanner.nextLine());
                if (generoInput < 1 || generoInput > GeneroPelicula.values().length) {
                    System.out.println("Error: Opción de género no válida.");
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número para el género.");
            }
        }
        GeneroPelicula genero = GeneroPelicula.values()[generoInput - 1];

        System.out.print("Ingrese nombre del director: ");
        String directorNombre = scanner.nextLine();
        Staff director = new Staff(directorNombre, "Director");

        int duracionMinutos = 0;
        while (duracionMinutos <= 0) {
            System.out.print("Ingrese duración en minutos: ");
            try {
                duracionMinutos = Integer.parseInt(scanner.nextLine());
                if (duracionMinutos <= 0) {
                    System.out.println("Error: La duración debe ser un número positivo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido para la duración.");
            }
        }
        Duration duracion = Duration.ofMinutes(duracionMinutos);

        Pelicula pelicula = new Pelicula(titulo, genero, "", director, 0.0, 0, null, null, 0, duracion);

        System.out.printf("Titulo: %s | Género: %s | Director: %s | Duración: %d min\n", pelicula.getTitulo(), pelicula.getGenero(), pelicula.getDirector().getNombre(), pelicula.getDuracion().toMinutes());
        System.out.println("¿La Película es correcta? [Y/N] ");
        String confirmacion = scanner.nextLine().trim().toUpperCase();

        if (!confirmacion.equals("Y")) {
            System.out.println("Operación cancelada. La película no fue cargada.");
            return null;
        }
        return pelicula;
    }


    public Reseña solicitarDatosResena(Usuario usuario) {
        System.out.println("\n--- Registro de Nueva Reseña ---");
        List<Pelicula> peliculas = this.peliculaDAO.listarTodas();
        if (peliculas.isEmpty()) {
            System.out.println("No hay películas cargadas en el sistema.");
            return null;
        }
        for (Pelicula p : peliculas) {
            System.out.printf("ID: %d - Título: %s\n", p.getID(), p.getTitulo());
        }
        int peliculaID = 0;
        boolean peliculaValida = false;
        while (!peliculaValida) {
            System.out.print("Ingrese un ID de pelicula valido para resenar: ");
            try {
                peliculaID = Integer.parseInt(scanner.nextLine());
                if (this.peliculaDAO.existePelicula(peliculaID)) {
                    peliculaValida = true;
                }
                else {
                    System.out.println("Error: ID de película no válido.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido para el ID.");
            }
        }
        System.out.print("Ingrese comentario: ");
        String comentario = scanner.nextLine();
        int puntaje = 0;
        while (puntaje < 1 || puntaje > 5) {
            System.out.print("Ingrese puntaje (1-5): ");
            try {
                puntaje = Integer.parseInt(scanner.nextLine());
                if (puntaje < 1 || puntaje > 5) {
                    System.out.println("Error: El puntaje debe ser un número entre 1 y 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido para el puntaje.");
            }
        }
        Reseña reseña = new Reseña(usuario, peliculaID, comentario, puntaje);
        System.out.println("Reseña a registrar:");
        System.out.printf("Usuario: %s | Película ID: %d | Comentario: %s | Puntaje: %d\n", reseña.getUsuario().getNombre(), reseña.getIDContenido(), reseña.getComentario(), reseña.getCalificacion());
        System.out.println("¿La Reseña es correcta? [Y/N] ");
        String confirmacion = scanner.nextLine().trim().toUpperCase();
        if (!confirmacion.equals("Y")) {
            System.out.println("Operación cancelada. La reseña no fue registrada.");
            return null;
        }
        return reseña;
    }
}