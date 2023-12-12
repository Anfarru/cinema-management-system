package com.example.cinemamanagementsystem.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectionDTO {

    @NotNull
    private String title;

    @NotNull
    @Future(message = "Starting time must be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Valid
    private LocalDateTime startingTime;
}
