package com.example.cinemamanagementsystem.services.contracts;

import com.example.cinemamanagementsystem.models.Projection;
import com.example.cinemamanagementsystem.models.Reservation;
import com.example.cinemamanagementsystem.models.dtos.SeatDTO;

import java.util.List;

public interface ReservationService {

    Reservation getById(Long id);

    List<Reservation> getAll();

    List<Reservation> getAllByProjection(Projection projection);

    void create(Reservation reservation, Projection projection);
}
