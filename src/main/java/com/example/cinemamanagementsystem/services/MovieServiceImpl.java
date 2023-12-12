package com.example.cinemamanagementsystem.services;

import com.example.cinemamanagementsystem.exceptions.EntityNotFoundException;
import com.example.cinemamanagementsystem.models.Movie;
import com.example.cinemamanagementsystem.repositories.MovieRepository;
import com.example.cinemamanagementsystem.services.contracts.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie getById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movie", id));
    }

    @Override
    public Movie getByTitle(String title) {
        return movieRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Movie", title));
    }

    @Override
    public List<Movie> getAll() {
        return movieRepository.findAll();
    }
}
