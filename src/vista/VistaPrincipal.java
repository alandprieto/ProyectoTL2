package vista;

import javax.swing.*;
import java.awt.*;

public class VistaPrincipal extends JFrame {

    public JTextField txtBusqueda;
    public JButton btnBuscar;
    public JButton btnExplorar; // <--- RENOMBRADO (Antes btnVerTodas)
    public JButton btnCargarPeliculas;
    public JButton btnAdmin;

    public JPanel panelResultados;
    public JScrollPane scrollResultados;

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

        panelSuperior.add(lblBuscar);
        panelSuperior.add(txtBusqueda);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(btnExplorar); // Agregamos el botón renombrado
        panelSuperior.add(btnCargarPeliculas);
        panelSuperior.add(btnAdmin);

        // --- Panel Central ---
        panelResultados = new JPanel();
        panelResultados.setLayout(new GridLayout(0, 4, 10, 10));
        panelResultados.setBackground(new Color(240, 240, 240));
        panelResultados.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scrollResultados = new JScrollPane(panelResultados);
        scrollResultados.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollResultados.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(panelSuperior, BorderLayout.NORTH);
        this.add(scrollResultados, BorderLayout.CENTER);
    }

    public void iniciar() {
        this.setVisible(true);
    }
}