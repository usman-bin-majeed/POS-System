//package org.example.src.DataBaseHandlers;
//
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
//public class GetBillDetails {
//    public static void main(String[] args) {
//        String dbFilePath = "src/main/java/org/example/src/DataBaseHandlers/database/testing.db";
//        String url = "jdbc:sqlite:" + dbFilePath;
//        int billId = 1; // Replace with the desired bill ID
//
//        try {
//            // Load the SQLite JDBC driver
//            Class.forName("org.sqlite.JDBC");
//
//            // Connect to the SQLite database
//            try (Connection conn = DriverManager.getConnection(url)) {
//                if (conn != null) {
//                    System.out.println("Connected to the database!");
//
//                    // SQL query to retrieve bill details
//                    String billSQL = """
//                        SELECT b.id, b.customerId, b.salesmanId, b.totalAmount, b.items, b.type,
//                               s.firstname || ' ' || s.lastname AS salesman_name
//                        FROM BILL b
//                        INNER JOIN SALESMAN s ON b.salesmanId = s.id
//                        WHERE b.id = ?;
//                        """;
//
//                    try (PreparedStatement billStmt = conn.prepareStatement(billSQL)) {
//                        billStmt.setInt(1, billId);
//
//                        try (ResultSet billRs = billStmt.executeQuery()) {
//                            if (billRs.next()) {
//                                System.out.println("Bill Details:");
//                                System.out.println("Bill ID: " + billRs.getInt("id"));
//                                System.out.println("Customer ID: " + billRs.getInt("customerId"));
//                                System.out.println("Salesman ID: " + billRs.getInt("salesmanId"));
//                                System.out.println("Salesman Name: " + billRs.getString("salesman_name"));
//                                System.out.println("Total Amount: " + billRs.getInt("totalAmount"));
//                                System.out.println("Type: " + billRs.getString("type"));
//                                System.out.println("Items:");
//
//                                // Manually parse the items field (assumed to be JSON-like)
//                                String itemsJson = billRs.getString("items");
//                                itemsJson = itemsJson.replace("[", "").replace("]", ""); // Remove square brackets
//                                String[] itemEntries = itemsJson.split("},"); // Split into individual item entries
//
//                                // SQL query to fetch item details including price
//                                String itemSQL = "SELECT name, price FROM ITEM WHERE id = ?;";
//                                try (PreparedStatement itemStmt = conn.prepareStatement(itemSQL)) {
//                                    for (String entry : itemEntries) {
//                                        entry = entry.replace("{", "").replace("}", ""); // Remove curly braces
//                                        String[] fields = entry.split(","); // Split into fields
//
//                                        int itemId = 0, quantity = 0;
//
//                                        for (String field : fields) {
//                                            String[] keyValue = field.split(":");
//                                            String key = keyValue[0].trim().replace("\"", "");
//                                            String value = keyValue[1].trim();
//
//                                            if (key.equals("itemId")) {
//                                                itemId = Integer.parseInt(value);
//                                            } else if (key.equals("quantity")) {
//                                                quantity = Integer.parseInt(value);
//                                            }
//                                        }
//
//                                        // Fetch item name and price
//                                        itemStmt.setInt(1, itemId);
//                                        try (ResultSet itemRs = itemStmt.executeQuery()) {
//                                            if (itemRs.next()) {
//                                                String itemName = itemRs.getString("name");
//                                                double itemPrice = itemRs.getDouble("price");
//                                                double totalPrice = itemPrice * quantity;  // Calculate total price for the item
//                                                System.out.println("  - " + itemName + " (Quantity: " + quantity + ", Price per Unit: " + itemPrice + ", Total: " + totalPrice + ")");
//                                            } else {
//                                                System.out.println("  - Item ID " + itemId + " not found in database.");
//                                            }
//                                        }
//                                    }
//                                }
//                            } else {
//                                System.out.println("No bill found with ID: " + billId);
//                            }
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public static String getBillDetailsAsString() {
//        // Return bill details as a formatted string for display
//        StringBuilder result = new StringBuilder();
//        result.append("Bill Details:\n");
//        // Add logic to fetch and format bill details
//        result.append("..."); // Replace with actual bill details
//        return result.toString();
//    }
//
//}


package org.example.src.DataBaseHandlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetBillDetails {

    public static String fetchBillDetails(int billId) {
        StringBuilder billDetails = new StringBuilder();
        String dbFilePath = "src/main/java/org/example/src/DataBaseHandlers/database/testing.db";
        String url = "jdbc:sqlite:" + dbFilePath;

        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = DriverManager.getConnection(url)) {
                if (conn != null) {
                    String billSQL = """
                        SELECT b.id, b.customerId, b.salesmanId, b.totalAmount, b.items, b.type, 
                               s.firstname || ' ' || s.lastname AS salesman_name
                        FROM BILL b
                        INNER JOIN SALESMAN s ON b.salesmanId = s.id
                        WHERE b.id = ?;
                        """;

                    try (PreparedStatement billStmt = conn.prepareStatement(billSQL)) {
                        billStmt.setInt(1, billId);

                        try (ResultSet billRs = billStmt.executeQuery()) {
                            if (billRs.next()) {
                                billDetails.append("Bill Details:\n");
                                billDetails.append("Bill ID: ").append(billRs.getInt("id")).append("\n");
                                billDetails.append("Customer ID: ").append(billRs.getInt("customerId")).append("\n");
                                billDetails.append("Salesman ID: ").append(billRs.getInt("salesmanId")).append("\n");
                                billDetails.append("Salesman Name: ").append(billRs.getString("salesman_name")).append("\n");
                                billDetails.append("Total Amount: ").append(billRs.getInt("totalAmount")).append("\n");
                                billDetails.append("Type: ").append(billRs.getString("type")).append("\n");
                                billDetails.append("Items:\n");

                                String itemsJson = billRs.getString("items");
                                itemsJson = itemsJson.replace("[", "").replace("]", "");
                                String[] itemEntries = itemsJson.split("},");

                                String itemSQL = "SELECT name, price FROM ITEM WHERE id = ?;";
                                try (PreparedStatement itemStmt = conn.prepareStatement(itemSQL)) {
                                    for (String entry : itemEntries) {
                                        entry = entry.replace("{", "").replace("}", "");
                                        String[] fields = entry.split(",");
                                        int itemId = 0, quantity = 0;

                                        for (String field : fields) {
                                            String[] keyValue = field.split(":");
                                            String key = keyValue[0].trim().replace("\"", "");
                                            String value = keyValue[1].trim();

                                            if (key.equals("itemId")) {
                                                itemId = Integer.parseInt(value);
                                            } else if (key.equals("quantity")) {
                                                quantity = Integer.parseInt(value);
                                            }
                                        }

                                        itemStmt.setInt(1, itemId);
                                        try (ResultSet itemRs = itemStmt.executeQuery()) {
                                            if (itemRs.next()) {
                                                String itemName = itemRs.getString("name");
                                                double itemPrice = itemRs.getDouble("price");
                                                double totalPrice = itemPrice * quantity;
                                                billDetails.append("  - ").append(itemName)
                                                        .append(" (Quantity: ").append(quantity)
                                                        .append(", Price per Unit: ").append(itemPrice)
                                                        .append(", Total: ").append(totalPrice).append(")\n");
                                            } else {
                                                billDetails.append("  - Item ID ").append(itemId).append(" not found in database.\n");
                                            }
                                        }
                                    }
                                }
                            } else {
                                billDetails.append("No bill found with ID: ").append(billId).append("\n");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            billDetails.append("Error fetching bill details: ").append(e.getMessage()).append("\n");
        }

        return billDetails.toString();
    }

    public static void main(String[] args) {
        int billId = 1; // Replace with the desired bill ID for testing
        String details = fetchBillDetails(billId);
        System.out.println(details);
    }
}
