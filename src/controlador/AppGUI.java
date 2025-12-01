package controlador;

import database.SetupBD;
import servicio.AppImple;
import vista.VistaLogin;
import javax.swing.SwingUtilities;

public class AppGUI {

    public static void main(String[] args) {
        SetupBD.crearTablas();
        AppImple servicio = new AppImple();

        SwingUtilities.invokeLater(() -> {
            VistaLogin vistaLogin = new VistaLogin();
            new ControladorLogin(servicio, vistaLogin);
            vistaLogin.iniciar();
        });
    }
}