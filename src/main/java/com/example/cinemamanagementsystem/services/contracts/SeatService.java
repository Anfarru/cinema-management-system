package com.example.cinemamanagementsystem.services.contracts;

import com.example.cinemamanagementsystem.models.Projection;
import com.example.cinemamanagementsystem.models.Room;
import com.example.cinemamanagementsystem.models.Seat;

import java.util.List;

public interface SeatService {

    Seat getById(Long id);

    Seat getByRoomAndSeatRowAndSeatColumn(Room room, int seatRow, int seatColumn);

    List<Seat> getAllByRoom(Room room);

    List<Seat> getAllAvailable(Projection projection);

    List<Seat> getAllTaken(Projection projection);

    boolean isPartOfReservation(Seat seat, Projection projection);
}
