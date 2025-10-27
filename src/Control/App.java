package Control;

import java.util.Scanner;
import DataBase.ConexionBD; 
import Servicio.IAppServicio;
import Servicio.AppImple;

public class App {
    public static void main(String[] args) {
        IAppServicio servicio = new AppImple();
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
                        servicio.registrarCliente(scanner); 
                        break;
                    case 2:
                        servicio.registrarAdmin(scanner,TokenAdm);
                        break;
                    case 3:
                        servicio.cargarPelicula(scanner);
                        break;
                    case 4:
                        servicio.listarPeliculas(scanner);
                        break;
                    case 5:
                        servicio.listarUsuarios(scanner);
                        break;
                    case 6:
                        servicio.registrarResena(scanner);
                        break;
                    case 7:
                        servicio.aprobarResena(scanner);
                        break;
                    case 0:
                        ConexionBD.cerrarConexion();
                        break;
                    default:
                        System.out.println("Opcion no valida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, ingrese un numero.");
            } catch (Exception e) {
                System.out.println("Ocurrio un error inesperado: " + e.getMessage());
            }
        }
        System.out.println("Saliendo de la aplicacion...");
        scanner.close();
    }
}