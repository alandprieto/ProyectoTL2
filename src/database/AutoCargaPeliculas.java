package database;

import servicio.AppImple;
import modelo.Pelicula;
import modelo.Staff;
import enums.GeneroPelicula;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AutoCargaPeliculas {

    /**
     * Busca el archivo `database/movies_database.csv` relativo al directorio de
     * trabajo
     * y, si existe, lo procesa e inserta las películas usando el servicio.
     *
     * Este enfoque evita rutas absolutas y es reproducible para otros
     * desarrolladores
     * que coloquen el CSV dentro de la carpeta `database` del proyecto.
     */
    public static void cargarSiExiste(AppImple servicio) {
        if (servicio.hayPeliculasCargadas()) {
            System.out.println("--> La base de datos ya tiene películas. Se omite la carga del CSV.");
            return; // ¡Aquí se detiene! No lee el archivo ni inserta nada.
        }
        File f = new File("database/movies_database.csv");
        // También aceptamos si el CSV está en `src/database` (p. ej. si descargaste
        // allí)
        if (!f.exists()) {
            File alt = new File("src/database/movies_database.csv");
            if (alt.exists()) {
                f = alt;
            }
        }
        if (!f.exists()) {
            return; // No hay archivo, nada que hacer
        }

        int cargadas = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            // Saltamos la cabecera si existe
            br.readLine();

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(
                        ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (datos.length < 9)
                    continue;

                try {
                    Pelicula p = new Pelicula();
                    p.setTitulo(datos[1].replace("\"", ""));

                    try {
                        p.setAnio(LocalDate.parse(datos[0]).getYear());
                    } catch (DateTimeParseException dtp) {
                        p.setAnio(2000);
                    }

                    try {
                        p.setRatingPromedio(Double.parseDouble(datos[5]));
                    } catch (Exception ex) {
                        p.setRatingPromedio(0.0);
                    }

                    p.setPosterURL(datos[8]);

                    // Mapeo sencillo de género: normalizamos y usamos valueOf, con fallback
                    String generoRaw = datos[7].replace("\"", "").trim().toUpperCase().replace(" ", "_")
                            .replace("-", "_");
                    try {
                        p.setGenero(GeneroPelicula.valueOf(generoRaw));
                    } catch (Exception ex) {
                        p.setGenero(GeneroPelicula.ACCION);
                    }

                    p.setDuracion(java.time.Duration.ofMinutes(120));
                    p.setDirector(new Staff("Director CSV", "Director"));

                    servicio.registrarPelicula(p);
                    cargadas++;
                } catch (Exception ex) {
                    System.err.println("AutoCarga: error procesando línea: " + ex.getMessage());
                }
            }

            System.out.println("AutoCarga: se cargaron " + cargadas + " películas desde database/movies_database.csv");

        } catch (Exception e) {
            System.err.println("AutoCarga error: " + e.getMessage());
        }
    }
}
