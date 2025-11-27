package controlador;

import modelo.Pelicula;
import modelo.Reseña;
import modelo.Usuario;
import servicio.AppImple;
import vista.VistaPrincipal;
import vista.VistaLogin;
import excepciones.DatoInvalidoException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

public class ControladorPrincipal implements ActionListener {
    private AppImple servicio;
    private VistaPrincipal vista;
    private Usuario usuario;
    private List<Pelicula> peliculasActuales; // Guarda el listado actual para refrescar

    public ControladorPrincipal(AppImple servicio, VistaPrincipal vista, Usuario usuario) {
        this.servicio = servicio;
        this.vista = vista;
        this.usuario = usuario;

        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnExplorar.addActionListener(this);
        this.vista.btnCerrarSesion.addActionListener(this);
        // Título siempre muestra el nombre del usuario
        this.vista.setTitle("Streaming - Usuario: " + usuario.getNombre());

        // Si el usuario nunca vio el Top10, mostramos un cartel de bienvenida + Top10
        boolean yaVio = false;
        try {
            yaVio = servicio.haVistoTop10(usuario.getID()) || usuario.isVistoTop10();
        } catch (Exception ex) {
            yaVio = usuario.isVistoTop10();
        }

        if (!yaVio) {
            // Mostrar cartel de bienvenida
            String mensaje = "BIENVENIDO: sabemos que te gustan las peliculas, aca estan las 10 mejores de nuestra plataforma ¡calificalas!";
            try {
                if (vista.lblBienvenida != null) {
                    vista.lblBienvenida.setText(mensaje);
                    vista.lblBienvenida.setVisible(true);
                }
            } catch (Exception ex) {
                System.err.println("No se pudo mostrar lblBienvenida: " + ex.getMessage());
            }

            cargarPeliculas(servicio.obtenerTop10Peliculas());
            // Marcamos que ya se le mostró para futuras entradas (DB + objeto en memoria)
            try {
                servicio.marcarVioTop10(usuario.getID());
                usuario.setVistoTop10(true);
            } catch (Exception ex) {
                System.err.println("No se pudo marcar VioTop10: " + ex.getMessage());
            }
        } else {
            // Ocultar cartel si existía y mostrar aleatorias
            if (vista.lblBienvenida != null) {
                vista.lblBienvenida.setVisible(false);
            }
            cargarPeliculas(servicio.obtenerPeliculasExplorar());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnBuscar) {
            // Ocultar banner si está visible antes de mostrar resultados
            if (vista.lblBienvenida != null && vista.lblBienvenida.isVisible()) {
                vista.lblBienvenida.setVisible(false);
            }
            String q = vista.txtBusqueda.getText();
            cargarPeliculas(servicio.buscarPeliculasPorTitulo(q));
        } else if (e.getSource() == vista.btnExplorar) {
            if (vista.lblBienvenida != null && vista.lblBienvenida.isVisible()) {
                vista.lblBienvenida.setVisible(false);
            }
            cargarPeliculas(servicio.obtenerPeliculasExplorar());
        } else if (e.getSource() == vista.btnCerrarSesion) {
            // Cerrar sesión: volver a la vista de login
            try {
                VistaLogin vLogin = new VistaLogin();
                new ControladorLogin(servicio, vLogin);
                vLogin.iniciar();
            } catch (Exception ex) {
                System.err.println("Error al abrir login: " + ex.getMessage());
            }
            // Cerramos la ventana actual
            vista.dispose();
        }
    }

    private void cargarPeliculas(List<Pelicula> peliculas) {
        this.peliculasActuales = peliculas; // Guardar para refrescar después
        vista.panelResultados.removeAll();
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

            JLabel lblRating = new JLabel("★ " + p.getRatingPromedio() + "/10");
            lblRating.setForeground(new Color(255, 140, 0));
            lblRating.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton btnVotar = new JButton("Calificar");
            btnVotar.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Verificar si el usuario ya calificó esta película
            if (servicio.yaCalificoUsuario(usuario.getID(), p.getID())) {
                btnVotar.setText("Ya calificada");
                btnVotar.setEnabled(false);
            } else {
                btnVotar.addActionListener(ev -> mostrarDialogoCalificacion(p));
            }

            tarjeta.add(Box.createVerticalStrut(10));
            tarjeta.add(lblImg);
            tarjeta.add(Box.createVerticalStrut(5));
            tarjeta.add(lblTitulo);
            tarjeta.add(lblRating);
            tarjeta.add(Box.createVerticalStrut(5));
            tarjeta.add(btnVotar);

            vista.panelResultados.add(tarjeta);
        }
        vista.panelResultados.revalidate();
        vista.panelResultados.repaint();
    }

    private void mostrarDialogoCalificacion(Pelicula p) {
        JDialog dialogo = new JDialog(vista, "Calificar: " + p.getTitulo(), true);
        dialogo.setSize(500, 350); // Un poco más alto para que entre todo bien
        dialogo.setLocationRelativeTo(vista);
        dialogo.setLayout(new BorderLayout());

        // 1. Título
        JLabel lblTitulo = new JLabel("Selecciona tu puntuación:", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialogo.add(lblTitulo, BorderLayout.NORTH);

        // 2. Botones 1-10
        JPanel panelBotones = new JPanel(new GridLayout(2, 5, 5, 5));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        ButtonGroup grupoBotones = new ButtonGroup();
        final int[] puntajeSeleccionado = { 0 };

        for (int i = 1; i <= 10; i++) {
            JToggleButton btn = new JToggleButton(String.valueOf(i));
            int valor = i;
            btn.addActionListener(e -> puntajeSeleccionado[0] = valor);
            grupoBotones.add(btn);
            panelBotones.add(btn);
        }
        dialogo.add(panelBotones, BorderLayout.CENTER);

        // 3. Comentario + Botón
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JTextArea txtComentario = new JTextArea(4, 20);
        // CAMBIO VISUAL: Indicamos que es obligatorio
        txtComentario.setBorder(BorderFactory.createTitledBorder("Tu comentario (Obligatorio)"));
        txtComentario.setLineWrap(true);
        txtComentario.setWrapStyleWord(true);
        panelSur.add(new JScrollPane(txtComentario), BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Enviar Reseña");
        btnGuardar.setBackground(new Color(50, 150, 50));
        btnGuardar.setForeground(Color.WHITE);

        // --- LÓGICA DEL BOTÓN ---
        btnGuardar.addActionListener(e -> {
            // Validación 1: Puntaje
            if (puntajeSeleccionado[0] == 0) {
                JOptionPane.showMessageDialog(dialogo, "Por favor selecciona un puntaje del 1 al 10.");
                return;
            }

            // Validación 2: Comentario (CAMBIO SOLICITADO)
            String textoComentario = txtComentario.getText().trim();
            if (textoComentario.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "El comentario es obligatorio para enviar la reseña.",
                        "Falta información", JOptionPane.WARNING_MESSAGE);
                return;
            }

            guardarResena(p, textoComentario, puntajeSeleccionado[0], dialogo);
        });

        JPanel panelBotonWrapper = new JPanel();
        panelBotonWrapper.add(btnGuardar);
        panelSur.add(panelBotonWrapper, BorderLayout.SOUTH);

        dialogo.add(panelSur, BorderLayout.SOUTH);
        dialogo.setVisible(true);
    }

    private void guardarResena(Pelicula p, String comentario, int puntaje, JDialog dialogo) {
        try {
            Reseña r = new Reseña(usuario, p.getID(), comentario, puntaje);
            servicio.registrarReseña(r);
            JOptionPane.showMessageDialog(vista, "¡Gracias! Tu reseña ha sido guardada.");
            dialogo.dispose();

            // Refrescar el listado actual para que el botón se actualice a "Ya calificada"
            if (peliculasActuales != null) {
                cargarPeliculas(peliculasActuales);
            }

        } catch (DatoInvalidoException ex) {
            JOptionPane.showMessageDialog(dialogo, ex.getMessage(), "Error en datos", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialogo, "Error inesperado: " + ex.getMessage());
        }
    }

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
}