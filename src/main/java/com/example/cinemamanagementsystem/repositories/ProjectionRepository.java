package com.example.cinemamanagementsystem.repositories;

import com.example.cinemamanagementsystem.models.Movie;
import com.example.cinemamanagementsystem.models.Projection;
import com.example.cinemamanagementsystem.models.ProjectionFilterOptions;
import com.example.cinemamanagementsystem.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectionRepository extends JpaRepository<Projection, Long> {

    Optional<Projection> findByRoomAndMovieAndStartingTime(Room room, Movie movie, LocalDateTime startingTime);

    List<Projection> findAllByRoom(Room room);

    default boolean hasOverlappingProjections(Room room, LocalDateTime startingTime, LocalDateTime endingTime){
        List<Projection> overlappedProjections = findOverlappingProjections(room, startingTime, endingTime);
        return !overlappedProjections.isEmpty();
    }

    @Query(
            "SELECT p from Projection p " +
                    "WHERE p.room = :room " +
                    " AND ((:start >= p.startingTime AND :start < p.endingTime) " +
                    "OR (:end > p.startingTime AND :end <= p.endingTime))"
    )
    List<Projection> findOverlappingProjections(@Param("room") Room room,
                                                @Param("start") LocalDateTime startingTime,
                                                @Param("end") LocalDateTime endingTime);

    @Query(
            "SELECT p from Projection p " +
                    "WHERE p.room = :#{#options.room} " +
                    "AND (:#{#options.intervalStart.isPresent()} = false OR p.startingTime >= :#{#options.intervalStart.orElse(null)}) " +
                    "AND (:#{#options.intervalEnd.isPresent()} = false OR p.endingTime <= :#{#options.intervalEnd.orElse(null)}) " +
                    "ORDER BY " +
                    "CASE " +
                    "   WHEN :#{#options.intervalStart.isPresent()} = true THEN p.startingTime " +
                    "   ELSE p.endingTime " +
                    "END ASC "
    )
    List<Projection> filterAndSort(@Param("options") ProjectionFilterOptions options);
}
