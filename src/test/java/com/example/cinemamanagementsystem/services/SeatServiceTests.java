package com.example.cinemamanagementsystem.services;

import com.example.cinemamanagementsystem.Helpers;
import com.example.cinemamanagementsystem.exceptions.EntityNotFoundException;
import com.example.cinemamanagementsystem.models.*;
import com.example.cinemamanagementsystem.repositories.*;
import com.example.cinemamanagementsystem.services.contracts.SeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class SeatServiceTests {

    @Container
    private static MariaDBContainer<?> mariaDBContainer =
            new MariaDBContainer<>("mariadb:10.6")
                    .withDatabaseName("cinema_management_system");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariaDBContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mariaDBContainer::getUsername);
        registry.add("spring.datasource.password", mariaDBContainer::getPassword);

        registry.add("spring.flyway.url", mariaDBContainer::getJdbcUrl);
        registry.add("spring.flyway.user", mariaDBContainer::getUsername);
        registry.add("spring.flyway.password", mariaDBContainer::getPassword);
    }

    @Autowired
    SeatRepository seatRepository;
    @Autowired
    CityRepository cityRepository;
    @Autowired
    CinemaRepository cinemaRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ProjectionRepository projectionRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    SeatService seatService;

    @BeforeEach
    void setUp(){
        List<Reservation> reservations = reservationRepository.findAll();
        for(Reservation reservation : reservations){
            reservation.getSeats().clear();
        }
        reservationRepository.deleteAll();
        seatRepository.deleteAll();
        projectionRepository.deleteAll();
        roomRepository.deleteAll();
        cinemaRepository.deleteAll();
        cityRepository.deleteAll();
        movieRepository.deleteAll();
    }

    @Test
    void getById_Should_Return_When_Exists(){
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);
        Seat seat = Helpers.createTestSeat();
        seat.setRoom(room);

        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);
        seatRepository.save(seat);

        assertEquals(seat, seatService.getById(seat.getSeatId()));
    }

    @Test
    void getById_Should_Throw_When_NotExists(){
        assertThrows(EntityNotFoundException.class, () -> seatService.getById(999L));
    }

    @Test
    void getByRoomAndSeatRowAndSeatColumn_Should_Throw_When_NotExists(){
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);

        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);

        assertThrows(EntityNotFoundException.class,
                () -> seatService.getByRoomAndSeatRowAndSeatColumn(room, 5, 5));
    }

    @Test
    void getAllByRoom_Should_Return(){
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);

        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);

        Seat seat = Helpers.createTestSeat();
        seat.setRoom(room);
        Seat seat2 = Helpers.createTestSeat();
        seat2.setSeatColumn(5);
        seat2.setRoom(room);
        seatRepository.save(seat);
        seatRepository.save(seat2);

        assertEquals(2, seatService.getAllByRoom(room).size());
    }

    @Test
    void getAllAvailable_Should_Return(){
        Movie movie = Helpers.createTestMovie();
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);
        Seat seat = Helpers.createTestSeat();
        seat.setRoom(room);
        Seat seat2 = Helpers.createTestSeat();
        seat2.setSeatColumn(5);
        seat2.setRoom(room);

        movieRepository.save(movie);
        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);
        seatRepository.save(seat);
        seatRepository.save(seat2);

        Projection projection = Helpers.createTestProjection();
        projection.setRoom(room);
        projection.setMovie(movie);
        projectionRepository.save(projection);

        assertEquals(2, seatService.getAllAvailable(projection).size());
    }

    @Test
    void getAllTaken_Should_Return(){
        Movie movie = Helpers.createTestMovie();
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);
        Seat seat = Helpers.createTestSeat();
        seat.setRoom(room);
        Seat seat2 = Helpers.createTestSeat();
        seat2.setSeatColumn(5);
        seat2.setRoom(room);

        movieRepository.save(movie);
        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);
        seatRepository.save(seat);
        seatRepository.save(seat2);

        LocalDateTime startingTime = LocalDateTime.of(2023, 11, 20, 19, 30, 00);
        LocalDateTime endingTime = LocalDateTime.of(2023, 11, 20, 21, 11, 00);
        Projection projection = new Projection(movie, startingTime, endingTime, room);
        projectionRepository.save(projection);

        Reservation reservation = new Reservation();
        reservation.setReservationCode("LLoHP");
        reservation.setProjection(projection);
        reservation.getSeats().add(seat);
        reservationRepository.save(reservation);

        assertEquals(1, seatService.getAllTaken(projection).size());
    }

    @Test
    void isPartOfReservation_Should_Return_False_When_NotReserved(){
        Movie movie = Helpers.createTestMovie();
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);
        Seat seat = Helpers.createTestSeat();
        seat.setRoom(room);
        Seat seat2 = Helpers.createTestSeat();
        seat2.setSeatColumn(5);
        seat2.setRoom(room);

        movieRepository.save(movie);
        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);
        seatRepository.save(seat);
        seatRepository.save(seat2);

        LocalDateTime startingTime = LocalDateTime.of(2023, 11, 20, 19, 30, 00);
        LocalDateTime endingTime = LocalDateTime.of(2023, 11, 20, 21, 11, 00);
        Projection projection = new Projection(movie, startingTime, endingTime, room);
        projectionRepository.save(projection);

        Reservation reservation = new Reservation();
        reservation.setReservationCode("LLoHP");
        reservation.setProjection(projection);
        reservation.getSeats().add(seat);
        reservationRepository.save(reservation);

        assertFalse(seatService.isPartOfReservation(seat2, projection));
    }
}
