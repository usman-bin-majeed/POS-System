package org.example.src.MainClasses;


public abstract class Customer extends Person {
    protected int customerId;
    protected double discountRate;

    public Customer (String firstName, String lastName, String CNIC , int customerId , double discountRate) {
        super(firstName , lastName , CNIC  );
        this.customerId = customerId;
        this.discountRate = discountRate;
    }

    
}
