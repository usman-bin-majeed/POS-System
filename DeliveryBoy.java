package org.example.src.MainClasses;


import java.sql.*;
import java.time.LocalDateTime;
public class DeliveryBoy extends Employee {
    protected LocalDateTime time;
    private static final String DB_FILE_PATH = "src/main/java/org/example/src/DataBaseHandlers/database/testing.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE_PATH;
    public DeliveryBoy(String firstName, String lastName, String CNIC,  int salary) {
        super(firstName, lastName, CNIC, salary);
        this.time = LocalDateTime.now(); // Corrected: LocalDateTime.now()
        this.employeeId = getHighestDeliveryBoy() + 1;

    }

    public int getHighestDeliveryBoy() {
        String query = "SELECT MAX(id) AS highestId FROM DeliveryBoy";
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
    
public LocalDateTime getTime() {
    return time;
}
    
}
