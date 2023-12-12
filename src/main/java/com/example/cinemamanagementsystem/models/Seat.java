package com.example.cinemamanagementsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private long seatId;

    @Column(name = "seat_row")
    private int seatRow;

    @Column(name = "seat_column")
    private int seatColumn;

    @OneToOne
    @JoinColumn(name = "seat_room_id")
    private Room room;

    public Seat(int seatRow, int seatColumn, Room room) {
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.room = room;
    }
}
