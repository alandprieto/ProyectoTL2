package database;

import servicio.AppImple;
import modelo.Pelicula;
import modelo.Staff;
import enums.GeneroPelicula;
import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AutoCargaPeliculas {

    public static void cargarSiExiste(AppImple servicio) {
        // 1. Verificar si ya hay datos
        if (servicio.hayPeliculasCargadas()) {
            System.out.println(">>> Base de datos con datos. No se requiere carga CSV.");
            return;
        }

        System.out.println(">>> Base de datos vacía. Iniciando búsqueda de CSV...");

        // 2. Buscamos el archivo en varias rutas posibles para evitar errores
        File f = new File("src/database/movies_database.csv");
        if (!f.exists()) {
            f = new File("database/movies_database.csv");
            if (!f.exists()) {
                f = new File("movies_database.csv"); // Intento en la raíz
            }
        }

        if (!f.exists()) {
            System.err.println(">>> ERROR CRÍTICO: No se encuentra 'movies_database.csv'.");
            System.err.println(">>> Asegurate de que el archivo esté en la carpeta 'src/database/'");
            System.err.println(">>> Ruta buscada: " + f.getAbsolutePath());
            return;
        }

        System.out.println(">>> Archivo encontrado en: " + f.getAbsolutePath());

        // 3. Leemos el archivo
        Connection conn = ConexionBD.getConnection();
        boolean previousAutoCommit = true;
        try {
            if (conn != null) {
                previousAutoCommit = conn.getAutoCommit();
                conn.setAutoCommit(false); // Mejor rendimiento al hacer muchas inserciones
            }
        } catch (SQLException se) {
            System.err.println("No se pudo desactivar autoCommit: " + se.getMessage());
        }

        int cargadas = 0;
        int totales = 0;
        int saltadas = 0;

        try (BufferedReader br = Files.newBufferedReader(f.toPath(), StandardCharsets.UTF_8)) {
            String linea = br.readLine(); // Saltar cabecera

            while ((linea = br.readLine()) != null) {
                totales++;
                List<String> datos = parseCSVLine(linea);

                // Verificamos que la línea tenga suficientes datos (mínimo 8 columnas)
                if (datos.size() < 8) {
                    saltadas++;
                    continue;
                }

                try {
                    Pelicula p = new Pelicula();

                    // Título (Columna 1)
                    p.setTitulo(datos.get(1).replace("\"", "").trim());

                    // Año (Columna 0 - Fecha)
                    try {
                        p.setAnio(Integer.parseInt(datos.get(0).split("-")[0]));
                    } catch (Exception e) {
                        p.setAnio(2022);
                    }

                    // Rating (Columna 5)
                    try {
                        p.setRatingPromedio(Double.parseDouble(datos.get(5)));
                    } catch (Exception e) {
                        p.setRatingPromedio(5.0);
                    }

                    // Genero (Columna 7) - Limpiamos comillas y espacios
                    String generoRaw = datos.get(7).replace("\"", "").trim().toUpperCase().split(",")[0].replace(" ",
                            "_");
                    try {
                        if (generoRaw.contains("ACTION"))
                            p.setGenero(GeneroPelicula.ACCION);
                        else if (generoRaw.contains("COMEDY"))
                            p.setGenero(GeneroPelicula.COMEDIA);
                        else if (generoRaw.contains("DRAMA"))
                            p.setGenero(GeneroPelicula.DRAMA);
                        else if (generoRaw.contains("SCIFI") || generoRaw.contains("SCIENCE"))
                            p.setGenero(GeneroPelicula.CIENCIA_FICCION);
                        else if (generoRaw.contains("HORROR"))
                            p.setGenero(GeneroPelicula.TERROR);
                        else
                            p.setGenero(GeneroPelicula.OTRO);
                    } catch (Exception ex) {
                        p.setGenero(GeneroPelicula.OTRO);
                    }

                    // Poster (Columna 8)
                    if (datos.size() > 8) {
                        p.setPosterURL(datos.get(8).replace("\"", "").trim());
                    } else {
                        p.setPosterURL("");
                    }

                    // Datos Dummy (no están en CSV o son complejos)
                    p.setDuracion(java.time.Duration.ofMinutes(120));
                    p.setDirector(new Staff("Director Desconocido", "Director"));

                    servicio.registrarPelicula(p);
                    cargadas++;
                } catch (Exception ex) {
                    saltadas++;
                    // Si falla una línea, seguimos con la siguiente
                }
            }

            // Commit de la transacción
            try {
                if (conn != null)
                    conn.commit();
            } catch (SQLException se) {
                System.err.println("Error haciendo commit: " + se.getMessage());
            }

            System.out.println(">>> LÍNEAS LEÍDAS: " + totales + ", CARGADAS: " + cargadas + ", SALTADAS: " + saltadas);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException se) {
                System.err.println("Error haciendo rollback: " + se.getMessage());
            }
        } finally {
            try {
                if (conn != null)
                    conn.setAutoCommit(previousAutoCommit);
            } catch (SQLException se) {
                System.err.println("No se pudo restaurar autoCommit: " + se.getMessage());
            }
        }
    }

    // Parser CSV sencillo que maneja comillas y comas dentro de campos
    private static List<String> parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        if (line == null)
            return result;
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                // Si es comilla y la siguiente también es comilla, es una comilla escapada
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    cur.append('"');
                    i++; // saltar la siguiente comilla
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        result.add(cur.toString());
        return result;
    }
}