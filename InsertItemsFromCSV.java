package org.example.src.DataBaseHandlers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InsertItemsFromCSV {
    public static void main(String[] args) {
        String dbFilePath = "src/main/java/org/example/src/DataBaseHandlers/database/testing.db";
        String url = "jdbc:sqlite:" + dbFilePath;
        String csvFilePath = "src/main/java/org/example/src/DataBaseHandlers/database/new items.csv";

        String checkSQL = "SELECT * FROM ITEM WHERE id = ?";
        String updateSQL = "UPDATE ITEM SET name = ?, price = ?, quantity = quantity + ?, availability = ?, mfgDate = ?, expDate = ?, weight = ?, itemCapacity = ?, extraDetails = ?, purchasePrice = ?, supplierName = ?, extraClassType = ? WHERE id = ?";
        String insertSQL = "INSERT INTO ITEM (id, name, price, quantity, availability, mfgDate, expDate, weight, itemCapacity, extraDetails, purchasePrice, supplierName, extraClassType) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {

            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                // Skip header line
                if (lineNumber == 1) continue;

                String[] values = line.split(",");
                if (values.length != 13) {
                    System.out.println("Invalid data on line " + lineNumber + ": " + line);
                    continue;
                }

                int id = Integer.parseInt(values[0]);
                String name = values[1];
                int price = Integer.parseInt(values[2]);
                int quantity = Integer.parseInt(values[3]);
                boolean availability = values[4].equals("1");
                String mfgDate = values[5];
                String expDate = values[6];
                double weight = Double.parseDouble(values[7]);
                int itemCapacity = Integer.parseInt(values[8]);
                String extraDetails = values[9];
                int purchasePrice = Integer.parseInt(values[10]);
                String supplierName = values[11];
                String extraClassType = values[12];

                try (PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {
                    checkStmt.setInt(1, id);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next()) {
                            // Item exists, update it
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                                updateStmt.setString(1, name);
                                updateStmt.setInt(2, price);
                                updateStmt.setInt(3, quantity); // Add quantity
                                updateStmt.setBoolean(4, availability);
                                updateStmt.setString(5, mfgDate);
                                updateStmt.setString(6, expDate);
                                updateStmt.setDouble(7, weight);
                                updateStmt.setInt(8, itemCapacity);
                                updateStmt.setString(9, extraDetails);
                                updateStmt.setInt(10, purchasePrice);
                                updateStmt.setString(11, supplierName);
                                updateStmt.setString(12, extraClassType);
                                updateStmt.setInt(13, id);
                                updateStmt.executeUpdate();
                            }
                        } else {
                            // Item does not exist, insert it
                            try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                                insertStmt.setInt(1, id);
                                insertStmt.setString(2, name);
                                insertStmt.setInt(3, price);
                                insertStmt.setInt(4, quantity);
                                insertStmt.setBoolean(5, availability);
                                insertStmt.setString(6, mfgDate);
                                insertStmt.setString(7, expDate);
                                insertStmt.setDouble(8, weight);
                                insertStmt.setInt(9, itemCapacity);
                                insertStmt.setString(10, extraDetails);
                                insertStmt.setInt(11, purchasePrice);
                                insertStmt.setString(12, supplierName);
                                insertStmt.setString(13, extraClassType);
                                insertStmt.executeUpdate();
                            }
                        }
                    }
                }
            }
            System.out.println("Data processed successfully from CSV.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
