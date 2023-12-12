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
public class SeatDTO {

    @NotNull
    private int seatRow;

    @NotNull
    private int seatColumn;
}
