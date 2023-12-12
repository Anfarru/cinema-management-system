package com.example.cinemamanagementsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private long movieId;

    @Column(name = "movie_title")
    private String title;

    @Column(name = "movie_duration")
    private String duration;

    public Movie(String title, String duration) {
        this.title = title;
        this.duration = duration;
    }
}
