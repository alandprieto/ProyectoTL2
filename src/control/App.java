package control;

import java.util.Scanner;

import database.CargaDatosPrueba;
import database.ConexionBD;
import database.SetupBD;
import modelo.Usuario;
import modelo.Administrador;
import modelo.Cliente;
import servicio.AppCargaDatos;
import servicio.AppImple;
import servicio.AppServicio;

public class App {
    public static void main(String[] args) {
        SetupBD.crearTablas();
        CargaDatosPrueba.cargarDatos();
        AppServicio servicio = new AppImple();
        AppCargaDatos cargadorDatos = new AppCargaDatos(); // Instanciar el cargador de datos
        String TokenAdm = "zlra4142";
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;
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
                        // 1. App llama a AppCargaDatos para obtener el objeto
                        Cliente nuevoCliente = cargadorDatos.solicitarDatosCliente();
                        if (nuevoCliente != null) {
                            // 2. App pasa el objeto a AppImple (servicio) para guardarlo
                            servicio.registrarCliente(nuevoCliente);
                        }
                        break;
                    case 2:
                        // 1. App llama a AppCargaDatos para obtener el objeto
                        Administrador nuevoAdmin = cargadorDatos.solicitarDatosAdmin(TokenAdm);
                        if (nuevoAdmin != null) {
                            // 2. App pasa el objeto a AppImple (servicio) para guardarlo
                            servicio.registrarAdmin(nuevoAdmin);
                        }
                        break;
                    case 3:
                        Usuario Admin = cargadorDatos.autenticarUsuarioPorRol(Administrador.class);
                        if (Admin != null) {
                            servicio.cargarPelicula();
                        }
                        break;
                    case 4:
                        // Corregido: Pasar el scanner al servicio
                        servicio.listarPeliculas();
                        break;
                    case 5:
                        // Corregido: Pasar el scanner al servicio
                        servicio.listarUsuarios();
                        break;
                    case 6:
                        // Corregido: Pasar el scanner al servicio
                        Usuario cliente = cargadorDatos.autenticarUsuarioPorRol(Cliente.class);
                        if (cliente != null) {
                            servicio.registrarResena(cliente);
                        }
                        break;
                    case 7:
                        // Corregido: Pasar el scanner al servicio
                        Usuario admin = cargadorDatos.autenticarUsuarioPorRol(Administrador.class);
                        if (admin != null){
                            servicio.aprobarResena();
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
            } finally {
                ConexionBD.cerrarConexion();
            }
        }
        System.out.println("Saliendo de la aplicacion...");
        scanner.close();
    }
}