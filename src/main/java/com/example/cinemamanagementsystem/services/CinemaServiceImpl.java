package com.example.cinemamanagementsystem.services;

import com.example.cinemamanagementsystem.exceptions.DuplicateEntityException;
import com.example.cinemamanagementsystem.exceptions.EntityNotFoundException;
import com.example.cinemamanagementsystem.models.Cinema;
import com.example.cinemamanagementsystem.models.City;
import com.example.cinemamanagementsystem.repositories.CinemaRepository;
import com.example.cinemamanagementsystem.services.contracts.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.cinemamanagementsystem.helpers.Constants.CINEMA_EXISTS_ERROR;

@Service
public class CinemaServiceImpl implements CinemaService {

    private final CinemaRepository cinemaRepository;

    @Autowired
    public CinemaServiceImpl(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public Cinema getById(Long id) {
        return cinemaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cinema", id));
    }

    @Override
    public Cinema getByAddress(String address) {
        return cinemaRepository.findByCinemaAddress(address)
                .orElseThrow(() -> new EntityNotFoundException("Cinema", address));
    }

    @Override
    public List<Cinema> getAll() {
        return cinemaRepository.findAll();
    }

    @Override
    public List<Cinema> getAllByCity(City city){
        return cinemaRepository.findAllByCity(city);
    }

    @Override
    @CacheEvict(value = "cinemasCache", allEntries = true)
    public void create(Cinema cinema) {
        Cinema newCinema = new Cinema();
        try{
            newCinema = getByAddress(cinema.getCinemaAddress());
            throw new DuplicateEntityException(CINEMA_EXISTS_ERROR);
        } catch(EntityNotFoundException e){
            newCinema.setCinemaAddress(cinema.getCinemaAddress());
            newCinema.setCinemaName(cinema.getCinemaName());
            newCinema.setCity(cinema.getCity());
            cinemaRepository.save(newCinema);
        }
    }
}
