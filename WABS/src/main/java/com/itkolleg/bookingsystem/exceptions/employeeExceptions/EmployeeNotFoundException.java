package com.itkolleg.bookingsystem.exceptions.employeeExceptions;

public class EmployeeNotFoundException extends Exception {

    public EmployeeNotFoundException(String s) {
        super("Mitarbeiter nicht gefunden!");
    }
}
