package com.example.cinemamanagementsystem.controllers;

import com.example.cinemamanagementsystem.models.Movie;
import com.example.cinemamanagementsystem.services.contracts.MovieService;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieRESTController {
    private final MovieService movieService;

    public MovieRESTController(MovieService movieService) {
        this.movieService = movieService;
    }

    @ApiOperation(value = "Returns all movies")
    @GetMapping
    @Cacheable(value = "moviesCache")
    public List<Movie> getAll(){
        return movieService.getAll();
    }
}
