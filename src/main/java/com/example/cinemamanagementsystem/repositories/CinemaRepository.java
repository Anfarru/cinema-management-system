package com.example.cinemamanagementsystem.repositories;

import com.example.cinemamanagementsystem.models.Cinema;
import com.example.cinemamanagementsystem.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {

    List<Cinema> findAllByCity(City city);

    Optional<Cinema> findByCinemaAddress(String address);
}
