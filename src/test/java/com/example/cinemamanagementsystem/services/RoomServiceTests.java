package com.example.cinemamanagementsystem.services;

import com.example.cinemamanagementsystem.Helpers;
import com.example.cinemamanagementsystem.exceptions.EntityNotFoundException;
import com.example.cinemamanagementsystem.models.Cinema;
import com.example.cinemamanagementsystem.models.City;
import com.example.cinemamanagementsystem.models.Reservation;
import com.example.cinemamanagementsystem.models.Room;
import com.example.cinemamanagementsystem.repositories.*;
import com.example.cinemamanagementsystem.services.contracts.RoomService;
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
public class RoomServiceTests {

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
    RoomService roomService;

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

        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);

        assertEquals(room, roomService.getById(room.getRoomId()));
    }

    @Test
    void getById_Should_Throw_When_NotExists(){
        assertThrows(EntityNotFoundException.class, () -> roomService.getById(999L));
    }

    @Test
    void getAllByCinema_Should_Return(){
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);
        Room room2 = Helpers.createTestRoom();
        room2.setNumber(2);
        room2.setCinema(cinema);

        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);
        roomRepository.save(room2);

        assertEquals(2, roomService.getAllByCinema(cinema).size());
    }

    @Test
    void getByCinemaAndNumber_Should_Return_When_Exists(){
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);

        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);

        assertEquals(room, roomService.getByCinemaAndNumber(cinema, 1));
    }

    @Test
    void getByCinemaAndNumber_Should_Throw_When_NotExists(){
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);

        cityRepository.save(city);
        cinemaRepository.save(cinema);

        assertThrows(EntityNotFoundException.class, () -> roomService.getByCinemaAndNumber(cinema, 1));
    }
}
