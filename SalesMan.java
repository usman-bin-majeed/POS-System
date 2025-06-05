//package org.example.src.MainClasses;
//
//
//import java.util.ArrayList;
//
//public class SalesMan extends Employee {
//    protected String user_name;
//    protected String password;
//
//    public SalesMan (String firstName, String lastName, String CNIC , int employeeId , double salary , String user_name ,String password) {
//        super(firstName , lastName , CNIC , employeeId , salary);
//        this.user_name = user_name;
//        this.password = password;
//
//    }
//
//    public String getUser_name () {
//        return user_name;
//    }
//    public String getPassword () {
//        return password;
//    }
//
//    public void createBill(int customerId, ArrayList<Bill.ItemQuantity> cart) {
//        if (cart == null || cart.isEmpty()) {
//            System.out.println("Cart is empty. Cannot create a bill.");
//            return;
//        }
//
//        Bill bill = new Bill(0, customerId, this.employeeId, 0.0, null);
//        bill.generateBill(customerId, this.employeeId, cart);
//    }
//    public int generatebill( ArrayList<ArrayList<Integer>> itemList ) {
//        return 0;
//    }
//
//
//}

package org.example.src.MainClasses;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class SalesMan extends Employee {
    private static final String DB_FILE_PATH = "src/main/java/org/example/src/DataBaseHandlers/database/testing.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE_PATH;

    private String user_name;
    private String password;
    private ArrayList<Bill.ItemQuantity> cart;

    public SalesMan(String firstName, String lastName, String CNIC, int salary, String user_name, String password) {
        super(firstName, lastName, CNIC, salary);
        this.user_name = user_name;
        this.password = password;
        this.employeeId = getHighestSalesmanId() + 1;


    }
    public SalesMan(){}
    public SalesMan(ArrayList<Bill.ItemQuantity> cart , int id){
        this.employeeId = id;
        this.cart = cart;
    }

    public int getHighestSalesmanId() {
        String query = "SELECT MAX(id) AS highestId FROM SALESMAN";
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
    public String getUser_name() {
        return user_name;
    }

    public String getPassword() {
        return password;
    }








        public void addToCart(int itemId, int quantity) {
            this.cart.add(new Bill.ItemQuantity(itemId, quantity));
            System.out.println("Item added to cart: Item ID " + itemId + ", Quantity " + quantity);
        }

        public void clearCart() {
            this.cart.clear();
        }

        public void createBill(int customerId) {
            if (cart.isEmpty()) {
                System.out.println("Cart is empty. Cannot create a bill.");
                return;
            }
            Bill bill = new Bill( customerId, this.employeeId, null);
            bill.generateBill(customerId, this.employeeId, cart);
            clearCart(); // Clear the cart after generating the bill
        }







    public void addCustomer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Enter the last name: ");
        String lastName = scanner.nextLine();
        System.out.println("Enter your CNIC: ");
        String CNIC = scanner.nextLine();
        System.out.println("Enter the username: ");
        String userName = scanner.nextLine();
        System.out.println("Enter the login password: ");
        String password = scanner.nextLine();

        String sql = "INSERT INTO CUSTOMER(firstName, lastName, CNIC, userName, password) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DB_URL);
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, firstName);
            pstm.setString(2, lastName);
            pstm.setString(3, CNIC);
            pstm.setString(4, userName);
            pstm.setString(5, password);

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Customer added successfully.");
            } else {
                System.out.println("Customer couldn't be added.");
            }
        } catch (SQLException e) {
            System.err.println("Error adding customer: " + e.getMessage());
        }
    }
}
