package com.example.cinemamanagementsystem.services.contracts;

import com.example.cinemamanagementsystem.models.Movie;

import java.util.List;

public interface MovieService {
    Movie getById(Long id);

    Movie getByTitle(String title);

    List<Movie> getAll();
}
