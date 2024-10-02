package com.itkolleg.bookingsystem.exceptions.roomExceptions;

public class RoomNotFoundException extends Exception {

    public RoomNotFoundException(String s){
        super("Raum wurde nicht gefunden!");
    }
}
