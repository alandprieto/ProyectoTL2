package controlador;

import database.SetupBD;
import database.AutoCargaPeliculas;
import servicio.AppImple;
import vista.VistaLogin;
import javax.swing.SwingUtilities;

public class AppGUI {

    public static void main(String[] args) {

        // 1. Configuración inicial (Base de datos)
        SetupBD.crearTablas();

        // 2. Instanciamos el Modelo/Servicio
        AppImple servicio = new AppImple();

        // 3. Carga automática de datos (si aplica)
        AutoCargaPeliculas.cargarSiExiste(servicio);

        // 4. Iniciar la Interfaz Gráfica
        SwingUtilities.invokeLater(() -> {
            try {
                // --- CAMBIO IMPORTANTE ---
                // En lugar de VistaPrincipal, instanciamos el Login
                VistaLogin vistaLogin = new VistaLogin();

                // Creamos el controlador del Login
                new ControladorLogin(servicio, vistaLogin);

                // Mostramos el Login
                vistaLogin.iniciar();

            } catch (Exception ex) {
                ex.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null, "Error fatal: " + ex.getMessage());
            }
        });
    }
}