package vista;

import modelo.Pelicula;
import servicio.ConsultaPeliculasOMDb;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import org.json.JSONObject;

/**
 * Ventana modal que muestra los resultados de búsqueda sin afectar la pantalla principal
 */
public class VistaResultadosBusqueda extends JDialog {
    private JPanel panelResultados;
    private JScrollPane scrollResultados;

    /**
     * Construye una ventana modal para mostrar resultados de búsqueda.
     */
    public VistaResultadosBusqueda(JFrame parent, String terminoBusqueda) {
        super(parent, "Resultados de búsqueda: " + terminoBusqueda, true);

        this.setSize(1000, 700);
        this.setLocationRelativeTo(parent);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBackground(Color.DARK_GRAY);

        JLabel lblInfo = new JLabel("Se encontraron resultados para: " + terminoBusqueda);
        lblInfo.setForeground(Color.WHITE);
        panelSuperior.add(lblInfo);

        this.add(panelSuperior, BorderLayout.NORTH);

        panelResultados = new JPanel();
        panelResultados.setLayout(new GridLayout(0, 4, 10, 10));
        panelResultados.setBackground(new Color(240, 240, 240));
        panelResultados.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scrollResultados = new JScrollPane(panelResultados);
        scrollResultados.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollResultados.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(scrollResultados, BorderLayout.CENTER);
    }

    /**
     * Carga y muestra una lista de películas en las tarjetas.
     */
    public void cargarPeliculas(List<Pelicula> peliculas) {
        panelResultados.removeAll();
        
        if (peliculas == null || peliculas.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay resultados");
            lblVacio.setHorizontalAlignment(SwingConstants.CENTER);
            panelResultados.add(lblVacio);
        } else {
            for (Pelicula p : peliculas) {
                JPanel tarjeta = new JPanel();
                tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
                tarjeta.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                tarjeta.setPreferredSize(new Dimension(180, 320));

                JLabel lblImg = new JLabel("Cargando...");
                lblImg.setAlignmentX(Component.CENTER_ALIGNMENT);
                cargarImagenAsync(p.getPosterURL(), lblImg);

                JLabel lblTitulo = new JLabel("<html><center>" + p.getTitulo() + "</center></html>");
                lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
                lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));

                JLabel lblAnio = new JLabel("Año: " + p.getAnio());
                lblAnio.setAlignmentX(Component.CENTER_ALIGNMENT);
                lblAnio.setFont(new Font("Arial", Font.PLAIN, 10));
                lblAnio.setForeground(new Color(100, 100, 100));

                JButton btnDetalles = new JButton("Ver Sinopsis");
                btnDetalles.setAlignmentX(Component.CENTER_ALIGNMENT);
                btnDetalles.addActionListener(ev -> mostrarSinopsis(p));

                tarjeta.add(Box.createVerticalStrut(10));
                tarjeta.add(lblImg);
                tarjeta.add(Box.createVerticalStrut(5));
                tarjeta.add(lblTitulo);
                tarjeta.add(lblAnio);
                tarjeta.add(Box.createVerticalStrut(5));
                tarjeta.add(btnDetalles);

                panelResultados.add(tarjeta);
            }
        }
        panelResultados.revalidate();
        panelResultados.repaint();
    }

    /**
     * Muestra una ventana modal con los detalles completos de una película desde OMDb.
     */
    private void mostrarSinopsis(Pelicula p) {
        JDialog dialogo = new JDialog(this, "Detalles: " + p.getTitulo(), true);
        dialogo.setSize(500, 400);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout(10, 10));

        JLabel lblCargando = new JLabel("Consultando información...", SwingConstants.CENTER);
        lblCargando.setFont(new Font("Arial", Font.ITALIC, 14));
        dialogo.add(lblCargando, BorderLayout.CENTER);
        dialogo.setVisible(true);

        new Thread(() -> {
            try {
                JSONObject datosPelicula = ConsultaPeliculasOMDb.consultarPelicula(p.getTitulo());
                
                String titulo = ConsultaPeliculasOMDb.obtenerTitulo(datosPelicula);
                String anio = ConsultaPeliculasOMDb.obtenerAnio(datosPelicula);
                String sinopsis = ConsultaPeliculasOMDb.obtenerSinopsis(datosPelicula);
                
                SwingUtilities.invokeLater(() -> {
                    dialogo.getContentPane().removeAll();

                    JPanel panelTitulo = new JPanel();
                    panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));
                    panelTitulo.setBackground(new Color(50, 100, 150));
                    panelTitulo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

                    JLabel lblTit = new JLabel(titulo);
                    lblTit.setFont(new Font("Arial", Font.BOLD, 18));
                    lblTit.setForeground(Color.WHITE);
                    lblTit.setAlignmentX(Component.LEFT_ALIGNMENT);

                    JLabel lblAn = new JLabel("Año: " + anio);
                    lblAn.setFont(new Font("Arial", Font.PLAIN, 14));
                    lblAn.setForeground(new Color(200, 200, 200));
                    lblAn.setAlignmentX(Component.LEFT_ALIGNMENT);

                    panelTitulo.add(lblTit);
                    panelTitulo.add(Box.createVerticalStrut(5));
                    panelTitulo.add(lblAn);

                    dialogo.add(panelTitulo, BorderLayout.NORTH);

                    JPanel panelSinopsis = new JPanel();
                    panelSinopsis.setLayout(new BorderLayout());
                    panelSinopsis.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

                    JLabel lblSinopsisTitulo = new JLabel("Sinopsis:");
                    lblSinopsisTitulo.setFont(new Font("Arial", Font.BOLD, 14));

                    JTextArea txtSinopsis = new JTextArea(sinopsis);
                    txtSinopsis.setEditable(false);
                    txtSinopsis.setLineWrap(true);
                    txtSinopsis.setWrapStyleWord(true);
                    txtSinopsis.setFont(new Font("Arial", Font.PLAIN, 12));
                    txtSinopsis.setBackground(new Color(245, 245, 245));
                    txtSinopsis.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                    JScrollPane scrollSinopsis = new JScrollPane(txtSinopsis);
                    scrollSinopsis.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                    panelSinopsis.add(lblSinopsisTitulo, BorderLayout.NORTH);
                    panelSinopsis.add(scrollSinopsis, BorderLayout.CENTER);

                    dialogo.add(panelSinopsis, BorderLayout.CENTER);

                    JPanel panelBotones = new JPanel();
                    panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

                    JButton btnCerrar = new JButton("Cerrar");
                    btnCerrar.addActionListener(e -> dialogo.dispose());

                    panelBotones.add(btnCerrar);
                    dialogo.add(panelBotones, BorderLayout.SOUTH);

                    dialogo.revalidate();
                    dialogo.repaint();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(dialogo, 
                            "Error al obtener la sinopsis: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    dialogo.dispose();
                });
            }
        }).start();
    }

    /**
     * Carga el poster de una película de forma asíncrona desde su URL.
     */
    private void cargarImagenAsync(String urlString, JLabel labelDestino) {
        Thread hilo = new Thread(() -> {
            try {
                if (urlString == null || urlString.isEmpty())
                    throw new Exception("No URL");
                URL url = new URL(urlString);
                BufferedImage img = ImageIO.read(url);
                if (img != null) {
                    Image scaled = img.getScaledInstance(150, 225, Image.SCALE_SMOOTH);
                    ImageIcon icon = new ImageIcon(scaled);
                    SwingUtilities.invokeLater(() -> {
                        labelDestino.setText("");
                        labelDestino.setIcon(icon);
                    });
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> labelDestino.setText("Sin Imagen"));
            }
        });
        hilo.start();
    }

    /**
     * Muestra la ventana modal.
     */
    public void mostrar() {
        this.setVisible(true);
    }
}
