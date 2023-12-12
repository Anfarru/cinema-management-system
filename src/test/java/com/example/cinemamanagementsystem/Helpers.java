package com.example.cinemamanagementsystem;

import com.example.cinemamanagementsystem.models.*;

import java.time.LocalDateTime;

public class Helpers {

    public static City createTestCity(){
        return new City("Gabrovo");
    }

    public static Movie createTestMovie(){
        return new Movie( "Old Dads", "104 minutes");
    }

    public static Cinema createTestCinema(){
        City city = createTestCity();
        return new Cinema("Kino Arena",
                "ul. Akademik Andrei Saharov 2",
                city);
    }

    public static Room createTestRoom(){
        Cinema cinema = createTestCinema();
        return new Room(1, 120, cinema);
    }

    public static Seat createTestSeat(){
        Room room = createTestRoom();
        return new Seat(6, 5, room);
    }

    public static Projection createTestProjection(){
        Movie movie = createTestMovie();
        LocalDateTime startingTime = LocalDateTime.of(2023, 11, 20, 19, 30, 00);
        LocalDateTime endingTime = LocalDateTime.of(2023, 11, 20, 21, 11, 00);
        Room room = createTestRoom();

        return new Projection(movie, startingTime, endingTime, room);
    }

    public static Projection createOverlappingProjection(){
        Movie movie = createTestMovie();
        LocalDateTime startingTime = LocalDateTime.of(2023, 11, 20, 19, 45, 00);
        LocalDateTime endingTime = LocalDateTime.of(2023, 11, 20, 21, 8, 00);
        Room room = createTestRoom();

        return new Projection(movie, startingTime, endingTime, room);
    }

    public static Projection createPastProjection(){
        Movie movie = createTestMovie();
        LocalDateTime startingTime = LocalDateTime.of(1999, 11, 20, 19, 45, 00);
        LocalDateTime endingTime = LocalDateTime.of(1999, 11, 20, 21, 8, 00);
        Room room = createTestRoom();

        return new Projection(movie, startingTime, endingTime, room);
    }

    public static Reservation createTestReservation(){
        Reservation reservation = new Reservation();
        Projection projection = createTestProjection();
        reservation.setProjection(projection);
        reservation.setReservationCode("LLodHP");

        return reservation;
    }
}
