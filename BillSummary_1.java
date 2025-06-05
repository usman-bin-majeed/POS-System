package org.example.src.DataBaseHandlers;

import java.sql.*;

public class BillSummary_1 {

    private static final String DB_PATH = "src/main/java/org/example/src/DataBaseHandlers/database/testing.db";

    public static double[] calculateProfitByDate(int year, int month) {
        String dateFilter;
        if (month == -1) {
            dateFilter = "strftime('%Y', creationDate) = ?";
        } else {
            dateFilter = "strftime('%Y', creationDate) = ? AND strftime('%m', creationDate) = ?";
        }

        String billQuery = "SELECT id, items, totalAmount FROM BILL WHERE " + dateFilter;

        double totalCost = 0.0;
        double totalBillAmount = 0.0;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
             PreparedStatement billStmt = conn.prepareStatement(billQuery)) {

            billStmt.setString(1, String.valueOf(year));
            if (month != -1) {
                billStmt.setString(2, String.format("%02d", month));
            }

            try (ResultSet billRs = billStmt.executeQuery()) {
                while (billRs.next()) {
                    String itemsJson = billRs.getString("items");
                    double billAmount = billRs.getDouble("totalAmount");

                    totalCost += calculateItemCost(itemsJson, conn);
                    totalBillAmount += billAmount;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        double profit = totalBillAmount - totalCost;
        return new double[]{totalCost, totalBillAmount, profit};
    }

    private static double calculateItemCost(String itemsJson, Connection conn) {
        double totalCost = 0.0;

        try {
            String[] items = itemsJson.replace("[", "").replace("]", "").split("},\\{");
            String itemQuery = "SELECT purchasePrice FROM ITEM WHERE id = ?";

            try (PreparedStatement itemStmt = conn.prepareStatement(itemQuery)) {
                for (String itemStr : items) {
                    String cleanedItemStr = itemStr.replace("{", "").replace("}", "").replace("\"", "");
                    String[] keyValuePairs = cleanedItemStr.split(",");

                    int itemId = 0;
                    int quantity = 0;

                    for (String pair : keyValuePairs) {
                        String[] keyValue = pair.split(":");
                        String key = keyValue[0].trim();
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
                            double purchasePrice = itemRs.getDouble("purchasePrice");
                            totalCost += purchasePrice * quantity;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalCost;
    }

    public static void main(String[] args) {
        int year = 2024;
        int month = -12; // Set -1 for year-only filter

        double[] results = calculateProfitByDate(year, month);
        double totalCost = results[0];
        double totalBillAmount = results[1];
        double profit = results[2];

        if (month == -1) {
            System.out.println("Summary for Year " + year + ":");
        } else {
            System.out.println("Summary for " + year + "-" + String.format("%02d", month) + ":");
        }

        System.out.println("Total Cost  :" + totalCost);
        System.out.println("Total Revenue : " + totalBillAmount);
        System.out.println("Total Profit: " + profit);
    }
}
