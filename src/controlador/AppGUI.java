package controlador;

import database.SetupBD;
import database.AutoCargaPeliculas;
import servicio.AppImple;
import vista.VistaLogin;
import javax.swing.SwingUtilities;

public class AppGUI {

    public static void main(String[] args) {
        SetupBD.crearTablas();
        AppImple servicio = new AppImple();
        AutoCargaPeliculas.cargarSiExiste(servicio);

        SwingUtilities.invokeLater(() -> {
            VistaLogin vistaLogin = new VistaLogin();
            new ControladorLogin(servicio, vistaLogin);
            vistaLogin.iniciar();
        });
    }
}