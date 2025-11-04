package Servicio;

import java.util.Scanner;
import Usuario.Administrador;
import Usuario.Cliente;
import Usuario.Usuario;
// Se eliminan importaciones DAO y ConexionBD

public class AppCargaDatos {
    
    // Método para solicitar datos y DEVOLVER un Cliente
    public Cliente solicitarDatosCliente() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n--- Registro de Nuevo Cliente ---");
        Usuario datosUsuario = solicitarDatosUsuario(scanner);
        if (datosUsuario == null) return null;
        
        Cliente cliente = new Cliente(datosUsuario.getDNI(), datosUsuario.getNombre(), datosUsuario.getApellido(), datosUsuario.getEmail(), datosUsuario.getContrasena());
        
        // Ya no se guarda aquí
        return cliente;
    }

    // Método para solicitar datos y DEVOLVER un Administrador
    public Administrador solicitarDatosAdmin(String TokenAdm) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese Token de validacion: ");
        String TokenValido = scanner.nextLine();
        if (!TokenValido.equals(TokenAdm)) { 
            System.out.println("Error: Token no valido"); 
            return null; // Devuelve null si el token es inválido
        }
        else{
            System.out.println("\n--- Registro de Nuevo Administrador ---");
            Usuario datosUsuario = solicitarDatosUsuario(scanner);
            if (datosUsuario == null) return null;

            Administrador admin = new Administrador(datosUsuario.getDNI(), datosUsuario.getNombre(), datosUsuario.getApellido(), datosUsuario.getEmail(), datosUsuario.getContrasena());
            
            // Ya no se guarda aquí
            return admin;
        }
    }

    // Este método auxiliar se mantiene igual
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

        // Devuelve un objeto base (Cliente) con los datos, AppCargaDatos lo convertirá
        return new Cliente(DNI, nombre, apellido, email, contrasena);
    }
}