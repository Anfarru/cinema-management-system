package com.example.cinemamanagementsystem.exceptions;

public class EmptyReservationException extends RuntimeException{
    public EmptyReservationException(String message) {
        super(message);
    }
}
