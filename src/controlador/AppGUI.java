package controlador; // O donde pongas tu main

import database.SetupBD;
import servicio.AppImple; // El Modelo/Servicio (M)
import database.AutoCargaPeliculas;
import vista.VistaPrincipal; // La Vista (V)
import javax.swing.SwingUtilities;

public class AppGUI {

    public static void main(String[] args) {

        // 1. Inicia la Base de Datos (como hacías en App.java)
        SetupBD.crearTablas();
        // CargaDatosPrueba.cargarDatos(); // Comenta esto si no quieres los datos de
        // prueba

        // 2. Crear las 3 partes del MVC (creamos servicio y vista ahora)
        AppImple servicio = new AppImple(); // <-- El Modelo/Servicio (M)
        VistaPrincipal vista = new VistaPrincipal(); // <-- La Vista (V)

        // 3. Ejecutar la inicialización de la UI y la carga en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Cargar automáticamente películas desde `database/movies_database.csv` si
                // existe
                AutoCargaPeliculas.cargarSiExiste(servicio);

                // Crear el Controlador y conectarlos
                new ControladorPrincipal(servicio, vista); // <-- El Controlador (C)

                // Hacer visible la vista
                vista.iniciar();
            } catch (Exception ex) {
                ex.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null, "Error al iniciar la aplicación: " + ex.getMessage(),
                        "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}