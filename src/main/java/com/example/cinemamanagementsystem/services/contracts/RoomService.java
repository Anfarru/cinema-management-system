package com.example.cinemamanagementsystem.services.contracts;

import com.example.cinemamanagementsystem.models.Cinema;
import com.example.cinemamanagementsystem.models.Room;

import java.util.List;

public interface RoomService {

    Room getById(Long id);

    List<Room> getAllByCinema(Cinema cinema);

    Room getByCinemaAndNumber(Cinema cinema, int number);
}
