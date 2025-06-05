package org.example.src.MainClasses;

import java.sql.*;
import java.util.ArrayList;

public class Bill {
    private int billId;
    private int customerId;
    private int salesmanId;
    private double totalAmount;
    private ItemQuantity[] items;

    private static final String DB_FILE_PATH = "src/main/java/org/example/src/DataBaseHandlers/database/testing.db";

    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE_PATH;


    public static int getHighestBillId() {
        String query = "SELECT MAX(id) AS highestId FROM Bill";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt("highestId");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving highest ID: " + e.getMessage());
        }
        return -1; // Return -1 if no records are found or an error occurs
    }
    public Bill(){}

    public Bill( int customerId, int salesmanId,  ItemQuantity[] items) {
        this.billId = getHighestBillId()+1;
        this.customerId = customerId;
        this.salesmanId = salesmanId;

        this.items = items;
    }

    public void generateBill(int customerId, int salesmanId, ArrayList<ItemQuantity> cart) {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. Cannot generate bill.");
            return;
        }

        double totalAmount = 0.0;
        ItemQuantity[] itemsArray = new ItemQuantity[cart.size()];

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                Class.forName("org.sqlite.JDBC");
                String insertBillSQL;
                if(salesmanId == 1){
                    insertBillSQL = """
                        INSERT INTO BILL (customerId, salesmanId, totalAmount, items, type)
                        VALUES (?, ?, ?, ?, "online");
                        """;
                }else{
                    insertBillSQL = """
                        INSERT INTO BILL (customerId, salesmanId, totalAmount, items, type)
                        VALUES (?, ?, ?, ?, "physical");
                        """;
                }
                // Prepare SQL statements
                String selectItemSQL = "SELECT name, price, quantity FROM ITEM WHERE id = ?";
                String updateItemSQL = "UPDATE ITEM SET quantity = ? WHERE id = ?";


                try (
                        PreparedStatement selectItemStmt = conn.prepareStatement(selectItemSQL);
                        PreparedStatement updateItemStmt = conn.prepareStatement(updateItemSQL);
                        PreparedStatement insertBillStmt = conn.prepareStatement(insertBillSQL)
                ) {
                    StringBuilder itemsJson = new StringBuilder("[");

                    for (int i = 0; i < cart.size(); i++) {
                        ItemQuantity cartItem = cart.get(i);
                        int itemId = cartItem.getItemId(); // Now it's an int
                        int requestedQuantity = cartItem.getQuantity();

                        // Retrieve item details
                        selectItemStmt.setInt(1, itemId);
                        try (ResultSet rs = selectItemStmt.executeQuery()) {
                            if (rs.next()) {
                                String itemName = rs.getString("name");
                                double itemPrice = rs.getDouble("price");
                                int currentStock = rs.getInt("quantity");

                                if (requestedQuantity > currentStock) {
                                    System.out.println("Insufficient stock for item: " + itemName);
                                    return;
                                }

                                // Calculate the total amount
                                double itemTotalPrice = itemPrice * requestedQuantity;
                                totalAmount += itemTotalPrice;

                                // Update item quantity in the database
                                updateItemStmt.setInt(1, currentStock - requestedQuantity);
                                updateItemStmt.setInt(2, itemId);
                                updateItemStmt.executeUpdate();

                                // Add item details to JSON string
                                itemsJson.append("{\"itemId\":").append(itemId)
                                        .append(",\"quantity\":").append(requestedQuantity)
                                        .append(",\"price\":").append(itemPrice)
                                        .append("},");

                                // Populate the items array
                                itemsArray[i] = new ItemQuantity(itemId, requestedQuantity); // Passing int itemId
                            } else {
                                System.out.println("Item ID " + itemId + " not found in the database.");
                                return;
                            }
                        }
                    }

                    // Finalize the items JSON
                    if (itemsJson.charAt(itemsJson.length() - 1) == ',') {
                        itemsJson.deleteCharAt(itemsJson.length() - 1);
                    }
                    itemsJson.append("]");

                    // Insert the bill into the database
                    insertBillStmt.setInt(1, customerId);
                    insertBillStmt.setInt(2, salesmanId);
                    insertBillStmt.setDouble(3, totalAmount);
                    insertBillStmt.setString(4, itemsJson.toString());

                    insertBillStmt.executeUpdate();
                    System.out.println("Bill generated successfully! Total amount: " + totalAmount);
                }

                // Update object fields
                this.customerId = customerId;
                this.salesmanId = salesmanId;
                this.totalAmount = totalAmount;
                this.items = itemsArray;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ItemQuantity {
        private final int itemId; // Changed to int
        private final int quantity;

        public ItemQuantity(int itemId, int quantity) {
            this.itemId = itemId; // No need to parse itemId to int
            this.quantity = quantity;
        }

        public int getItemId() {
            return itemId; // Returns itemId as an int
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
