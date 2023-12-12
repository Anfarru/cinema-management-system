package com.example.cinemamanagementsystem.services;

import com.example.cinemamanagementsystem.exceptions.EntityNotFoundException;
import com.example.cinemamanagementsystem.models.Cinema;
import com.example.cinemamanagementsystem.models.Room;
import com.example.cinemamanagementsystem.repositories.RoomRepository;
import com.example.cinemamanagementsystem.services.contracts.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room getById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room", id));
    }

    @Override
    public List<Room> getAllByCinema(Cinema cinema) {
        return roomRepository.findAllByCinema(cinema);
    }

    @Override
    public Room getByCinemaAndNumber(Cinema cinema, int number) {
        return roomRepository.findRoomByCinemaAndNumber(cinema, number)
                .orElseThrow(() -> new EntityNotFoundException("Room", number));
    }
}
