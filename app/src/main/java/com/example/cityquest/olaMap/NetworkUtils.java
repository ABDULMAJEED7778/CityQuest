package com.example.cityquest.olaMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// NetworkUtils.java
public class NetworkUtils {

    public static String getCitySuggestions(String input, String apiKey) throws IOException {
        // Construct your URL with the Ola Maps Places API endpoint
        String urlString = "https://api.olamaps.io/places/v1/autocomplete?input=" + input + "&api_key=" + apiKey;


        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        reader.close();
        connection.disconnect();

        return result.toString();
    }
}
