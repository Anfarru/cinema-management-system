package com.example.cinemamanagementsystem.controllers;

import com.example.cinemamanagementsystem.exceptions.DuplicateEntityException;
import com.example.cinemamanagementsystem.exceptions.EntityNotFoundException;
import com.example.cinemamanagementsystem.exceptions.InvalidTimeframeException;
import com.example.cinemamanagementsystem.helpers.CinemaMapper;
import com.example.cinemamanagementsystem.helpers.ProjectionMapper;
import com.example.cinemamanagementsystem.models.*;
import com.example.cinemamanagementsystem.models.dtos.CinemaDTO;
import com.example.cinemamanagementsystem.models.dtos.NewCinemaDTO;
import com.example.cinemamanagementsystem.models.dtos.ProjectionDTO;
import com.example.cinemamanagementsystem.services.contracts.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cinemas")
public class CinemaRESTController {

    private final CinemaService cinemaService;
    private final CityService cityService;
    private final RoomService roomService;
    private final CinemaMapper cinemaMapper;
    private final ProjectionMapper projectionMapper;
    private final ProjectionService projectionService;

    public CinemaRESTController(CinemaService cinemaService,
                                CityService cityService,
                                RoomService roomService,
                                CinemaMapper cinemaMapper,
                                ProjectionMapper projectionMapper,
                                ProjectionService projectionService) {
        this.cinemaService = cinemaService;
        this.cityService = cityService;
        this.roomService = roomService;
        this.cinemaMapper = cinemaMapper;
        this.projectionMapper = projectionMapper;
        this.projectionService = projectionService;
    }

    @ApiOperation(value = "Returns a cinema")
    @GetMapping("/{cinemaId}")
    public Cinema getCinemaById(@PathVariable Long cinemaId){
        try{
            return cinemaService.getById(cinemaId);
        } catch(EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @ApiOperation(value = "Returns all cinemas and their respective rooms")
    @GetMapping
    @Cacheable(value = "cinemasCache")
    public List<CinemaDTO> getAll(){
            List<Cinema> cinemas = cinemaService.getAll();
            return cinemaMapper.getCinemasAndRooms(cinemas);
    }

    @ApiOperation(value = "Returns all cinemas in a city")
    @GetMapping("/city/{cityName}")
    public List<Cinema> getAllByCity(@PathVariable String cityName){
        try {
            City city = cityService.getByName(cityName);
            return cinemaService.getAllByCity(city);
        } catch(EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @ApiOperation(value = "Returns all rooms of a cinema")
    @GetMapping("/{cinemaId}/rooms")
    @Cacheable(value = "roomsCache")
    public List<Room> getAllRoomsByCinema(@PathVariable Long cinemaId){
        try{
            Cinema cinema = cinemaService.getById(cinemaId);
            return roomService.getAllByCinema(cinema);
        } catch(EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @ApiOperation("Returns all projections for a room")
    @GetMapping("/{cinemaId}/rooms/{roomNumber}/schedule")
    public List<Projection> getRoomSchedule(@PathVariable Long cinemaId,
                                            @PathVariable int roomNumber){
        try{
            Cinema cinema = cinemaService.getById(cinemaId);
            Room room = roomService.getByCinemaAndNumber(cinema, roomNumber);
            return projectionService.getAllByRoom(room);
        } catch(EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @ApiOperation(value = "Returns all projections that both start and end within an interval")
    @GetMapping("/{cinemaId}/projections/rooms/{roomNumber}")
    @Cacheable(value = "projectionsCache")
    public List<Projection> getProjectionsInPeriod(
            @PathVariable Long cinemaId,
            @PathVariable int roomNumber,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime intervalStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime intervalEnd){
        try{
            Cinema cinema = cinemaService.getById(cinemaId);
            Room room = roomService.getByCinemaAndNumber(cinema, roomNumber);
            ProjectionFilterOptions projectionFilterOptions = new ProjectionFilterOptions(
                    room,
                    intervalStart,
                    intervalEnd
            );
            return projectionService.filterAndSort(projectionFilterOptions);
        } catch(EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @ApiOperation(value = "Schedules a projection for a room")
    @PostMapping("/{cinemaId}/rooms/{roomNumber}/projection")
    public Projection scheduleProjection(@PathVariable Long cinemaId,
                                         @PathVariable int roomNumber,
                                         @Valid @RequestBody ProjectionDTO projectionDTO){
        try{
            Cinema cinema = cinemaService.getById(cinemaId);
            Room room = roomService.getByCinemaAndNumber(cinema, roomNumber);
            Projection projection = projectionMapper.fromDTO(projectionDTO);
            projection.setRoom(room);
            projectionService.create(projection);
            return projection;
        } catch(EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch(InvalidTimeframeException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

    @ApiOperation(value = "Creates a cinema")
    @PostMapping()
    public Cinema createCinema(@Valid @RequestBody NewCinemaDTO newCinemaDTO){
        try{
            Cinema cinema = cinemaMapper.fromDTO(newCinemaDTO);
            cinemaService.create(cinema);
            return cinema;
        } catch(DuplicateEntityException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch(EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
