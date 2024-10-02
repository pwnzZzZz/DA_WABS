package com.itkolleg.bookingsystem.exceptions;


public class CustomIllegalArgumentException extends IllegalArgumentException {
    private final String origin;

    public CustomIllegalArgumentException(String s, String origin) {
        super(s);
        this.origin = origin;
    }

    public String getOrigin() {
        return origin;
    }
}

