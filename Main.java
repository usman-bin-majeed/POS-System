package org.example.src.MainClasses;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
//        // Example Salesman
//        SalesMan salesman = new SalesMan("SAAD", "String lastName", "String CNIC" , 3 , 10 ,  "user_name" , "password");
//        // Example Customer ID
//        int customerId = 1001;
//
//        // Example Cart with items
//        ArrayList<Bill.ItemQuantity> cart = new ArrayList<>();
//        cart.add(new Bill.ItemQuantity("1", 2)); // Item ID: 1, Quantity: 2, Price per unit: 50.0
//        cart.add(new Bill.ItemQuantity("2", 1)); // Item ID: 2, Quantity: 1, Price per unit: 30.0
//        cart.add(new Bill.ItemQuantity("3", 3)); // Item ID: 3, Quantity: 3, Price per unit: 20.0
//
//        // Generate the bill
//        salesman.createBill(customerId, cart);


        Manager M1 = new Manager( "ASHAH" , "KHAN" , "vhj" , 1 , 10000 , "hhh ", "bgvsahs");
//        SalesMan S1 = new SalesMan("ASHAH" , "KHAN" , "vhj" , 10 , 10000 , "hhh ", "bgvsahs");
//        M1.addSalesman(S1);

        DeliveryBoy D1 = new DeliveryBoy("DeliveryBoy" , "KHAN" , "vhj"  , 10000 );
        M1.addDeliveryBoy(D1);
    }
}
