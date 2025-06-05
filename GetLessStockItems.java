package org.example.src.DataBaseHandlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class GetLessStockItems {


    public static String getLessStockItems() {
        String DB_FILE_PATH = "src/main/java/org/example/src/DataBaseHandlers/database/testing.db";
        String DB_URL = "jdbc:sqlite:" + DB_FILE_PATH;
        StringBuilder lowStockItems = new StringBuilder("Low Stock Items:\n\n");

        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Establish the connection
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                if (conn != null) {
                    String query = "SELECT id, name, quantity, itemCapacity FROM ITEM";

                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(query)) {

                        // Header row for formatted output
                        lowStockItems.append(String.format("%-5s %-30s %-10s %-10s%n", "ID", "Name", "Quantity", "Capacity"));

                        while (rs.next()) {
                            int id = rs.getInt("id");
                            String name = rs.getString("name").trim();
                            int quantity = rs.getInt("quantity");
                            int capacity = rs.getInt("itemCapacity");

                            if (quantity < 0.9 * capacity) {
                                lowStockItems.append(String.format("%-5d %-30s %-10d %-10d%n", id, name, quantity, capacity));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            lowStockItems.append("Error fetching low stock items: ").append(e.getMessage());
        }

        return lowStockItems.toString();
    }

    public static void main(String[] args) {
        System.out.println(getLessStockItems());
    }
}
