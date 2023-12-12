package com.example.cinemamanagementsystem.services.contracts;

import com.example.cinemamanagementsystem.models.City;

public interface CityService {

    City getById(Long id);

    City getByName(String name);
}
