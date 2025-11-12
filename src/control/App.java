package control;

import java.util.Scanner;

import database.CargaDatosPrueba;
import database.ConexionBD;
import database.SetupBD;
import modelo.Usuario;
import modelo.Cliente;
import modelo.Reseña;
import servicio.AppImple;
import dao.PeliculaDAO;
import dao.PeliculaDAOimple;
import java.util.List;
import modelo.Pelicula;

public class App {
    private PeliculaDAO peliculaDAO;
    public static Scanner scanner = new Scanner(System.in);
    private AppImple servicio;


    public static void main(String[] args) {
        SetupBD.crearTablas();
        CargaDatosPrueba.cargarPeliculasDesdeCSV();
        
        App app = new App();
        app.peliculaDAO = new PeliculaDAOimple();
        app.servicio = new AppImple();
        
        int opcion = -1;

        try {
            while (opcion != 0) {
                System.out.println("\n===== MENÚ PRINCIPAL =====");
                System.out.println("1. Iniciar sesión");
                System.out.println("2. Registrar un nuevo Cliente");
                System.out.println("3. Listar todas las Peliculas");
                System.out.println("4. Registrar una Resena");
                System.out.println("0. Salir");
                System.out.print("--> Seleccione una opcion: ");
                try {
                    opcion = Integer.parseInt(scanner.nextLine());
                    switch (opcion) {
                        case 1:
                            Usuario usuario = app.autenticarUsuario();
                            break;
                        case 2:
                            Cliente nuevoCliente = app.solicitarDatosCliente();
                            if (nuevoCliente != null) {
                                app.servicio.registrarCliente(nuevoCliente);
                            }
                            break;
                        case 3:
                            System.out.print("Ordenar por (1: Título, 2: Género): ");
                            int orden = Integer.parseInt(scanner.nextLine());
                            app.servicio.listarPeliculas(orden);
                            break;
                        case 4:
                            Usuario cliente = app.autenticarUsuario();
                            if (cliente != null) {
                                Reseña nuevaReseña = app.solicitarDatosResena(cliente);
                                if (nuevaReseña != null) {
                                    app.servicio.registrarResena(nuevaReseña);
                                }
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

    public Usuario autenticarUsuario() { 
        System.out.println("\n--- Autenticación de Usuario Requerida ---");
        System.out.print("Ingrese su email: ");
        String email = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String contrasena = scanner.nextLine();
        Usuario usuario = servicio.login(email, contrasena);
        return usuario;
    }


    public Cliente solicitarDatosCliente() {
        System.out.println("\n--- Registro de Nuevo Cliente ---");
        Usuario datosUsuario = solicitarDatosUsuario();
        if (datosUsuario == null) return null;
        
        Cliente cliente = new Cliente(datosUsuario.getDNI(), datosUsuario.getNombre(), datosUsuario.getApellido(), datosUsuario.getEmail(), datosUsuario.getContrasena());
        
        return cliente;
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