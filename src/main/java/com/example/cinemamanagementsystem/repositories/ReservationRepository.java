package com.example.cinemamanagementsystem.repositories;

import com.example.cinemamanagementsystem.models.Projection;
import com.example.cinemamanagementsystem.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByProjection(Projection projection);
}
