package com.example.cinemamanagementsystem.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private long cityId;

    @NotBlank
    @Column(name = "city_name")
    private String name;

    public City(String name) {
        this.name = name;
    }
}
