package com.example.cinemamanagementsystem.helpers;

import com.example.cinemamanagementsystem.exceptions.DuplicateEntityException;
import com.example.cinemamanagementsystem.exceptions.EmptyReservationException;
import com.example.cinemamanagementsystem.exceptions.SeatAlreadyTakenException;
import com.example.cinemamanagementsystem.models.Projection;
import com.example.cinemamanagementsystem.models.Reservation;
import com.example.cinemamanagementsystem.models.Room;
import com.example.cinemamanagementsystem.models.Seat;
import com.example.cinemamanagementsystem.models.dtos.SeatDTO;
import com.example.cinemamanagementsystem.services.contracts.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SeatMapper {

    private final SeatService seatService;

    @Autowired
    public SeatMapper(SeatService seatService) {
        this.seatService = seatService;
    }

    public Seat fromDTO(SeatDTO seatDTO, Room room){
        Seat seat = new Seat();
        seat.setSeatRow(seatDTO.getSeatRow());
        seat.setSeatColumn(seatDTO.getSeatColumn());
        seat.setRoom(room);
        return seat;
    }

    public Seat findFromDTO(SeatDTO seatDTO, Room room){
        return seatService.getByRoomAndSeatRowAndSeatColumn(
                room,
                seatDTO.getSeatRow(),
                seatDTO.getSeatColumn());
    }

    public void checkIfSeatsReserved(List<SeatDTO> seatDTOs,
                                     Projection projection,
                                     Reservation reservation){
        Room room = projection.getRoom();
        for(SeatDTO seatDTO : seatDTOs){
            Seat seat = findFromDTO(seatDTO, room);
            if(!seatService.isPartOfReservation(seat, projection)){
                if(reservation.getSeats().contains(seat)){
                    throw new DuplicateEntityException("You cannot reserve the same seat twice.");
                }
                reservation.getSeats().add(seat);
            }
            else{
                throw new SeatAlreadyTakenException(
                        String.format(
                                "Seat on row %d column %d is already taken.",
                                seat.getSeatRow(),
                                seat.getSeatColumn()));
            }
        }
    }
}
