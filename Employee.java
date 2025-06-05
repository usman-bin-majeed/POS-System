package org.example.src.MainClasses;

public abstract class Employee extends Person {
    
    protected int employeeId;
    protected double salary;
    // protected Date DOB;
    
    public Employee (String firstName, String lastName, String CNIC  , double salary) {
        super(firstName , lastName , CNIC );
//        this.employeeId = employeeId;
        this.salary = salary;
        // this.BOB = DOB;
    }
    public Employee(){}

    public int getEmployeeId() {
        return employeeId;
    }
    public double getSalary() {
        return salary;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }


    
}
