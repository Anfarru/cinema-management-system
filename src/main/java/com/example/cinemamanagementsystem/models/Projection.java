package com.example.cinemamanagementsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projections")
public class Projection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projection_id")
    private long projectionId;

    @OneToOne
    @JoinColumn(name = "projection_movie_id")
    private Movie movie;

    @Column(name = "projection_starting_time")
    private LocalDateTime startingTime;

    @Column(name = "projection_ending_time")
    private LocalDateTime endingTime;

    @ManyToOne
    @JoinColumn(name = "projection_room_id")
    private Room room;

    public Projection(Movie movie, LocalDateTime startingTime, LocalDateTime endingTime, Room room) {
        this.movie = movie;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.room = room;
    }
}
