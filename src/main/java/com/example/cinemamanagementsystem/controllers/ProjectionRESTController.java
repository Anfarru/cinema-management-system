package com.example.cinemamanagementsystem.controllers;

import com.example.cinemamanagementsystem.exceptions.DuplicateEntityException;
import com.example.cinemamanagementsystem.exceptions.EmptyReservationException;
import com.example.cinemamanagementsystem.exceptions.EntityNotFoundException;
import com.example.cinemamanagementsystem.exceptions.SeatAlreadyTakenException;
import com.example.cinemamanagementsystem.helpers.SeatMapper;
import com.example.cinemamanagementsystem.models.*;
import com.example.cinemamanagementsystem.models.dtos.SeatDTO;
import com.example.cinemamanagementsystem.services.contracts.ProjectionService;
import com.example.cinemamanagementsystem.services.contracts.ReservationService;
import com.example.cinemamanagementsystem.services.contracts.SeatService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/projections")
public class ProjectionRESTController {

    private final ProjectionService projectionService;
    private final SeatService seatService;
    private final SeatMapper seatMapper;
    private final ReservationService reservationService;

    public ProjectionRESTController(ProjectionService projectionService,
                                    SeatService seatService,
                                    SeatMapper seatMapper,
                                    ReservationService reservationService) {
        this.projectionService = projectionService;
        this.seatService = seatService;
        this.seatMapper = seatMapper;
        this.reservationService = reservationService;
    }

    @ApiOperation(value = "Returns all unreserved seats for a projection")
    @GetMapping("/{projectionId}/seats/available")
    public List<Seat> getAvailableSeats(@PathVariable Long projectionId){
        try{
            Projection projection = projectionService.getById(projectionId);
            return seatService.getAllAvailable(projection);
        } catch(EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @ApiOperation(value = "Returns all reserved seats for a projection")
    @GetMapping("/{projectionId}/seats/taken")
    public List<Seat> getTakenSeats(@PathVariable Long projectionId){
        try{
            Projection projection = projectionService.getById(projectionId);
            return seatService.getAllTaken(projection);
        } catch(EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @ApiOperation(value = "Creates a reservation for one or more seats for a projection and generates a 6-symbol unique code")
    @PostMapping("/{projectionId}/seats/reserve")
    public Mono<Reservation> reserveSeat(@PathVariable Long projectionId,
                                         @Valid @RequestBody List<SeatDTO> seatDTOs){
        return Mono.fromSupplier(() -> {
            try{
                Projection projection = projectionService.getById(projectionId);
                Reservation reservation = new Reservation();
                seatMapper.checkIfSeatsReserved(seatDTOs, projection, reservation);
                reservationService.create(reservation, projection);
                return reservation;
            } catch(EntityNotFoundException e){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            } catch(EmptyReservationException | SeatAlreadyTakenException | DuplicateEntityException e){
                throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
            }
        });
    }
}
