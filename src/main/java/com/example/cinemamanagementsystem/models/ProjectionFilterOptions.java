package com.example.cinemamanagementsystem.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
public class ProjectionFilterOptions {
    private Room room;
    private Optional<LocalDateTime> intervalStart;
    private Optional<LocalDateTime> intervalEnd;

    public ProjectionFilterOptions(){
        this(null, null, null);
    }

    public ProjectionFilterOptions(Room room, LocalDateTime intervalStart, LocalDateTime intervalEnd) {
        this.room = room;
        this.intervalStart = Optional.ofNullable(intervalStart);
        this.intervalEnd = Optional.ofNullable(intervalEnd);
    }
}
