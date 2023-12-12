CREATE DATABASE IF NOT EXISTS `cinema_management_system`;
USE `cinema_management_system`;

create table cities
(
    city_id   bigint auto_increment
        primary key,
    city_name varchar(50) not null
);

create table cinemas
(
    cinema_id      bigint auto_increment
        primary key,
    cinema_name    varchar(70)  not null,
    cinema_address varchar(100) not null,
    cinema_city_id bigint       not null,
    constraint cinemas_cities_city_id_fk
        foreign key (cinema_city_id) references cities (city_id)
);

create table movies
(
    movie_id       bigint auto_increment
        primary key,
    movie_title    varchar(50) not null,
    movie_duration varchar(20) not null
);

create table rooms
(
    room_id        bigint auto_increment
        primary key,
    room_number    int    not null,
    room_capacity  int    not null,
    room_cinema_id bigint not null,
    constraint rooms_cinemas_cinema_id_fk
        foreign key (room_cinema_id) references cinemas (cinema_id)
);

create table projections
(
    projection_id            bigint auto_increment
        primary key,
    projection_movie_id      bigint   not null,
    projection_starting_time datetime not null,
    projection_ending_time   datetime not null,
    projection_room_id       bigint   not null,
    constraint projections_movies_movie_id_fk
        foreign key (projection_movie_id) references movies (movie_id),
    constraint projections_rooms_room_id_fk
        foreign key (projection_room_id) references rooms (room_id)
);

create table reservations
(
    reservation_id            bigint auto_increment
        primary key,
    reservation_projection_id bigint     not null,
    reservation_code          varchar(6) not null,
    constraint reservations_projections_projection_id_fk
        foreign key (reservation_projection_id) references projections (projection_id)
);

create table seats
(
    seat_id      bigint auto_increment
        primary key,
    seat_row     int    not null,
    seat_column  int    not null,
    seat_room_id bigint not null,
    constraint seats_rooms_room_id_fk
        foreign key (seat_room_id) references rooms (room_id)
);

create table reservations_seats
(
    r_s_id             bigint auto_increment
        primary key,
    r_s_reservation_id bigint not null,
    r_s_seat_id        bigint not null,
    constraint reservations_seats_reservations_reservation_id_fk
        foreign key (r_s_reservation_id) references reservations (reservation_id),
    constraint reservations_seats_seats_seat_id_fk
        foreign key (r_s_seat_id) references seats (seat_id)
);

