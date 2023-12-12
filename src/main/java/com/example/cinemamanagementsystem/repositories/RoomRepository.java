package com.example.cinemamanagementsystem.repositories;

import com.example.cinemamanagementsystem.models.Cinema;
import com.example.cinemamanagementsystem.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    List<Room> findAllByCinema(Cinema cinema);

    Optional<Room> findRoomByCinemaAndNumber(Cinema cinema, int number);
}
