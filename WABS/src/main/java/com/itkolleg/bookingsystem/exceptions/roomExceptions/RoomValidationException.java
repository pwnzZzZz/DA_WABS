package com.itkolleg.bookingsystem.exceptions.roomExceptions;

import com.itkolleg.bookingsystem.exceptions.FormValidationExceptionDTO;

public class RoomValidationException extends Exception {

    private FormValidationExceptionDTO errors;

    public RoomValidationException(String message) {
        super("Eingegebene Raumdaten sind inkorrekt: " + message);
    }

    public RoomValidationException(FormValidationExceptionDTO errors) {
        this.errors = errors;
    }

    public FormValidationExceptionDTO getErrorMap() {
        return errors;
    }

}
