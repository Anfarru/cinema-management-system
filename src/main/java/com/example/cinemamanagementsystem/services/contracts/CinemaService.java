package com.example.cinemamanagementsystem.services.contracts;

import com.example.cinemamanagementsystem.models.Cinema;
import com.example.cinemamanagementsystem.models.City;

import java.util.List;

public interface CinemaService {

    Cinema getById(Long id);

    Cinema getByAddress(String address);

    List<Cinema> getAll();

    List<Cinema> getAllByCity(City city);

    void create(Cinema cinema);
}
