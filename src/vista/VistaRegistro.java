package vista;

import javax.swing.*;
import java.awt.*;

public class VistaRegistro extends JFrame {

    // Componentes públicos para el Controlador
    public JTextField txtNombre;
    public JTextField txtApellido;
    public JTextField txtDNI;
    public JTextField txtEmail;
    public JPasswordField txtPassword;

    public JButton btnGuardar;
    public JButton btnCancelar;

    public VistaRegistro() {
        this.setTitle("Nuevo Usuario - Streaming TDL2");
        this.setSize(400, 450);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        // --- Título ---
        JLabel lblTitulo = new JLabel("Crear Cuenta", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        this.add(lblTitulo, BorderLayout.NORTH);

        // --- Panel del Formulario ---
        // GridLayout(5, 2): 5 filas, 2 columnas
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 10, 15));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        // 1. Nombre
        panelForm.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre); // <--- ESTA LINEA FALTABA ANTES

        // 2. Apellido
        panelForm.add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        panelForm.add(txtApellido); // <--- AGREGADO

        // 3. DNI
        panelForm.add(new JLabel("DNI:"));
        txtDNI = new JTextField();
        panelForm.add(txtDNI); // <--- AGREGADO

        // 4. Email
        panelForm.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelForm.add(txtEmail); // <--- AGREGADO

        // 5. Contraseña
        panelForm.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panelForm.add(txtPassword); // <--- AGREGADO

        this.add(panelForm, BorderLayout.CENTER);

        // --- Panel de Botones ---
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(50, 150, 50)); // Verde
        btnGuardar.setForeground(Color.WHITE);

        btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        this.add(panelBotones, BorderLayout.SOUTH);
    }

    public void iniciar() {
        this.setVisible(true);
    }

    public void cerrar() {
        this.dispose();
    }
}