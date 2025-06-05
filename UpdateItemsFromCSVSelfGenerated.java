
package org.example.src.DataBaseHandlers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UpdateItemsFromCSVSelfGenerated {
    public static void main(String[] args) {
        String dbFilePath = "src/main/java/org/example/src/DataBaseHandlers/database/testing.db";
        String url = "jdbc:sqlite:" + dbFilePath;
        String csvFilePath = "src/main/java/org/example/src/DataBaseHandlers/database/products_to_fill_capacity.csv";

        // SQL query for selecting the item to retrieve existing quantity
        String selectSQL = "SELECT * FROM ITEM WHERE id = ?";

        // SQL query for updating the item quantity
        String updateSQL = "UPDATE ITEM SET quantity = ?, availability = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {

            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Prepare the select statement to check current data
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
                 PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {

                String line;
                int lineNumber = 0;

                while ((line = br.readLine()) != null) {
                    lineNumber++;
                    // Skip the header line
                    if (lineNumber == 1) continue;

                    String[] values = line.split(",");
                    if (values.length != 5) {
                        System.out.println("Invalid data on line " + lineNumber + ": " + line);
                        continue;
                    }

                    // Extract data from the CSV file
                    int id = Integer.parseInt(values[0]);
                    String name = values[1];
                    int quantity = Integer.parseInt(values[2]);
                    int itemCapacity = Integer.parseInt(values[3]);
                    int quantityToAdd = Integer.parseInt(values[4]);

                    // Check if the item exists and retrieve the current quantity and availability
                    selectStmt.setInt(1, id);
                    try (ResultSet rs = selectStmt.executeQuery()) {
                        if (rs.next()) {
                            int currentQuantity = rs.getInt("quantity");
                            boolean currentAvailability = rs.getBoolean("availability");

                            // Calculate the new quantity
                            int newQuantity = currentQuantity + quantityToAdd;

                            // Update the item with the new quantity (and keep other information the same)
                            updateStmt.setInt(1, newQuantity);
                            updateStmt.setBoolean(2, currentAvailability); // keep the current availability status
                            updateStmt.setInt(3, id);

                            // Execute the update
                            updateStmt.executeUpdate();
                            System.out.println("Updated item with ID " + id + ": " + name + " | New Quantity: " + newQuantity);
                        } else {
                            System.out.println("Item with ID " + id + " not found in the database.");
                        }
                    }
                }
                System.out.println("Data updated successfully from CSV.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
