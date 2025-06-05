package org.example.src.DataBaseHandlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DeliveryBoyAvailabilityChecker {
    private static final String DB_FILE_PATH = "src/main/java/org/example/src/DataBaseHandlers/database/testing.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE_PATH;

    public static String assignDeliveryBoy(int busyDurationMinutes) {
        busyDurationMinutes = (busyDurationMinutes * 2 ) + 20;
        int assignedDeliveryBoyId = -1;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime earliestFreeTime = null;
        String resultMessage = "No delivery boy could be assigned.";

        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                if (conn != null) {
                    String selectSQL = "SELECT id, busytill FROM DeliveryBoy";
                    try (PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
                         ResultSet rs = selectStmt.executeQuery()) {

                        while (rs.next()) {
                            int deliveryBoyId = rs.getInt("id");
                            Timestamp busyTillTimestamp = rs.getTimestamp("busytill");
                            LocalDateTime busyTill = busyTillTimestamp != null ? busyTillTimestamp.toLocalDateTime() : null;

                            if (busyTill == null || now.isAfter(busyTill)) {
                                assignedDeliveryBoyId = deliveryBoyId;
                                earliestFreeTime = now.plusMinutes(busyDurationMinutes);
                                break;
                            }

                            if (earliestFreeTime == null || busyTill.isBefore(earliestFreeTime)) {
                                assignedDeliveryBoyId = deliveryBoyId;
                                earliestFreeTime = busyTill.plusMinutes(busyDurationMinutes);
                            }
                        }
                    }

                    if (assignedDeliveryBoyId != -1) {
                        String updateSQL = "UPDATE DeliveryBoy SET busytill = ? WHERE id = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                            updateStmt.setTimestamp(1, Timestamp.valueOf(earliestFreeTime));
                            updateStmt.setInt(2, assignedDeliveryBoyId);
                            updateStmt.executeUpdate();
                        }

                        if (now.isBefore(earliestFreeTime.minusMinutes(busyDurationMinutes))) {
                            resultMessage = "Delivery Boy ID: " + assignedDeliveryBoyId +
                                    " is busy and will take this order after: " + earliestFreeTime.minusMinutes(busyDurationMinutes);
                        } else {
                            resultMessage = "Delivery Boy ID: " + assignedDeliveryBoyId +
                                    " has been assigned to take this order immediately.";
                        }
                    }
                }
            }
        } catch (Exception e) {
            resultMessage = "Error occurred: " + e.getMessage();
        }

        return resultMessage;
    }

    public static String isDeliveryFree(int deliveryBoyId) {
        String resultMessage = "Error occurred while checking availability.";
        LocalDateTime now = LocalDateTime.now();

        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                if (conn != null) {
                    String selectSQL = "SELECT busytill FROM DeliveryBoy WHERE id = ?";
                    try (PreparedStatement selectStmt = conn.prepareStatement(selectSQL)) {
                        selectStmt.setInt(1, deliveryBoyId);
                        try (ResultSet rs = selectStmt.executeQuery()) {
                            if (rs.next()) {
                                Timestamp busyTillTimestamp = rs.getTimestamp("busytill");
                                LocalDateTime busyTill = busyTillTimestamp != null ? busyTillTimestamp.toLocalDateTime() : null;

                                if (busyTill == null || now.isAfter(busyTill)) {
                                    resultMessage = "Delivery Boy ID: " + deliveryBoyId + " is free.";
                                } else {
                                    resultMessage = "Delivery Boy ID: " + deliveryBoyId + " is busy until " + busyTill;
                                }
                            } else {
                                resultMessage = "No delivery boy found with ID: " + deliveryBoyId;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            resultMessage = "Error occurred: " + e.getMessage();
        }

        return resultMessage;
    }

    public static void main(String[] args) {
        int busyDurationMinutes = 30;
        String assignmentStatus = assignDeliveryBoy(busyDurationMinutes);
        System.out.println(assignmentStatus);

        int deliveryBoyId = 1;
        String availabilityStatus = isDeliveryFree(deliveryBoyId);
        System.out.println(availabilityStatus);
    }
}
