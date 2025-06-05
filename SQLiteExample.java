package org.example.src.DataBaseHandlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SQLiteExample {
    public static void main(String[] args) {
        String dbFilePath = "database.db";
        String url = "jdbc:sqlite:" + dbFilePath;

        try {
            // Load the SQLite JDBC driver (ensure it's included in the classpath)
            Class.forName("org.sqlite.JDBC");

            // Establish the connection to the SQLite database
            try (Connection conn = DriverManager.getConnection(url)) {
                if (conn != null) {
                    System.out.println("Connected to the database!");

                    // Get the current timestamp
                    String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    // Correct SQL query
                    String selectSQL = "SELECT id FROM DELIVERYBOY WHERE busyTill < '" + currentTime + "'";
                    
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(selectSQL)) {

                        // Process the result set
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            System.out.println("ID: " + id);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
