package com.example.cinemamanagementsystem.helpers;

public class Constants {
    public static final String OVERLAPPING_PROJECTIONS_ERROR =
            "Room %d has a projection scheduled within that time frame.";
    public static final String PAST_TIME_ERROR = "Starting time must be in the future.";
    public static final String CINEMA_EXISTS_ERROR =
            "There is an existing cinema on the given address";
    public static final String NO_PROJECTIONS_IN_INTERVAL_ERROR = "No projections in this interval of time.";
    public static final String NO_PROJECTION_MATCH_ERROR = "No projection matches given criteria.";
    public static final String SEAT_DOES_NOT_EXIST_ERROR = "No seat on row %s and column %s.";
    public static final String NO_SEATS_RESERVED_ERROR = "You have not reserved any seats.";
}
