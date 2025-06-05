package org.example.src.ApiClasses;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OSMGuiApp {
    private static StringBuilder routeDetails = new StringBuilder(); // StringBuilder to store route details

    // Method to get coordinates from an address
    public static void getRouteDetails(double startLat, double startLon, double endLat, double endLon) {
        try {
            // OSRM API URL for routing with steps
            String urlString = String.format(
                    "https://router.project-osrm.org/route/v1/driving/%f,%f;%f,%f?overview=false&steps=true",
                    startLon, startLat, endLon, startLat
            );
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                // Debug: Print raw API response
                System.out.println("API Response: " + response.toString());

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray routes = jsonResponse.getJSONArray("routes");

                if (routes.length() > 0) {
                    JSONObject route = routes.getJSONObject(0);
                    double distance = route.getDouble("distance") / 1000; // Convert to kilometers
                    double duration = route.getDouble("duration") / 60;  // Convert to minutes

                    routeDetails.setLength(0);  // Clear previous route details

                    if (distance > 50) {
                        routeDetails.append("Cannot be Dispatched: Distance exceeds 50 km.\n");
                    } else {
                        routeDetails.append(String.format("Distance: %.2f km, Duration: %.2f minutes%n", distance, duration));
                        routeDetails.append("Route Instructions:\n");

                        JSONArray legs = route.getJSONArray("legs");
                        int stepCount = 1;

                        for (int i = 0; i < legs.length(); i++) {
                            JSONObject leg = legs.getJSONObject(i);
                            JSONArray steps = leg.getJSONArray("steps");

                            for (int j = 0; j < steps.length(); j++) {
                                JSONObject step = steps.getJSONObject(j);
                                JSONObject maneuver = step.getJSONObject("maneuver");

                                // Extract step details
                                String instruction = maneuver.optString("modifier", "Proceed");
                                String roadName = step.optString("name", "Unnamed Road");
                                double stepDistance = step.getDouble("distance") / 1000; // Convert to kilometers

                                // Debug: Print each step details
                                System.out.println("Step " + stepCount + ": " + instruction + " onto " + roadName);

                                routeDetails.append(String.format("%d. %s onto %s (%.2f km)%n", stepCount++, instruction, roadName, stepDistance));
                            }
                        }
                    }
                } else {
                    routeDetails.append("No route found.");
                }
            } else {
                routeDetails.append("Error: Received response code ").append(responseCode);
                System.out.println("Error: API response code " + responseCode);
            }
        } catch (Exception e) {
            routeDetails.append("Error occurred: ").append(e.getMessage());
            e.printStackTrace();  // Debug: Print stack trace for exceptions
        }
    }



    // Method to get route details as a string
    public static String getRouteDetailsString() {
        return routeDetails.toString();
    }
}
