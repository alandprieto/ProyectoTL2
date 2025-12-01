package servicio;

import java.util.List;
import java.util.Collections;
import dao.*;
import database.ConexionBD;
import excepciones.*;
import modelo.*;

/**
 * Implementación del servicio de aplicación con lógica de negocio.
 */
public class AppImple {
    private UsuarioDAO usuarioDAO;
    private PeliculaDAO peliculaDAO;
    private ReseñaDAO reseñaDAO;

    /**
     * Constructor que inicializa los DAOs.
     */
    public AppImple() {
        this.usuarioDAO = new UsuarioDAOimple();
        this.peliculaDAO = new PeliculaDAOimple();
        this.reseñaDAO = new ReseñaDAOimple();
        ConexionBD.getConnection();
    }

    /**
     * Registra un nuevo cliente en la base de datos.
     */
    public void registrarCliente(Cliente cliente) throws UsuarioYaExisteException {
        if (usuarioDAO.emailExiste(cliente.getEmail())) {
            throw new UsuarioYaExisteException("El email ya está registrado.");
        }
        if (usuarioDAO.dniExiste(cliente.getDNI())) {
            throw new UsuarioYaExisteException("El DNI ya está registrado.");
        }
        boolean exito = this.usuarioDAO.guardar(cliente);
        if (!exito) {
            throw new RuntimeException("Error en base de datos al guardar cliente.");
        }
    }

    /**
     * Autentica un usuario con email y contraseña.
     */
    public Usuario autenticarUsuario(String email, String password) throws CredencialesInvalidasException {
        Usuario usuario = this.usuarioDAO.autenticar(email, password);
        if (usuario == null) {
            throw new CredencialesInvalidasException("Usuario o contraseña incorrectos.");
        }
        return usuario;
    }

    /**
     * Registra una nueva reseña con validaciones.
     */
    public void registrarReseña(Reseña nuevaReseña) throws DatoInvalidoException {
        if (nuevaReseña.getCalificacion() < 1 || nuevaReseña.getCalificacion() > 10) {
            throw new DatoInvalidoException("La calificación debe ser entre 1 y 10.");
        }

        if (nuevaReseña.getComentario() == null || nuevaReseña.getComentario().trim().isEmpty()) {
            throw new DatoInvalidoException("Debes escribir un comentario para tu reseña.");
        }

        if (reseñaDAO.existeResena(nuevaReseña.getUsuario().getID(), nuevaReseña.getIDContenido())) {
            throw new DatoInvalidoException("Ya has calificado esta película anteriormente.");
        }

        this.reseñaDAO.guardar(nuevaReseña);
    }

    /**
     * Busca películas por título.
     */
    public List<Pelicula> buscarPeliculasPorTitulo(String titulo) {
        return peliculaDAO.buscarPorTitulo(titulo);
    }

    /**
     * Verifica si hay películas cargadas en la base de datos.
     */
    public boolean hayPeliculasCargadas() {
        return !peliculaDAO.listarTodas().isEmpty();
    }

    /**
     * Registra una nueva película en la base de datos.
     */
    public void registrarPelicula(Pelicula p) {
        peliculaDAO.guardar(p);
    }

    /**
     * Obtiene las 10 películas mejor calificadas.
     */
    public List<Pelicula> obtenerTop10Peliculas() {
        List<Pelicula> todas = peliculaDAO.listarTodas();
        if (todas == null || todas.isEmpty())
            return todas;
        todas.sort((p1, p2) -> Double.compare(p2.getRatingPromedio(), p1.getRatingPromedio()));
        return todas.subList(0, Math.min(10, todas.size()));
    }

    /**
     * Obtiene 10 películas aleatorias para explorar.
     */
    public List<Pelicula> obtenerPeliculasExplorar() {
        List<Pelicula> todas = peliculaDAO.listarTodas();
        if (todas == null || todas.isEmpty())
            return todas;
        Collections.shuffle(todas);
        return todas.subList(0, Math.min(10, todas.size()));
    }

    /**
     * Verifica si un usuario ya calificó una película.
     */
    public boolean yaCalificoUsuario(int idUsuario, int idPelicula) {
        return reseñaDAO.existeResena(idUsuario, idPelicula);
    }

    /**
     * Verifica si un usuario vio el Top 10.
     */
    public boolean haVistoTop10(int idUsuario) {
        return usuarioDAO.haVistoTop10(idUsuario);
    }

    /**
     * Marca que un usuario vio el Top 10.
     */
    public void marcarVioTop10(int idUsuario) {
        usuarioDAO.marcarVioTop10(idUsuario);
    }
}