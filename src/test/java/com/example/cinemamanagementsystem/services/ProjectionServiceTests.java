package com.example.cinemamanagementsystem.services;

import com.example.cinemamanagementsystem.Helpers;
import com.example.cinemamanagementsystem.exceptions.EntityNotFoundException;
import com.example.cinemamanagementsystem.exceptions.InvalidTimeframeException;
import com.example.cinemamanagementsystem.models.*;
import com.example.cinemamanagementsystem.repositories.*;
import com.example.cinemamanagementsystem.services.contracts.ProjectionService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
public class ProjectionServiceTests {

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
    ProjectionService projectionService;

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

        movieRepository.save(movie);
        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);
        projectionRepository.save(projection);

        assertEquals(projection, projectionService.getById(projection.getProjectionId()));
    }

    @Test
    void getById_Should_Throw_When_NotExists(){
        assertThrows(EntityNotFoundException.class, () -> projectionService.getById(999L));
    }

    @Test
    void getByRoomAndMovieAndStartingTime_Should_Return_When_Exists(){
        Movie movie = Helpers.createTestMovie();
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);
        Projection projection = Helpers.createTestProjection();
        projection.setMovie(movie);
        projection.setRoom(room);

        movieRepository.save(movie);
        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);
        projectionRepository.save(projection);

        assertEquals(projection,
                projectionService.getByRoomAndMovieAndStartingTime(
                        room,
                        movie,
                        LocalDateTime.of(2023, 11, 20, 19, 30, 00) ));
    }

    @Test
    void getByRoomAndMovieAndStartingTime_Should_Throw_When_NotExists(){
        Movie movie = Helpers.createTestMovie();
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);

        movieRepository.save(movie);
        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);

        assertThrows(EntityNotFoundException.class,
                () -> projectionService.getByRoomAndMovieAndStartingTime(
                        room,
                        movie,
                        LocalDateTime.of(2023, 11, 20, 19, 30, 00)));
    }

    @Test
    void getAllByRoom_Should_Return(){
        Movie movie = Helpers.createTestMovie();
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);
        Projection projection = Helpers.createTestProjection();
        projection.setMovie(movie);
        projection.setRoom(room);

        movieRepository.save(movie);
        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);
        projectionRepository.save(projection);

        assertEquals(1, projectionService.getAllByRoom(room).size());
    }

    @Test
    void filterAndSort_Should_Return_When_Exists(){
        Movie movie = Helpers.createTestMovie();
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);
        Room room2 = Helpers.createTestRoom();
        room2.setNumber(2);
        room2.setCinema(cinema);
        Projection projection = Helpers.createTestProjection();
        projection.setMovie(movie);
        projection.setRoom(room);
        Projection projection2 = Helpers.createTestProjection();
        projection2.setMovie(movie);
        projection2.setRoom(room2);

        movieRepository.save(movie);
        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);
        roomRepository.save(room2);
        projectionRepository.save(projection);
        projectionRepository.save(projection2);

        ProjectionFilterOptions projectionFilterOptions =
                new ProjectionFilterOptions(room, null, null);

        assertEquals(1, projectionService.filterAndSort(projectionFilterOptions).size());
    }

    @Test
    void filterAndSort_Should_Throw_When_NotExists(){
        Movie movie = Helpers.createTestMovie();
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);
        Room room2 = Helpers.createTestRoom();
        room2.setNumber(2);
        room2.setCinema(cinema);
        Projection projection = Helpers.createTestProjection();
        projection.setMovie(movie);
        projection.setRoom(room);

        movieRepository.save(movie);
        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);
        roomRepository.save(room2);
        projectionRepository.save(projection);

        ProjectionFilterOptions projectionFilterOptions =
                new ProjectionFilterOptions(room2, null, null);

        assertThrows(EntityNotFoundException.class,
                () -> projectionService.filterAndSort(projectionFilterOptions));
    }

    @Test
    void create_Should_Throw_When_OverlappingProjectionExists(){
        Movie movie = Helpers.createTestMovie();
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);
        Projection projection = Helpers.createTestProjection();
        projection.setMovie(movie);
        projection.setRoom(room);
        Projection overlappingProjection = Helpers.createOverlappingProjection();
        overlappingProjection.setMovie(movie);
        overlappingProjection.setRoom(room);

        movieRepository.save(movie);
        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);
        projectionRepository.save(projection);
        projectionRepository.save(overlappingProjection);

        assertThrows(InvalidTimeframeException.class, () -> projectionService.create(projection));
    }

    @Test
    void create_Should_Throw_When_ProjectionInThePast(){
        Movie movie = Helpers.createTestMovie();
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);
        Projection projection = Helpers.createPastProjection();
        projection.setRoom(room);
        projection.setMovie(movie);

        movieRepository.save(movie);
        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);
        projectionRepository.save(projection);

        assertThrows(InvalidTimeframeException.class, () -> projectionService.create(projection));
    }

    @Test
    void create_Should_Save_When_ValidProjection(){
        Movie movie = Helpers.createTestMovie();
        City city = Helpers.createTestCity();
        Cinema cinema = Helpers.createTestCinema();
        cinema.setCity(city);
        Room room = Helpers.createTestRoom();
        room.setCinema(cinema);
        Projection projection = Helpers.createTestProjection();
        projection.setRoom(room);
        projection.setMovie(movie);

        movieRepository.save(movie);
        cityRepository.save(city);
        cinemaRepository.save(cinema);
        roomRepository.save(room);

        projectionService.create(projection);

        assertEquals(1, projectionService.getAllByRoom(room).size());
    }
}
