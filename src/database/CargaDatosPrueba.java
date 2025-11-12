package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import dao.PeliculaDAO;
import dao.PeliculaDAOimple; // Asegúrate de importar tu implementación
import enums.GeneroPelicula;
import modelo.Pelicula;

public class CargaDatosPrueba {

    /**
     * Método principal para ejecutar la carga de datos.
     * Puedes ejecutar esta clase directamente (Run As > Java Application)
     * para poblar la base de datos.
     */
    public static void main(String[] args) {
        System.out.println("Iniciando carga de películas desde el CSV...");
        cargarPeliculasDesdeCSV();
    }

    /**
     * Lee el archivo "movies_database.csv" y carga las películas en la BD.
     */
    public static void cargarPeliculasDesdeCSV() {
        // Asume que el CSV está en la raíz del proyecto
        String rutaCSV = "movies_database.csv"; 
        
        // Usamos la interfaz DAO
        PeliculaDAO peliDAO = new PeliculaDAOimple(); 
        String linea = "";
        int contadorPeliculas = 0;
        int lineasOmitidas = 0;

        // Usamos try-with-resources para que el BufferedReader se cierre solo
        try (BufferedReader br = new BufferedReader(new FileReader(rutaCSV))) {
            
            // 1. Omitir la línea de encabezado (los títulos de las columnas)
            br.readLine(); 

            // 2. Leer línea por línea
            while ((linea = br.readLine()) != null) {
                
                // Regex para separar por comas, pero ignorar comas dentro de comillas
                // Esto es clave porque 'Overview' y 'Genre' contienen comas.
                String[] campos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                // Verificación básica de que la línea tiene los campos esperados
                if (campos.length < 9) {
                    System.err.println("[OMITIDA] Línea mal formada: " + linea);
                    lineasOmitidas++;
                    continue; // Salta a la siguiente iteración
                }

                try {
                    // 3. Extraer y "limpiar" los datos de las columnas
                    
                    // Col 0: Release_Date (ej: "2021-12-15")
                    String fechaCruda = campos[0];
                    
                    // Col 1: Title
                    String titulo = campos[1].trim();
                    
                    // Col 2: Overview (viene con comillas, hay que quitarlas)
                    String sinopsis = campos[2].trim().replaceAll("^\"|\"$", "");
                    
                    // Col 5: Vote_Average (rating)
                    double rating = Double.parseDouble(campos[5].trim());
                    
                    // Col 7: Genre (viene con comillas, ej: "Action, Adventure, Science Fiction")
                    String generoString = campos[7].trim().replaceAll("^\"|\"$", "");
                    
                    // Col 8: Poster_Url
                    String posterUrl = campos[8].trim();

                    // 4. Procesar los datos extraídos
                    
                    // Procesar el Año
                    int anio = 0;
                    if (fechaCruda != null && !fechaCruda.isEmpty() && fechaCruda.contains("-")) {
                        anio = Integer.parseInt(fechaCruda.split("-")[0]);
                    }

                    // Mapear el String de Género al Enum GeneroPelicula
                    GeneroPelicula genero = mapearGenero(generoString);

                    // 5. Crear el objeto Pelicula
                    Pelicula peli = new Pelicula();
                    peli.setTitulo(titulo);
                    peli.setSinopsis(sinopsis);
                    peli.setGenero(genero); // Asigna el Enum
                    peli.setAnio(anio);
                    peli.setPuntaje(rating);
                    peli.setPoster(posterUrl); // Guarda la URL como String

                    // 6. Insertar en la Base de Datos usando el DAO
                    peliDAO.guardar(peli);
                    contadorPeliculas++;

                } catch (NumberFormatException e) {
                    System.err.println("[OMITIDA] Error de formato de número en línea: " + linea);
                    lineasOmitidas++;
                } catch (Exception e) {
                     System.err.println("[OMITIDA] Error inesperado procesando línea: " + linea + " | " + e.getMessage());
                     lineasOmitidas++;
                }
            } // Fin del while

        } catch (IOException e) {
            System.err.println("Error fatal al leer el archivo CSV: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("-------------------------------------------------");
        System.out.println("Carga de CSV completada.");
        System.out.println("Películas insertadas exitosamente: " + contadorPeliculas);
        System.out.println("Líneas omitidas por errores: " + lineasOmitidas);
        System.out.println("-------------------------------------------------");
    }

    /**
     * Método ayudante para convertir el String de género del CSV (ej: "Action, Adventure")
     * a un valor de tu Enum GeneroPelicula (ej: ACCION).
     * * Esta es una lógica de "mejor esfuerzo" (best-effort) basada en tu Enum.
     * * @param csvGenero El string de géneros del CSV.
     * @return El GeneroPelicula correspondiente.
     */
    private static GeneroPelicula mapearGenero(String csvGenero) {
        if (csvGenero == null || csvGenero.isEmpty()) {
            return GeneroPelicula.DRAMA; // Un default si viene vacío
        }
        
        String generosUpper = csvGenero.toUpperCase();

        // Buscamos una coincidencia por prioridad
        if (generosUpper.contains("ACTION")) {
            return GeneroPelicula.ACCION;
        }
        if (generosUpper.contains("SCIENCE FICTION")) {
            return GeneroPelicula.CIENCIA_FICCION;
        }
        if (generosUpper.contains("COMEDY")) {
            return GeneroPelicula.COMEDIA;
        }
        if (generosUpper.contains("DRAMA")) {
            return GeneroPelicula.DRAMA;
        }

        // Si el CSV trae géneros como "Thriller", "Mystery", "History", etc.
        // que NO están en tu Enum, asignamos DRAMA como categoría por defecto.
        return GeneroPelicula.DRAMA;
    }
}