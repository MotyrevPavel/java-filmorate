package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */

@Data
@AllArgsConstructor
public class Film {
    private Long id;

    @NotNull
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Long duration;

    public void setDuration(Duration duration) {
        this.duration = duration.getSeconds();
    }
}
