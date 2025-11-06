package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.dto.genre.NewGenreRequest;
import ru.yandex.practicum.filmorate.dto.mpa.NewMpaRequest;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateFilmRequest {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Long duration;
    private NewMpaRequest mpa;
    private List<NewGenreRequest> genres;

    public boolean hasMpa() {
        return (mpa != null);
    }

    public boolean hasGenres() {
        return (genres != null && !genres.isEmpty());
    }
}
