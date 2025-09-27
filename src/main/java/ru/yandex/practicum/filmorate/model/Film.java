package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

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

    /*@ManyToMany
    @JoinTable(
            name = "film_likes",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )*/
    private Set<Long> likes;

    /*@ManyToMany
    @JoinTable(
            name = "film_genres",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )*/
    private Set<Genre> genreList;

    private Raiting raiting;
}
