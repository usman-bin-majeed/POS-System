package org.example.src.MainClasses;// import Data;

public abstract class Person {
    protected String firstName , lastName, CNIC;
    // protected Date DOB;
    
    public Person(String firstName, String lastName, String CNIC) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.CNIC = CNIC;
        // this.BOB = DOB;
    }
    public Person(){}

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getCNIC() {
        return CNIC;
    }
}
