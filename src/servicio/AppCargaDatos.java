package servicio;

import java.util.Scanner;

import modelo.Administrador;
import modelo.Cliente;
import modelo.Usuario;

import dao.UsuarioDAO;
import dao.UsuarioDAOimple;


public class AppCargaDatos {

    private UsuarioDAO usuarioDAO;

    public AppCargaDatos() {
        this.usuarioDAO = new UsuarioDAOimple();
    }
    
    public Cliente solicitarDatosCliente() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n--- Registro de Nuevo Cliente ---");
        Usuario datosUsuario = solicitarDatosUsuario(scanner);
        if (datosUsuario == null) return null;
        
        Cliente cliente = new Cliente(datosUsuario.getDNI(), datosUsuario.getNombre(), datosUsuario.getApellido(), datosUsuario.getEmail(), datosUsuario.getContrasena());
        
        return cliente;
    }

    public Administrador solicitarDatosAdmin(String TokenAdm) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese Token de validacion: ");
        String TokenValido = scanner.nextLine();
        if (!TokenValido.equals(TokenAdm)) { 
            System.out.println("Error: Token no valido"); 
            return null; 
        }
        else{
            System.out.println("\n--- Registro de Nuevo Administrador ---");
            Usuario datosUsuario = solicitarDatosUsuario(scanner);
            if (datosUsuario == null) return null;

            Administrador admin = new Administrador(datosUsuario.getDNI(), datosUsuario.getNombre(), datosUsuario.getApellido(), datosUsuario.getEmail(), datosUsuario.getContrasena());
            
            return admin;
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