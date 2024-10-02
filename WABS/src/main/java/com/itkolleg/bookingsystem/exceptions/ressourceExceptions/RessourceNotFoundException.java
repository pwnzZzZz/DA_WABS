package com.itkolleg.bookingsystem.exceptions.ressourceExceptions;

public class RessourceNotFoundException extends Exception {
    public RessourceNotFoundException() {
        super("ressource not found!");
    }
}
