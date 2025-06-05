package org.example.src.DataBaseHandlers;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;



    public class RetrieveItems {
        public static String fetchItemsFromDatabase() {
            StringBuilder itemList = new StringBuilder("Item List:\n\n");
            String dbFilePath = "src/main/java/org/example/src/DataBaseHandlers/database/testing.db";
            String url = "jdbc:sqlite:" + dbFilePath;

            try {
                Class.forName("org.sqlite.JDBC");
                try (Connection conn = DriverManager.getConnection(url)) {
                    if (conn != null) {
                        String selectSQL = "SELECT * FROM ITEM";
                        try (Statement stmt = conn.createStatement();
                             ResultSet rs = stmt.executeQuery(selectSQL)) {

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                            // Header row formatting for better column alignment
                            itemList.append(String.format("%-5s %-30s %-10s %-10s %-15s %-15s %-10s %-10s %-20s %-15s %-20s %-20s%n",
                                    "ID", "Name", "Price", "Quantity", "Availability", "MfgDate", "ExpDate", "Capacity", "Details", "PurchasePrice", "Supplier", "ExtraClassType"));

                            while (rs.next()) {
                                // Use trim() to remove any extra spaces from the data
                                int id = rs.getInt("id");
                                String name = rs.getString("name").trim();
                                int price = rs.getInt("price");
                                int quantity = rs.getInt("quantity");
                                boolean availability = rs.getBoolean("availability");

                                String mfgDateStr = rs.getString("mfgDate");
                                String expDateStr = rs.getString("expDate");
//                                Date mfgDate = mfgDateStr != null ? dateFormat.parse(mfgDateStr.trim()) : null;
//                                Date expDate = expDateStr != null ? dateFormat.parse(expDateStr.trim()) : null;

                                int itemCapacity = rs.getInt("itemCapacity");
                                String extraDetails = rs.getString("extraDetails").trim();
                                int purchasePrice = rs.getInt("purchasePrice");
                                String supplierName = rs.getString("supplierName").trim();
                                String extraClassType = rs.getString("extraClassType").trim();

                                // Format dates for display
//                                String formattedMfgDate = mfgDate != null ? dateFormat.format(mfgDate) : "N/A";
//                                String formattedExpDate = expDate != null ? dateFormat.format(expDate) : "N/A";

                                // Append item details in a well-aligned format
                                itemList.append(String.format("%-5d %-30s %-10d %-10d %-15s %-15s %-10s %-10d %-20s %-15d %-20s %-20s%n",
                                        id, name, price, quantity, availability ? "true" : "false", mfgDateStr,
                                        expDateStr, itemCapacity, extraDetails, purchasePrice, supplierName, extraClassType));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                itemList.append("Error fetching items: ").append(e.getMessage());
            }
            return itemList.toString();
        }


    }




