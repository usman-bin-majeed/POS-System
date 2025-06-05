package org.example.src.MainClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
public class Manager extends Employee {
    protected String user_name;
    protected String password;
        
    public Manager (String firstName, String lastName, String CNIC , int employeeId , double salary, String user_name ,String password) {
        super(firstName , lastName , CNIC  , salary);
        this.user_name = user_name;
        this.password = password;
        
    }
    String dbFilePath = "src/main/java/org/example/src/DataBaseHandlers/database/testing.db";
    String url = "jdbc:sqlite:" + dbFilePath;
    public void addSalesman(SalesMan salesman){

        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "INSERT INTO SALESMAN (id, firstName, lastName, CNIC , salary , username, password) VALUES (?,?,?,?,?,?,?)";
            try {
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setInt(1, salesman.getEmployeeId());
                statement.setString(2 , salesman.getFirstName());
                statement.setString(3 , salesman.getLastName());
                statement.setString(4, salesman.getCNIC());
                statement.setDouble(5 , salesman.getSalary());
                statement.setString(6 , salesman.getUser_name() );
                statement.setString(7 , salesman.getPassword());

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Salesman added successfully");
                }else{
                    System.out.println("Salesman cant be added ");
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    public void addDeliveryBoy(DeliveryBoy dilveryBoy){
//        String dbFilePath = "E:\\THIRD_SEMESTER_BDS-3\\OOP\\Final Project\\src\\main\\java\\org\\example\\src\\DataBaseHandlers\\database\\testing.db";
//        String url = "jdbc:sqlite:" + dbFilePath;
//        try (Connection conn = DriverManager.getConnection(url)) {
//            String sql = "INSERT INTO DELIVERYBOY (id, firstName, lastName, CNIC , salary ,Busytill) VALUES (?,?,?,?,?,DateTime(now)";
//            try {
//                PreparedStatement statement = conn.prepareStatement(sql);
//                statement.setInt(1, dilveryBoy.getEmployeeId());
//                statement.setString(2 , dilveryBoy.getFirstName());
//                statement.setString(3 , dilveryBoy.getLastName());
//                statement.setString(4, dilveryBoy.getCNIC());
//                statement.setDouble(5 , dilveryBoy.getSalary());
//
//
//                int rowsAffected = statement.executeUpdate();
//                if (rowsAffected > 0) {
//                    System.out.println("dilveryBoy added successfully");
//                }else{
//                    System.out.println("dilveryBoy cant be added ");
//                }
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }


    public void addDeliveryBoy(DeliveryBoy deliveryBoy) {


        String sql = "INSERT INTO DELIVERYBOY (id, firstName, lastName, CNIC, salary, Busytill) VALUES (?, ?, ?, ?, ?, DateTime('now'))";

        try (Connection conn = DriverManager.getConnection(url)) {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, deliveryBoy.getEmployeeId());
                statement.setString(2, deliveryBoy.getFirstName());
                statement.setString(3, deliveryBoy.getLastName());
                statement.setString(4, deliveryBoy.getCNIC());
                statement.setDouble(5, deliveryBoy.getSalary());

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("DeliveryBoy added successfully.");
                } else {
                    System.out.println("Failed to add DeliveryBoy.");
                }
            } catch (Exception e) {
                System.out.println("Error while executing SQL query: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error while connecting to the database: " + e.getMessage());
        }
    }


}
