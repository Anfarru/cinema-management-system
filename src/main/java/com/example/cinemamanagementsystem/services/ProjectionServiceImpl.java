package com.example.cinemamanagementsystem.services;

import com.example.cinemamanagementsystem.exceptions.EntityNotFoundException;
import com.example.cinemamanagementsystem.exceptions.InvalidTimeframeException;
import com.example.cinemamanagementsystem.models.Movie;
import com.example.cinemamanagementsystem.models.Projection;
import com.example.cinemamanagementsystem.models.ProjectionFilterOptions;
import com.example.cinemamanagementsystem.models.Room;
import com.example.cinemamanagementsystem.repositories.ProjectionRepository;
import com.example.cinemamanagementsystem.services.contracts.ProjectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.cinemamanagementsystem.helpers.Constants.*;

@Service
public class ProjectionServiceImpl implements ProjectionService {

    private final ProjectionRepository projectionRepository;

    @Autowired
    public ProjectionServiceImpl(ProjectionRepository projectionRepository) {
        this.projectionRepository = projectionRepository;
    }

    @Override
    public Projection getById(Long id) {
        return projectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projection", id));
    }

    @Override
    public Projection getByRoomAndMovieAndStartingTime(Room room, Movie movie, LocalDateTime startingTime) {
        return projectionRepository.findByRoomAndMovieAndStartingTime(room, movie, startingTime)
                .orElseThrow(() -> new EntityNotFoundException(NO_PROJECTION_MATCH_ERROR));
    }

    @Override
    public List<Projection> getAllByRoom(Room room) {
        return projectionRepository.findAllByRoom(room);
    }

    @Override
    public List<Projection> filterAndSort(ProjectionFilterOptions projectionFilterOptions) {
        List<Projection> projections = projectionRepository.filterAndSort(projectionFilterOptions);
        if(projections.isEmpty()){
            throw new EntityNotFoundException(NO_PROJECTIONS_IN_INTERVAL_ERROR);
        }
        return projections;
    }

    @Override
    public void create(Projection projection) {
        Movie movie = projection.getMovie();
        String movieDuration = movie.getDuration();
        LocalDateTime endingTime = calculateEndingTime(projection.getStartingTime(), movieDuration);
        projection.setEndingTime(endingTime);

        if(projectionRepository.hasOverlappingProjections(projection.getRoom(),
                projection.getStartingTime(),
                projection.getEndingTime())){
            throw new InvalidTimeframeException(
                    String.format(OVERLAPPING_PROJECTIONS_ERROR, projection.getRoom().getNumber()));
        }

        if(projection.getStartingTime().isBefore(LocalDateTime.now()) ||
                projection.getStartingTime().isEqual(LocalDateTime.now())){
            throw new InvalidTimeframeException(PAST_TIME_ERROR);
        }

        projectionRepository.save(projection);
    }

    private LocalDateTime calculateEndingTime(LocalDateTime startingTime, String duration){
        String[] durationArr = duration.split(" ");
        int durationDigits = Integer.parseInt(durationArr[0]);
        return startingTime.plusMinutes(durationDigits);
    }
}
