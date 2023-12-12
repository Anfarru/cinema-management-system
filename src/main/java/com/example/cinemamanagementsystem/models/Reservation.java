package com.example.cinemamanagementsystem.models;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private long reservationId;

    @OneToOne
    @JoinColumn(name = "reservation_projection_id")
    private Projection projection;

    @Column(name = "reservation_code")
    private String reservationCode;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "reservations_seats",
            joinColumns = @JoinColumn(name = "r_s_reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "r_s_seat_id")
    )
    private List<Seat> seats = new ArrayList<>();

    public Reservation(Projection projection, String reservationCode) {
        this.projection = projection;
        this.reservationCode = reservationCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return reservationId == that.reservationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
    }
}
