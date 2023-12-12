package com.example.cinemamanagementsystem.services;

import com.example.cinemamanagementsystem.Helpers;
import com.example.cinemamanagementsystem.exceptions.EmptyReservationException;
import com.example.cinemamanagementsystem.exceptions.EntityNotFoundException;
import com.example.cinemamanagementsystem.models.*;
import com.example.cinemamanagementsystem.repositories.*;
import com.example.cinemamanagementsystem.services.contracts.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
public class ReservationServiceTests {

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
    ReservationService reservationService;

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
        Movie movie = Helpers.createTestMovie();
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);
        Projection projection = Helpers.createTestProjection();
        projection.setMovie(movie);
        projection.setRoom(room);
        Reservation reservation = Helpers.createTestReservation();
        reservation.setProjection(projection);

        movieRepository.save(movie);
        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);
        projectionRepository.save(projection);
        reservationRepository.save(reservation);

        assertEquals(reservation, reservationService.getById(reservation.getReservationId()));
    }

    @Test
    void getById_Should_Throw_When_NotExists(){
        assertThrows(EntityNotFoundException.class, () -> reservationService.getById(999L));
    }

    @Test
    void getAll_Should_Return(){
        Movie movie = Helpers.createTestMovie();
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);
        Projection projection = Helpers.createTestProjection();
        projection.setMovie(movie);
        projection.setRoom(room);
        Reservation reservation = Helpers.createTestReservation();
        reservation.setProjection(projection);

        movieRepository.save(movie);
        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);
        projectionRepository.save(projection);
        reservationRepository.save(reservation);

        assertEquals(1, reservationService.getAll().size());
    }

    @Test
    void getAllByProjection_Should_Return(){
        Movie movie = Helpers.createTestMovie();
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);
        Projection projection = Helpers.createTestProjection();
        projection.setMovie(movie);
        projection.setRoom(room);
        Reservation reservation = Helpers.createTestReservation();
        reservation.setProjection(projection);

        movieRepository.save(movie);
        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);
        projectionRepository.save(projection);
        reservationRepository.save(reservation);

        assertEquals(1, reservationService.getAllByProjection(projection).size());
    }

    @Test
    void create_Should_Throw_When_ReservationEmpty(){
        Movie movie = Helpers.createTestMovie();
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);
        Projection projection = Helpers.createTestProjection();
        projection.setMovie(movie);
        projection.setRoom(room);
        Reservation reservation = Helpers.createTestReservation();
        reservation.setProjection(projection);

        movieRepository.save(movie);
        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);
        projectionRepository.save(projection);
        reservationRepository.save(reservation);

        assertThrows(EmptyReservationException.class, () -> reservationService.create(reservation, projection));
    }

    @Test
    void create_Should_Save_When_ValidReservation(){
        Movie movie = Helpers.createTestMovie();
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);
        Projection projection = Helpers.createTestProjection();
        projection.setMovie(movie);
        projection.setRoom(room);
        Seat seat = Helpers.createTestSeat();
        seat.setRoom(room);
        Seat seat2 = Helpers.createTestSeat();
        seat2.setSeatColumn(5);
        seat2.setRoom(room);
        Reservation reservation = Helpers.createTestReservation();
        reservation.setProjection(projection);
        reservation.getSeats().add(seat);
        reservation.getSeats().add(seat2);

        movieRepository.save(movie);
        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);
        projectionRepository.save(projection);
        seatRepository.save(seat);
        seatRepository.save(seat2);

        reservationService.create(reservation, projection);
        assertEquals(1, reservationService.getAll().size());
    }
}
