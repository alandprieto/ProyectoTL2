package controlador;

import modelo.Cliente;
import servicio.AppImple;
import vista.VistaRegistro;
import vista.VistaLogin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorRegistro implements ActionListener {

    private AppImple servicio;
    private VistaRegistro vistaRegistro;
    private VistaLogin vistaLogin;

    // Lista de dominios válidos
    private static final String[] DOMINIOS_PERMITIDOS = {
            "@gmail.com", "@hotmail.com", "@outlook.com", "@yahoo.com", "@alumno.unlp.edu.ar"
    };

    public ControladorRegistro(AppImple servicio, VistaRegistro vistaRegistro, VistaLogin vistaLogin) {
        this.servicio = servicio;
        this.vistaRegistro = vistaRegistro;
        this.vistaLogin = vistaLogin;

        this.vistaRegistro.btnGuardar.addActionListener(this);
        this.vistaRegistro.btnCancelar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaRegistro.btnGuardar) {
            procesarRegistro();
        } else if (e.getSource() == vistaRegistro.btnCancelar) {
            vistaRegistro.cerrar();
        }
    }

    private void procesarRegistro() {
        // 1. OBTENER DATOS
        String nombre = vistaRegistro.txtNombre.getText().trim();
        String apellido = vistaRegistro.txtApellido.getText().trim();
        String dniStr = vistaRegistro.txtDNI.getText().trim();
        String email = vistaRegistro.txtEmail.getText().trim();
        String password = new String(vistaRegistro.txtPassword.getPassword());

        // --- VALIDACIONES ---

        // A. Campos Vacíos
        if (nombre.isEmpty() || apellido.isEmpty() || dniStr.isEmpty() || email.isEmpty() || password.isEmpty()) {
            mostrarError("Todos los campos son obligatorios.");
            return;
        }

        // B. Validar Email
        if (!esDominioValido(email)) {
            mostrarError("Mail incorrecto. Debe usar: @gmail.com, @hotmail.com, etc.");
            return;
        }

        // C. Validar Nombres
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$") || !apellido.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            mostrarError("Nombre y Apellido no deben contener números ni símbolos.");
            return;
        }

        // D. Validar Contraseña
        if (password.length() < 8) {
            mostrarError("La contraseña debe tener al menos 8 caracteres.");
            return;
        }
        if (!contieneDigito(password)) {
            mostrarError("La contraseña debe contener al menos un número.");
            return;
        }

        // E. VALIDACIÓN NUEVA: LONGITUD DE DNI
        // Verificamos que tenga entre 7 y 9 caracteres (estándar aproximado)
        if (dniStr.length() < 7 || dniStr.length() > 9) {
            mostrarError("El DNI debe tener entre 7 y 9 dígitos.");
            return; // <--- FRENAMOS ACÁ
        }

        // F. Validar DNI Numérico y Unicidad
        try {
            Long dni = Long.parseLong(dniStr);

            if (dni <= 0) {
                mostrarError("El DNI debe ser positivo.");
                return;
            }

            if (servicio.existeDNI(dni)) {
                mostrarError("El DNI ya existe.");
                return;
            }
            if (servicio.existeEmail(email)) {
                mostrarError("El Email ya existe.");
                return;
            }

            // --- GUARDADO ---
            Cliente nuevo = new Cliente(dni, nombre, apellido, email, password);

            boolean exito = servicio.registrarCliente(nuevo);

            if (exito) {
                vistaRegistro.cerrar();
                vistaLogin.mostrarExito("¡Registro exitoso! Inicie sesión.");
                // Asegurarnos de que la ventana de login esté visible y reciba foco
                javax.swing.SwingUtilities.invokeLater(() -> {
                    vistaLogin.setVisible(true);
                    vistaLogin.toFront();
                    vistaLogin.requestFocus();
                });
            } else {
                mostrarError("Error al guardar en la base de datos.");
            }

        } catch (NumberFormatException ex) {
            mostrarError("El DNI debe contener solo números.");
        } catch (Exception ex) {
            mostrarError("Error inesperado: " + ex.getMessage());
        }
    }

    // Muestra un diálogo de error en la vista de registro
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vistaRegistro, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Verifica que el email termine en alguno de los dominios permitidos
    private boolean esDominioValido(String email) {
        if (email == null || !email.contains("@"))
            return false;
        for (String d : DOMINIOS_PERMITIDOS) {
            if (email.toLowerCase().endsWith(d))
                return true;
        }
        return false;
    }

    // Verifica si la cadena contiene al menos un dígito
    private boolean contieneDigito(String texto) {
        if (texto == null)
            return false;
        for (char c : texto.toCharArray()) {
            if (Character.isDigit(c))
                return true;
        }
        return false;
    }

}