package com.example.cinemamanagementsystem.services;

import com.example.cinemamanagementsystem.exceptions.EntityNotFoundException;
import com.example.cinemamanagementsystem.models.Projection;
import com.example.cinemamanagementsystem.models.Reservation;
import com.example.cinemamanagementsystem.models.Room;
import com.example.cinemamanagementsystem.models.Seat;
import com.example.cinemamanagementsystem.repositories.SeatRepository;
import com.example.cinemamanagementsystem.services.contracts.ReservationService;
import com.example.cinemamanagementsystem.services.contracts.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.cinemamanagementsystem.helpers.Constants.SEAT_DOES_NOT_EXIST_ERROR;

@Service
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    private final ReservationService reservationService;

    @Autowired
    public SeatServiceImpl(SeatRepository seatRepository, ReservationService reservationService) {
        this.seatRepository = seatRepository;
        this.reservationService = reservationService;
    }

    @Override
    public Seat getById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seat", id));
    }

    @Override
    public Seat getByRoomAndSeatRowAndSeatColumn(Room room, int seatRow, int seatColumn) {
        return seatRepository.findByRoomAndSeatRowAndSeatColumn(room, seatRow, seatColumn)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(SEAT_DOES_NOT_EXIST_ERROR, seatRow, seatColumn)));
    }

    @Override
    public List<Seat> getAllByRoom(Room room) {
        return seatRepository.findAllByRoom(room);
    }

    @Override
    public List<Seat> getAllAvailable(Projection projection) {
        List<Seat> allSeats = getAllByRoom(projection.getRoom());
        return allSeats
                .stream()
                .filter(s -> !isPartOfReservation(s, projection))
                .collect(Collectors.toList());
    }

    @Override
    public List<Seat> getAllTaken(Projection projection) {
        List<Seat> allSeats = getAllByRoom(projection.getRoom());
        return allSeats
                .stream()
                .filter(s -> isPartOfReservation(s, projection))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isPartOfReservation(Seat seat, Projection projection){
        List<Reservation> reservations = reservationService.getAllByProjection(projection);
        if(reservations.isEmpty()){
            return false;
        }
        for(Reservation reservation : reservations){
            if(reservation.getSeats().contains(seat)){
                return true;
            }
        }
        return false;
    }
}
