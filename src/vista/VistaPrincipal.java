package vista;

import javax.swing.*;
import java.awt.*;

public class VistaPrincipal extends JFrame {

    public JTextField txtBusqueda;
    public JButton btnBuscar;
    public JButton btnExplorar; // <--- RENOMBRADO (Antes btnVerTodas)
    public JButton btnCargarPeliculas;
    public JButton btnAdmin;
    public JButton btnCerrarSesion;

    public JPanel panelResultados;
    public JScrollPane scrollResultados;
    public JLabel lblBienvenida;

    public VistaPrincipal() {
        this.setTitle("ALTI - Tu Plataforma de Streaming favorita");
        this.setSize(1000, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        // --- Panel Superior ---
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBackground(Color.DARK_GRAY);

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setForeground(Color.WHITE);

        txtBusqueda = new JTextField(20);
        btnBuscar = new JButton("Buscar");

        // CAMBIO AQUÍ: Texto y variable del botón
        btnExplorar = new JButton("Explorar (Aleatorias)");

        btnCargarPeliculas = new JButton("Cargar Películas (CSV)");
        btnCargarPeliculas.setVisible(false);

        btnAdmin = new JButton("Panel Admin");
        btnCerrarSesion = new JButton("Cerrar Sesión");

        panelSuperior.add(lblBuscar);
        panelSuperior.add(txtBusqueda);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(btnExplorar); // Agregamos el botón renombrado
        panelSuperior.add(btnCargarPeliculas);
        panelSuperior.add(btnAdmin);
        panelSuperior.add(btnCerrarSesion);

        // Contenedor superior: panel con el panelSuperior y el lblBienvenida debajo
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BorderLayout());
        topContainer.add(panelSuperior, BorderLayout.NORTH);

        lblBienvenida = new JLabel("", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 16));
        lblBienvenida.setForeground(Color.BLACK);
        lblBienvenida.setOpaque(true);
        lblBienvenida.setBackground(new Color(255, 255, 200));
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblBienvenida.setVisible(false);
        topContainer.add(lblBienvenida, BorderLayout.SOUTH);

        // --- Panel Central ---
        panelResultados = new JPanel();
        panelResultados.setLayout(new GridLayout(0, 4, 10, 10));
        panelResultados.setBackground(new Color(240, 240, 240));
        panelResultados.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scrollResultados = new JScrollPane(panelResultados);
        scrollResultados.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollResultados.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Añadimos el contenedor superior que incluye la barra y el banner
        this.add(topContainer, BorderLayout.NORTH);
        this.add(scrollResultados, BorderLayout.CENTER);
    }

    public void iniciar() {
        this.setVisible(true);
    }
}