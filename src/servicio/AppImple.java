package servicio;

import java.util.List;
import java.util.Collections;
import dao.*;
import database.ConexionBD;
import excepciones.*;
import modelo.*;

public class AppImple {
    private UsuarioDAO usuarioDAO;
    private PeliculaDAO peliculaDAO;
    private ReseñaDAO reseñaDAO;

    public AppImple() {
        this.usuarioDAO = new UsuarioDAOimple();
        this.peliculaDAO = new PeliculaDAOimple();
        this.reseñaDAO = new ReseñaDAOimple();
        ConexionBD.getConnection();
    }

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

    public Usuario autenticarUsuario(String email, String password) throws CredencialesInvalidasException {
        Usuario usuario = this.usuarioDAO.autenticar(email, password);
        if (usuario == null) {
            throw new CredencialesInvalidasException("Usuario o contraseña incorrectos.");
        }
        return usuario;
    }

    public void registrarReseña(Reseña nuevaReseña) throws DatoInvalidoException {
        // Validación de Rango
        if (nuevaReseña.getCalificacion() < 1 || nuevaReseña.getCalificacion() > 10) {
            throw new DatoInvalidoException("La calificación debe ser entre 1 y 10.");
        }

        // --- NUEVA VALIDACIÓN: Comentario Obligatorio ---
        if (nuevaReseña.getComentario() == null || nuevaReseña.getComentario().trim().isEmpty()) {
            throw new DatoInvalidoException("Debes escribir un comentario para tu reseña.");
        }

        // Validación de Unicidad (Solo una reseña por usuario por película)
        if (reseñaDAO.existeResena(nuevaReseña.getUsuario().getID(), nuevaReseña.getIDContenido())) {
            throw new DatoInvalidoException("Ya has calificado esta película anteriormente.");
        }

        this.reseñaDAO.guardar(nuevaReseña);
    }

    // Métodos delegados
    public List<Pelicula> buscarPeliculasPorTitulo(String titulo) {
        return peliculaDAO.buscarPorTitulo(titulo);
    }

    public boolean hayPeliculasCargadas() {
        return !peliculaDAO.listarTodas().isEmpty();
    }

    public void registrarPelicula(Pelicula p) {
        peliculaDAO.guardar(p);
    }

    public List<Pelicula> obtenerTop10Peliculas() {
        List<Pelicula> todas = peliculaDAO.listarTodas();
        if (todas == null || todas.isEmpty())
            return todas;
        // Ordenar por rating descendente y tomar top 10
        todas.sort((p1, p2) -> Double.compare(p2.getRatingPromedio(), p1.getRatingPromedio()));
        return todas.subList(0, Math.min(10, todas.size()));
    }

    public List<Pelicula> obtenerPeliculasExplorar() {
        List<Pelicula> todas = peliculaDAO.listarTodas();
        if (todas == null || todas.isEmpty())
            return todas;
        Collections.shuffle(todas);
        return todas.subList(0, Math.min(10, todas.size()));
    }

    public boolean yaCalificoUsuario(int idUsuario, int idPelicula) {
        return reseñaDAO.existeResena(idUsuario, idPelicula);
    }

    public boolean haVistoTop10(int idUsuario) {
        return usuarioDAO.haVistoTop10(idUsuario);
    }

    public void marcarVioTop10(int idUsuario) {
        usuarioDAO.marcarVioTop10(idUsuario);
    }
}