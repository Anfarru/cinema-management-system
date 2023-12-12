package com.example.cinemamanagementsystem.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewCinemaDTO {

    @NotNull
    private String cinemaName;

    @NotNull
    private String cinemaAddress;

    @NotNull
    private String cinemaCity;
}
