package org.example.src.DataBaseHandlers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class GenerateCSVForFillCapacity {
    public static void main(String[] args) {
        String dbFilePath = "src/main/java/org/example/src/DataBaseHandlers/database/testing.db";
        String url = "jdbc:sqlite:" + dbFilePath;
        String csvFilePath = "src/main/java/org/example/src/DataBaseHandlers/database/products_to_fill_capacity.csv";

        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Establish the connection
            try (Connection conn = DriverManager.getConnection(url)) {
                if (conn != null) {
                    System.out.println("Connected to the database!");

                    // SQL query to retrieve all items
                    String selectSQL = "SELECT id, name, quantity, itemCapacity FROM ITEM";

                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(selectSQL);
                         BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {

                        // Write the header to the CSV file
                        writer.write("ID,Name,Quantity,ItemCapacity,QuantityToAdd\n");

                        // Iterate through the result set
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            String name = rs.getString("name");
                            int quantity = rs.getInt("quantity");
                            int itemCapacity = rs.getInt("itemCapacity");

                            // Calculate if quantity is less than 30% of capacity
                            if (quantity < 0.9 * itemCapacity) {
                                int quantityToAdd = itemCapacity - quantity;
                                writer.write(id + "," + name + "," + quantity + "," + itemCapacity + "," + quantityToAdd + "\n");
                                System.out.println("Added to CSV: " + name + " (ID: " + id + ")");
                            }
                        }
                    }
                    System.out.println("CSV file generated successfully at: " + csvFilePath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
