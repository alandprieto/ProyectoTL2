package vista;

import javax.swing.*;
import java.awt.*;

public class VistaLogin extends JFrame {

    public JTextField txtEmail;
    public JPasswordField txtPassword;
    public JButton btnIngresar;
    public JButton btnRegistrar;

    // Componente para el mensaje de éxito (registro)
    public JLabel lblMensaje;

    public VistaLogin() {
        this.setTitle("Login - Streaming TDL2");
        this.setSize(400, 350);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setResizable(false);

        // --- Panel Superior (Mensaje + Título) ---
        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.Y_AXIS));

        // 1. Cartel de Mensaje (Oculto por defecto)
        lblMensaje = new JLabel(" ");
        lblMensaje.setOpaque(true);
        lblMensaje.setBackground(new Color(240, 240, 240));
        lblMensaje.setForeground(new Color(240, 240, 240));
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 14));
        lblMensaje.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        // Truco para que ocupe todo el ancho
        lblMensaje.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // 2. Título
        JLabel lblTitulo = new JLabel("Bienvenido", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        panelNorte.add(lblMensaje);
        panelNorte.add(lblTitulo);

        this.add(panelNorte, BorderLayout.NORTH);

        // --- Panel Central (Formulario) ---
        JPanel panelForm = new JPanel(new GridLayout(3, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // 1. Email
        panelForm.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelForm.add(txtEmail); // <--- ESTA LÍNEA FALTABA (Agrega la cajita)

        // 2. Password
        panelForm.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panelForm.add(txtPassword); // <--- ESTA LÍNEA FALTABA (Agrega la cajita)

        // 3. Espacio vacío de relleno
        panelForm.add(new JLabel(""));
        panelForm.add(new JLabel(""));

        this.add(panelForm, BorderLayout.CENTER);

        // --- Panel Inferior (Botones) ---
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        btnIngresar = new JButton("Iniciar Sesión");
        btnIngresar.setBackground(new Color(50, 150, 250)); // Azul
        btnIngresar.setForeground(Color.WHITE);

        btnRegistrar = new JButton("Registrarse");

        panelBotones.add(btnIngresar);
        panelBotones.add(btnRegistrar);

        this.add(panelBotones, BorderLayout.SOUTH);
    }

    public void iniciar() {
        this.setVisible(true);
    }

    public void cerrar() {
        this.dispose();
    }

    // Método para activar el cartel verde desde el ControladorRegistro
    public void mostrarExito(String texto) {
        lblMensaje.setText(texto);
        lblMensaje.setBackground(new Color(40, 167, 69)); // Verde
        lblMensaje.setForeground(Color.WHITE);
    }
}