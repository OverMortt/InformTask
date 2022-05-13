package org.mortt.weather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherJSONController {
    @FXML
    private TextArea output;

    @FXML
    private TextField city;

    @FXML
    protected void onRequestButtonClick() {
        // If city is provided -> find it
        String cityName = city.getText();
        if (cityName == null || cityName.isBlank()) {
            cityName = "Ivanovo";
        }

        // Receiving app id from environment variables
        String appId = System.getenv("APP_ID");

        if (appId == null) {
            System.err.println("App id should be provided via environment variable APP_ID");
            return;
        }

        // Creating URI
        URI uri;
        try {
            uri = new URI("http://api.openweathermap.org/data/2.5/weather?q=" + cityName.trim() + "&appid=" + appId + "&lang=ru");
        } catch (URISyntaxException e) {
            System.err.println("Exception during URI conversion: " + e.getMessage());
            return;
        }

        // Creating HTTP request with new Java 11's API
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        // Sending request with HttpClient
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.err.println("Exception while sending HTTP request: " + e.getMessage());
            return;
        }

        // Processing response body
        if (response.body() == null) {
            System.err.println("Response body is null");
            return;
        }

        JsonParser parser = new JsonParser();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement element = parser.parse(response.body());

        String prettyString = gson.toJson(element);

        output.setText(prettyString);
    }
}