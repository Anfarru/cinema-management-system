package com.example.cinemamanagementsystem.helpers;

import com.example.cinemamanagementsystem.models.Movie;
import com.example.cinemamanagementsystem.models.Projection;
import com.example.cinemamanagementsystem.models.dtos.ProjectionDTO;
import com.example.cinemamanagementsystem.services.contracts.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProjectionMapper {

    private final MovieService movieService;

    @Autowired
    public ProjectionMapper(MovieService movieService) {
        this.movieService = movieService;
    }

    public Projection fromDTO(ProjectionDTO projectionDTO){
        Projection projection = new Projection();
        Movie movie = movieService.getByTitle(projectionDTO.getTitle());
        LocalDateTime startingTime = projectionDTO.getStartingTime();
        projection.setMovie(movie);
        projection.setStartingTime(startingTime);
        return projection;
    }
}
