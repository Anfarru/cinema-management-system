package com.example.cinemamanagementsystem.repositories;

import com.example.cinemamanagementsystem.models.Room;
import com.example.cinemamanagementsystem.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    Optional<Seat> findByRoomAndSeatRowAndSeatColumn(Room room, int seatRow, int seatColumn);

    List<Seat> findAllByRoom(Room room);
}
