package com.example.cinemamanagementsystem.services;

import com.example.cinemamanagementsystem.Helpers;
import com.example.cinemamanagementsystem.exceptions.DuplicateEntityException;
import com.example.cinemamanagementsystem.exceptions.EntityNotFoundException;
import com.example.cinemamanagementsystem.models.Cinema;
import com.example.cinemamanagementsystem.models.City;
import com.example.cinemamanagementsystem.models.Reservation;
import com.example.cinemamanagementsystem.repositories.*;
import com.example.cinemamanagementsystem.services.contracts.CinemaService;
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
public class CinemaServiceTests {

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
    CinemaService cinemaService;

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
        cityRepository.save(city);
        cinemaRepository.save(cinema);

        assertEquals(cinema, cinemaService.getById(cinema.getCinemaId()));
    }

    @Test
    void getById_Should_Throw_When_NotExists(){
        assertThrows(EntityNotFoundException.class, () -> cinemaService.getById(999L));
    }

    @Test
    void getByAddress_Should_Return_When_Exists(){
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        cityRepository.save(city);
        cinemaRepository.save(cinema);

        assertEquals(cinema, cinemaService.getByAddress("ul. Akademik Andrei Saharov 2"));
    }

    @Test
    void getByAddress_Should_Throw_When_NotExists(){
        assertThrows(EntityNotFoundException.class,
                () -> cinemaService.getByAddress("ul. Akademik Andrei Saharov 2"));
    }

    @Test
    void getAll_Should_Return(){
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        Cinema cinema2 = Helpers.createTestCinema();
        cinema.setCity(city);
        cinema2.setCity(city);
        cinema2.setCinemaAddress("Arsenalski Blvd 2");
        cityRepository.save(city);
        cinemaRepository.save(cinema);
        cinemaRepository.save(cinema2);

        assertEquals(2, cinemaService.getAll().size());
    }

    @Test
    void getAllByCity_Should_Return(){
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        Cinema cinema2 = Helpers.createTestCinema();
        cinema.setCity(city);
        cinema2.setCity(city);
        cinema2.setCinemaAddress("Arsenalski Blvd 2");
        cityRepository.save(city);
        cinemaRepository.save(cinema);
        cinemaRepository.save(cinema2);

        assertEquals(2, cinemaService.getAllByCity(city).size());
    }

    @Test
    void create_Should_Throw_DuplicateException_When_Duplicate_Exists(){
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        cityRepository.save(city);
        cinemaRepository.save(cinema);

        Cinema cinema2 = Helpers.createTestCinema();
        cinema2.setCity(city);

        assertThrows(DuplicateEntityException.class, () -> cinemaService.create(cinema2));
    }

    @Test
    void create_Should_Save_When_Duplicate_NotExists(){
        City city = Helpers.createTestCity();
        cityRepository.save(city);

        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        cinemaService.create(cinema);

        assertEquals(1, cinemaService.getAll().size());
    }
}
