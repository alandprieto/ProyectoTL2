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
        // Reutiliza la ventana `VistaDetallesPelicula` para evitar duplicación de UI.
        JDialog dialogoCarga = new JDialog(this, "Consultando OMDb...", true);
        dialogoCarga.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialogoCarga.setSize(300, 100);
        dialogoCarga.setLocationRelativeTo(this);
        JProgressBar progress = new JProgressBar();
        progress.setIndeterminate(true);
        dialogoCarga.add(progress);

        new Thread(() -> {
            try {
                JSONObject datosPelicula = ConsultaPeliculasOMDb.consultarPelicula(p.getTitulo());

                SwingUtilities.invokeLater(() -> {
                    dialogoCarga.dispose();

                    if (datosPelicula != null) {
                        String titulo = ConsultaPeliculasOMDb.obtenerTitulo(datosPelicula);
                        String anio = ConsultaPeliculasOMDb.obtenerAnio(datosPelicula);
                        String sinopsis = ConsultaPeliculasOMDb.obtenerSinopsis(datosPelicula);
                        String rating = ConsultaPeliculasOMDb.obtenerRating(datosPelicula);

                        VistaDetallesPelicula ventanaDetalles = new VistaDetallesPelicula((JFrame) this.getParent(), titulo, anio, sinopsis, rating);
                        ventanaDetalles.mostrar();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "No se encontró la película '" + p.getTitulo() + "' en OMDb.",
                                "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    dialogoCarga.dispose();
                    JOptionPane.showMessageDialog(this,
                            "Error al obtener la sinopsis: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();

        dialogoCarga.setVisible(true);
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
