package servicio;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

/**
 * Servicio para consultar la API de OMDb y obtener detalles de películas
 */
public class ConsultaPeliculasOMDb {

    // Reemplaza con tu API Key obtenida en https://www.omdbapi.com/apikey.aspx
    private static final String API_KEY = "74e5c254";

    /**
     * Consulta la API de OMDb por título exacto.
     * Retorna un JSONObject con los datos de la película si la encuentra.
     */
    public static JSONObject consultarPelicula(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return null;
        }

        try {
            if (API_KEY.equals("TU_API_KEY")) {
                System.err.println("⚠️  API_KEY no configurada en ConsultaPeliculasOMDb.java");
                return null;
            }

            String url = "https://www.omdbapi.com/?t=" + titulo.replace(" ", "+") + "&apikey=" + API_KEY;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());
            if (json.has("Response") && json.getString("Response").equals("True")) {
                return json;
            } else {
                System.out.println("❌ Película no encontrada: " + titulo);
                return null;
            }

        } catch (Exception e) {
            System.err.println("Error al consultar la API: " + e.getMessage());
            return null;
        }
    }

    /**
     * Extrae título de la respuesta OMDb
     */
    public static String obtenerTitulo(JSONObject json) {
        if (json != null && json.has("Title")) {
            return json.getString("Title");
        }
        return "N/A";
    }

    /**
     * Extrae año de la respuesta OMDb
     */
    public static String obtenerAnio(JSONObject json) {
        if (json != null && json.has("Year")) {
            return json.getString("Year");
        }
        return "N/A";
    }

    /**
     * Extrae sinopsis/plot de la respuesta OMDb
     */
    public static String obtenerSinopsis(JSONObject json) {
        if (json != null && json.has("Plot")) {
            return json.getString("Plot");
        }
        return "N/A";
    }

    /**
     * Extrae rating de la respuesta OMDb
     */
    public static String obtenerRating(JSONObject json) {
        if (json != null && json.has("imdbRating")) {
            return json.getString("imdbRating");
        }
        return "N/A";
    }
}
