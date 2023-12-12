package com.example.cinemamanagementsystem.services;

import com.example.cinemamanagementsystem.exceptions.EntityNotFoundException;
import com.example.cinemamanagementsystem.models.City;
import com.example.cinemamanagementsystem.repositories.CityRepository;
import com.example.cinemamanagementsystem.services.contracts.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public City getById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("City", id));
    }

    @Override
    public City getByName(String name) {
        return cityRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("City", name));
    }
}
