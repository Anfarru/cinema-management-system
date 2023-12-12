package com.example.cinemamanagementsystem.services;

import com.example.cinemamanagementsystem.exceptions.EmptyReservationException;
import com.example.cinemamanagementsystem.exceptions.EntityNotFoundException;
import com.example.cinemamanagementsystem.helpers.ReservationCodeGenerator;
import com.example.cinemamanagementsystem.models.Projection;
import com.example.cinemamanagementsystem.models.Reservation;
import com.example.cinemamanagementsystem.repositories.ReservationRepository;
import com.example.cinemamanagementsystem.services.contracts.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.cinemamanagementsystem.helpers.Constants.NO_SEATS_RESERVED_ERROR;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation getById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation", id));
    }

    @Override
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> getAllByProjection(Projection projection) {
        return reservationRepository.findAllByProjection(projection);
    }

    @Override
    public void create(Reservation reservation, Projection projection) {
        if(reservation.getSeats().isEmpty()){
            throw new EmptyReservationException(NO_SEATS_RESERVED_ERROR);
        }
        reservation.setProjection(projection);
        reservation.setReservationCode(ReservationCodeGenerator.generateCode());
        reservationRepository.save(reservation);
    }
}
