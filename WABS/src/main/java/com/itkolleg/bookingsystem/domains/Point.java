package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.Embeddable;

@Embeddable
public class Point {
    private int x;
    private int y;

    // default constructor
    public Point() {
    }

    // constructor
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // getters and setters...

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}