package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.ConexionBD;
import modelo.Administrador;
import modelo.Cliente;
import modelo.Usuario;

public class UsuarioDAOimple implements UsuarioDAO {

    @Override
    public boolean guardar(Usuario usuario) {
        // CORREGIDO: Nombres de columnas exactos como en SetupBD
        // CORREGIDO: Usamos TipoUsuario en lugar de ROL
        String sql = "INSERT INTO Usuario (DNI, Nombre, Apellido, Email, Contrasena, TipoUsuario) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = ConexionBD.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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
                    }
                }
                return true; // ¡ÉXITO!
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
        }
        return false; // FALLÓ
    }

    @Override
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM Usuario WHERE ID = ?";
        Connection conn = ConexionBD.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuario";
        Connection conn = ConexionBD.getConnection();

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario usuario = mapResultSetToUsuario(rs);
                if (usuario != null) {
                    usuarios.add(usuario);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM Usuario WHERE ID = ?";
        Connection conn = ConexionBD.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("--> Usuario con ID " + id + " eliminado correctamente.");
            } else {
                System.out.println("No se encontró usuario con ID " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
    }

    @Override
    public Usuario autenticar(String email, String contrasena) {
        // CORREGIDO: Email y Contrasena (Capitalizados)
        String sql = "SELECT * FROM Usuario WHERE Email = ? AND Contrasena = ?";
        Connection conn = ConexionBD.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, contrasena);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en autenticación: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean dniExiste(long dni) {
        // CORREGIDO: DNI en mayúsculas (o como esté en SetupBD, normalmente es DNI)
        String sql = "SELECT 1 FROM Usuario WHERE DNI = ?";
        Connection conn = ConexionBD.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, dni);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar DNI: " + e.getMessage());
            return false;
        }
    }

    // --- MÉTODO NUEVO QUE DABA ERROR ---
    @Override
    public boolean emailExiste(String email) {
        // CORREGIDO: "Email" en lugar de "EMAIL"
        String sql = "SELECT 1 FROM Usuario WHERE Email = ?";
        Connection conn = ConexionBD.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar email: " + e.getMessage());
            return false;
        }
    }

    // Método auxiliar para mapear ResultSet a objeto
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        // CORREGIDO: Usamos TipoUsuario y las columnas con la mayúscula inicial
        // correcta
        String tipo = rs.getString("TipoUsuario");
        Usuario usuario = null;

        if ("ADMIN".equalsIgnoreCase(tipo)) {
            usuario = new Administrador();
        } else {
            usuario = new Cliente();
        }

        usuario.setID(rs.getInt("ID"));
        usuario.setDNI(rs.getLong("DNI"));
        usuario.setNombre(rs.getString("Nombre"));
        usuario.setApellido(rs.getString("Apellido"));
        usuario.setEmail(rs.getString("Email"));
        usuario.setContrasena(rs.getString("Contrasena"));

        return usuario;
    }
}