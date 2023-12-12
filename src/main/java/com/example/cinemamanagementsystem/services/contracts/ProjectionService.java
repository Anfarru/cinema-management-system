package com.example.cinemamanagementsystem.services.contracts;

import com.example.cinemamanagementsystem.models.Movie;
import com.example.cinemamanagementsystem.models.Projection;
import com.example.cinemamanagementsystem.models.ProjectionFilterOptions;
import com.example.cinemamanagementsystem.models.Room;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectionService {

    Projection getById(Long id);

    Projection getByRoomAndMovieAndStartingTime(Room room, Movie movie, LocalDateTime startingTime);

    List<Projection> getAllByRoom(Room room);

    List<Projection> filterAndSort(ProjectionFilterOptions projectionFilterOptions);

    void create(Projection projection);
}
