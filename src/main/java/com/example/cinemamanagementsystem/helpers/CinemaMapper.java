package com.example.cinemamanagementsystem.helpers;

import com.example.cinemamanagementsystem.models.Cinema;
import com.example.cinemamanagementsystem.models.City;
import com.example.cinemamanagementsystem.models.Room;
import com.example.cinemamanagementsystem.models.dtos.CinemaDTO;
import com.example.cinemamanagementsystem.models.dtos.NewCinemaDTO;
import com.example.cinemamanagementsystem.models.dtos.RoomDTO;
import com.example.cinemamanagementsystem.services.contracts.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CinemaMapper {

    private final CityService cityService;

    @Autowired
    public CinemaMapper(CityService cityService) {
        this.cityService = cityService;
    }

    public CinemaDTO toDTO(Cinema cinema){
        CinemaDTO cinemaDTO = new CinemaDTO();
        cinemaDTO.setCinemaName(cinema.getCinemaName());
        String fullAddress =
                String.format("%s, %s", cinema.getCity().getName(), cinema.getCinemaAddress());
        cinemaDTO.setCinemaAddress(fullAddress);

        List<RoomDTO> roomDTOs = new ArrayList<>();
        for(Room room : cinema.getRooms()){
            RoomDTO roomDTO = new RoomDTO();
            roomDTO.setNumber(room.getNumber());
            roomDTO.setCapacity(room.getCapacity());
            roomDTOs.add(roomDTO);
        }
        cinemaDTO.setRooms(roomDTOs);
        return cinemaDTO;
    }

    public Cinema fromDTO(NewCinemaDTO newCinemaDTO){
        Cinema cinema = new Cinema();
        cinema.setCinemaName(newCinemaDTO.getCinemaName());
        cinema.setCinemaAddress(newCinemaDTO.getCinemaAddress());
        City city = cityService.getByName(newCinemaDTO.getCinemaCity());
        cinema.setCity(city);
        return cinema;
    }

    public List<CinemaDTO> getCinemasAndRooms(List<Cinema> cinemas){
        List<CinemaDTO> cinemaDTOs = new ArrayList<>();
        for(Cinema cinema : cinemas){
            CinemaDTO cinemaDTO = toDTO(cinema);
            cinemaDTOs.add(cinemaDTO);
        }
        return cinemaDTOs;
    }
}
