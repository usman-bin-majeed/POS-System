package org.example.src.ApiClasses;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CordinatesConverter {

    // Method to get coordinates from an address
    public static double[] getCoordinates(String location) {
        try {
            // Encode the location for the URL
            String query = java.net.URLEncoder.encode(location, "UTF-8");
            // URL to the Nominatim API to search for the address
            String urlString = String.format(
                    "https://nominatim.openstreetmap.org/search?q=%s&format=json&limit=1", query
            );
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            // Check the response code from the API
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                // Parse the JSON response
                JSONArray jsonResponse = new JSONArray(response.toString());

                // If a result is found, extract the latitude and longitude
                if (jsonResponse.length() > 0) {
                    JSONObject locationData = jsonResponse.getJSONObject(0);
                    double latitude = locationData.getDouble("lat");
                    double longitude = locationData.getDouble("lon");

                    // Return the coordinates as an array [latitude, longitude]
                    return new double[]{latitude, longitude};
                }
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
        return null;  // Return null if coordinates are not found
    }
}
