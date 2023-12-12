package com.example.cinemamanagementsystem.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "rooms")
public class Room {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private long roomId;

    @Column(name = "room_number")
    private int number;

    @Column(name = "room_capacity")
    private int capacity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "room_cinema_id")
    private Cinema cinema;

    public Room(int number, int capacity, Cinema cinema) {
        this.number = number;
        this.capacity = capacity;
        this.cinema = cinema;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return roomId == room.roomId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId);
    }
}
