package com.itkolleg.bookingsystem.exceptions.employeeExceptions;

import com.itkolleg.bookingsystem.exceptions.FormValidationExceptionDTO;

public class EmployeeValidationException extends Exception {

    private FormValidationExceptionDTO errors;

    public EmployeeValidationException(String message) {
        super("Eingegebene Mitarbeiterdaten sind inkorrekt: " + message);
    }

    public EmployeeValidationException(FormValidationExceptionDTO errors) {
        this.errors = errors;
    }

    public FormValidationExceptionDTO getErrorMap() {
        return errors;
    }

}
