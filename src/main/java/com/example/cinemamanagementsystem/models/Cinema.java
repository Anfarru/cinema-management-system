package com.example.cinemamanagementsystem.models;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "cinemas")
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinema_id")
    private long cinemaId;

    @Column(name = "cinema_name")
    private String cinemaName;

    @Column(name = "cinema_address")
    private String cinemaAddress;

    @ManyToOne
    @JoinColumn(name = "cinema_city_id")
    private City city;

    @OneToMany(
            mappedBy = "cinema",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Room> rooms = new HashSet<>();

    public Cinema(String cinemaName, String cinemaAddress, City city) {
        this.cinemaName = cinemaName;
        this.cinemaAddress = cinemaAddress;
        this.city = city;
    }

    public Cinema(Long cinemaId, String cinemaName, String cinemaAddress, City city) {
        this.cinemaId = cinemaId;
        this.cinemaName = cinemaName;
        this.cinemaAddress = cinemaAddress;
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cinema cinema = (Cinema) o;
        return cinemaId == cinema.cinemaId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cinemaId);
    }
}
