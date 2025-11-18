package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.ConexionBD;
import modelo.Administrador;
import modelo.Cliente;
import modelo.Usuario;

public class UsuarioDAOimple implements UsuarioDAO {

    @Override
    public boolean guardar(Usuario usuario) {
        String sql = "INSERT INTO USUARIO (DNI, NOMBRE, APELLIDO, EMAIL, CONTRASENA, ROL) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = ConexionBD.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, usuario.getDNI());
            pstmt.setString(2, usuario.getNombre());
            pstmt.setString(3, usuario.getApellido());
            pstmt.setString(4, usuario.getEmail());
            pstmt.setString(5, usuario.getContrasena());

            if (usuario instanceof Administrador) {
                pstmt.setString(6, "ADMIN");
            } else {
                pstmt.setString(6, "CLIENTE");
            }

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usuario.setID(generatedKeys.getInt(1));
                        System.out.println(
                                "--> Usuario '" + usuario.getEmail() + "' guardado con ID: " + usuario.getID());
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al guardar el usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Usuario autenticar(String email, String contrasena) {
        String sql = "SELECT * FROM USUARIO WHERE EMAIL = ? AND CONTRASENA = ?";
        Connection conn = ConexionBD.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, contrasena);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String rol = rs.getString("ROL");
                    Long dni = rs.getLong("DNI");
                    String nombre = rs.getString("NOMBRE");
                    String apellido = rs.getString("APELLIDO");

                    Usuario usuario;
                    if ("ADMIN".equals(rol)) {
                        usuario = new Administrador(dni, nombre, apellido, email, contrasena);
                    } else {
                        usuario = new Cliente(dni, nombre, apellido, email, contrasena);
                    }
                    usuario.setID(rs.getInt("ID"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error de autenticación: " + e.getMessage());
        }

        return null; 
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM USUARIO";
        Connection conn = ConexionBD.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String rol = rs.getString("ROL");
                Long dni = rs.getLong("DNI");
                String nombre = rs.getString("NOMBRE");
                String apellido = rs.getString("APELLIDO");
                String email = rs.getString("EMAIL");
                String contrasena = rs.getString("CONTRASENA");

                Usuario usuario;
                if ("ADMIN".equals(rol)) {
                    usuario = new Administrador(dni, nombre, apellido, email, contrasena);
                } else {
                    usuario = new Cliente(dni, nombre, apellido, email, contrasena);
                }
                usuario.setID(rs.getInt("ID"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    @Override
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM USUARIO WHERE ID = ?";
        Connection conn = ConexionBD.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String rol = rs.getString("ROL");
                    Long dni = rs.getLong("DNI");
                    String nombre = rs.getString("NOMBRE");
                    String apellido = rs.getString("APELLIDO");
                    String email = rs.getString("EMAIL");
                    String contrasena = "";

                    Usuario usuario;
                    if ("ADMIN".equals(rol)) {
                        usuario = new Administrador(dni, nombre, apellido, email, contrasena);
                    } else {
                        usuario = new Cliente(dni, nombre, apellido, email, contrasena);
                    }
                    usuario.setID(id);
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el usuario por ID: " + e.getMessage());
        }

        return null; 
    }

    @Override
    public void eliminar(int idUsuario) {
        String sql = "DELETE FROM USUARIO WHERE ID = ?";
        Connection conn = ConexionBD.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("--> Usuario con ID " + idUsuario + " eliminado correctamente.");
            } else {
                System.out.println("No se encontró un usuario con el ID " + idUsuario + " para eliminar.");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar el usuario: " + e.getMessage());
        }
    }

        public void actualizar(Usuario usuario) {

            String sql = "UPDATE USUARIO SET DNI = ?, NOMBRE = ?, APELLIDO = ?, EMAIL = ?, CONTRASENA = ?, ROL = ? WHERE ID = ?";

            Connection conn = ConexionBD.getConnection();

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

    

                pstmt.setLong(1, usuario.getDNI());

                pstmt.setString(2, usuario.getNombre());

                pstmt.setString(3, usuario.getApellido());

                pstmt.setString(4, usuario.getEmail());

                pstmt.setString(5, usuario.getContrasena());

    

                if (usuario instanceof Administrador) {

                    pstmt.setString(6, "ADMIN");

                } else {

                    pstmt.setString(6, "CLIENTE");

                }

    

                pstmt.setInt(7, usuario.getID());

                int filasAfectadas = pstmt.executeUpdate();

    

                if (filasAfectadas > 0) {

                    System.out.println("--> Usuario con ID " + usuario.getID() + " actualizado correctamente.");

                } else {

                    System.out.println("No se encontró un usuario con el ID " + usuario.getID() + " para actualizar.");

                }

    

            } catch (SQLException e) {

                System.err.println("Error al actualizar el usuario: " + e.getMessage());

            }

        }

    

        @Override

        public boolean dniExiste(long dni) {

            String sql = "SELECT 1 FROM USUARIO WHERE DNI = ?";

            Connection conn = ConexionBD.getConnection();

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setLong(1, dni);

                try (ResultSet rs = pstmt.executeQuery()) {

                    return rs.next();

                }

            } catch (SQLException e) {

                System.err.println("Error al verificar si el DNI existe: " + e.getMessage());

            }

            return false;

        }

    }

    