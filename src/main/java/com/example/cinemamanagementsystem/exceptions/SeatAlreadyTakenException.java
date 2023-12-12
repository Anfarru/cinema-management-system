package com.example.cinemamanagementsystem.exceptions;

public class SeatAlreadyTakenException extends RuntimeException{
    public SeatAlreadyTakenException(String message) {
        super(message);
    }
}
