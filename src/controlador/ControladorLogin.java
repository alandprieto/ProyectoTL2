package controlador;

import modelo.Usuario;
import servicio.AppImple;
import vista.VistaLogin;
import vista.VistaPrincipal;
import vista.VistaRegistro;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorLogin implements ActionListener {

    private AppImple servicio;
    private VistaLogin vistaLogin;

    public ControladorLogin(AppImple servicio, VistaLogin vistaLogin) {
        this.servicio = servicio;
        this.vistaLogin = vistaLogin;

        // Escuchamos los botones
        this.vistaLogin.btnIngresar.addActionListener(this);
        this.vistaLogin.btnRegistrar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaLogin.btnIngresar) {
            procesarLogin();
        } else if (e.getSource() == vistaLogin.btnRegistrar) {
            abrirRegistro();
        }
    }

    private void procesarLogin() {
        String email = vistaLogin.txtEmail.getText();
        String password = new String(vistaLogin.txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(vistaLogin, "Por favor complete todos los campos.");
            return;
        }

        // 1. Llamamos al servicio para validar
        Usuario usuario = servicio.autenticarUsuario(email, password);

        if (usuario != null) {
            // --- LOGIN EXITOSO ---
            JOptionPane.showMessageDialog(vistaLogin, "¡Bienvenido " + usuario.getNombre() + "!");

            // 2. Cerramos la ventana de Login
            vistaLogin.cerrar();

            // 3. INICIAMOS LA VISTA PRINCIPAL (La aplicación real)
            abrirVistaPrincipal();

        } else {
            // --- LOGIN FALLIDO ---
            JOptionPane.showMessageDialog(vistaLogin, "Email o contraseña incorrectos.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirVistaPrincipal() {
        // Creamos la vista y el controlador principal
        VistaPrincipal vistaP = new VistaPrincipal();

        // Conectamos el Controlador Principal (que ya tenías hecho)
        ControladorPrincipal controladorP = new ControladorPrincipal(servicio, vistaP);

        // Mostramos la ventana principal
        vistaP.iniciar();
    }

    private void abrirRegistro() {
        // 1. Crear la vista de registro
        VistaRegistro vistaReg = new VistaRegistro();

        // 2. Crear su controlador
        // AHORA LE PASAMOS (servicio, vistaReg, vistaLogin)
        // 'this.vistaLogin' es la ventana actual de login que este controlador maneja
        new ControladorRegistro(servicio, vistaReg, this.vistaLogin);

        // 3. Mostrar la ventana
        vistaReg.iniciar();
    }
}