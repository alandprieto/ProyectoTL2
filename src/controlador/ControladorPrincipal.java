package controlador;

import modelo.Pelicula;
import servicio.AppImple;
import vista.VistaPrincipal;

import javax.imageio.ImageIO; // Para leer imágenes
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage; // Para manipular la imagen
import java.net.URL; // Para la dirección web
import java.util.Collections;
import java.util.List;

public class ControladorPrincipal implements ActionListener {

    private AppImple servicio;
    private VistaPrincipal vista;

    public ControladorPrincipal(AppImple servicio, VistaPrincipal vista) {
        this.servicio = servicio;
        this.vista = vista;

        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnAdmin.addActionListener(this);
        this.vista.btnExplorar.addActionListener(this);

        // Carga inicial: Top 10
        cargarTop10Mejores();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnBuscar) {
            hacerBusqueda();
        } else if (e.getSource() == vista.btnExplorar) {
            vista.txtBusqueda.setText("");
            cargarPeliculasAleatorias();
        } else if (e.getSource() == vista.btnAdmin) {
            JOptionPane.showMessageDialog(vista, "Funcionalidad de Admin en construcción.");
        }
    }

    // --- MÉTODOS LÓGICOS (Sin cambios importantes) ---

    private void cargarPeliculasAleatorias() {
        List<Pelicula> todas = servicio.listarPeliculas(1); // Usar método público
        if (todas.isEmpty())
            return;
        Collections.shuffle(todas);
        int limite = Math.min(todas.size(), 10);
        actualizarPanelResultados(todas.subList(0, limite));
    }

    // Mostrar todas las películas ordenadas por título
    private void mostrarTodasLasPeliculas() {
        List<Pelicula> todas = servicio.listarPeliculas(1);
        actualizarPanelResultados(todas);
    }

    private void cargarTop10Mejores() {
        List<Pelicula> todas = servicio.listarPeliculas(1); // Orden por título, pero luego reordenamos por rating
        todas.sort((p1, p2) -> Double.compare(p2.getRatingPromedio(), p1.getRatingPromedio()));
        int limite = Math.min(todas.size(), 10);
        actualizarPanelResultados(todas.subList(0, limite));
        System.out.println("--> Mostrando Top " + limite + " películas por rating.");
    }

    private void hacerBusqueda() {
        String texto = vista.txtBusqueda.getText().trim();
        if (texto.isEmpty()) {
            mostrarTodasLasPeliculas();
        } else {
            List<Pelicula> resultados = servicio.buscarPeliculasPorTitulo(texto);
            actualizarPanelResultados(resultados);
        }
    }

    // --- MÉTODOS VISUALES (Aquí está la magia de las imágenes) ---

    private void actualizarPanelResultados(List<Pelicula> listaPeliculas) {
        vista.panelResultados.removeAll();

        if (listaPeliculas.isEmpty()) {
            vista.panelResultados.add(new JLabel("No se encontraron películas."));
        } else {
            for (Pelicula p : listaPeliculas) {
                JPanel tarjeta = new JPanel();
                tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                tarjeta.setBackground(Color.WHITE);

                // Hacemos la tarjeta un poco más grande para que entre la imagen
                // Ancho fijo 180, Alto variable
                tarjeta.setPreferredSize(new Dimension(180, 300));

                // 1. Label para la imagen (Inicia con texto cargando)
                JLabel lblImagen = new JLabel("Cargando imagen...", SwingConstants.CENTER);
                lblImagen.setPreferredSize(new Dimension(150, 225)); // Tamaño estandar de poster
                lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);

                // BORDE NEGRO FINO alrededor de la imagen (Opcional, queda bonito)
                lblImagen.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

                // 2. Llamamos al método asíncrono para cargar la foto
                cargarImagenAsync(p.getPosterURL(), lblImagen);

                // Datos de texto
                String titulo = p.getTitulo();
                if (titulo.length() > 20)
                    titulo = titulo.substring(0, 18) + "...";

                JLabel lblTitulo = new JLabel("<html><center><b>" + titulo + "</b></center></html>",
                        SwingConstants.CENTER);
                JLabel lblRating = new JLabel(String.format("⭐ %.1f", p.getRatingPromedio()));

                // Estilos
                lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
                lblRating.setAlignmentX(Component.CENTER_ALIGNMENT);

                if (p.getRatingPromedio() >= 8.0) {
                    lblRating.setForeground(new Color(0, 100, 0)); // Verde
                }

                // Armado de la tarjeta
                tarjeta.add(lblImagen);
                tarjeta.add(Box.createVerticalStrut(10));
                tarjeta.add(lblTitulo);
                tarjeta.add(Box.createVerticalStrut(5));
                tarjeta.add(lblRating);

                vista.panelResultados.add(tarjeta);
            }
        }

        vista.panelResultados.revalidate();
        vista.panelResultados.repaint();
    }

    /**
     * Este método crea un hilo separado para descargar la imagen
     * sin congelar la interfaz gráfica.
     */
    private void cargarImagenAsync(String urlString, JLabel labelDestino) {
        // Creamos un nuevo hilo (Thread)
        Thread hiloCarga = new Thread(() -> {
            try {
                // A. Verificamos si hay URL
                if (urlString == null || urlString.isEmpty()) {
                    SwingUtilities.invokeLater(() -> labelDestino.setText("Sin imagen"));
                    return;
                }

                // B. Descargamos la imagen de internet (Esto tarda un poco)
                URL url = new URL(urlString);
                BufferedImage imagenOriginal = ImageIO.read(url);

                if (imagenOriginal != null) {
                    // C. Redimensionamos la imagen para que quepa en la tarjeta (150x225 aprox)
                    Image imagenRedimensionada = imagenOriginal.getScaledInstance(150, 225, Image.SCALE_SMOOTH);
                    ImageIcon icono = new ImageIcon(imagenRedimensionada);

                    // D. Actualizamos la interfaz (SIEMPRE dentro de invokeLater)
                    SwingUtilities.invokeLater(() -> {
                        labelDestino.setText(""); // Borramos el texto "Cargando..."
                        labelDestino.setIcon(icono);
                    });
                } else {
                    SwingUtilities.invokeLater(() -> labelDestino.setText("Img Error"));
                }

            } catch (Exception e) {
                // Si falla (ej: sin internet), mostramos error en el label
                SwingUtilities.invokeLater(() -> labelDestino.setText("Error carga"));
                // System.err.println("No se pudo cargar imagen: " + urlString);
            }
        });

        // Iniciamos el hilo
        hiloCarga.start();
    }
}