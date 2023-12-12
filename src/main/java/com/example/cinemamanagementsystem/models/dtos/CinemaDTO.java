package com.example.cinemamanagementsystem.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CinemaDTO {
    private String cinemaName;
    private String cinemaAddress;
    private List<RoomDTO> rooms;
}
