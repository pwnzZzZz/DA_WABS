package com.itkolleg.bookingsystem.exceptions.ressourceExceptions;

public class RessourceAlreadyExistsException extends Throwable {
    public RessourceAlreadyExistsException(String s) {
        System.out.println("Die von Ihnen angegebene ressource existiert bereits!");
    }
}
