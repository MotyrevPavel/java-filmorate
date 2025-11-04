package ru.yandex.practicum.filmorate.dto.film;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.genre.GenreMapper;
import ru.yandex.practicum.filmorate.dto.genre.NewGenreRequest;
import ru.yandex.practicum.filmorate.dto.mpa.MpaMapper;
import ru.yandex.practicum.filmorate.dto.mpa.NewMpaRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {
    public static Film mapToFilm(NewFilmRequest request) {
        Mpa mpa = null;
        if (request.getMpa() != null) {
            mpa = new Mpa(request.getMpa().getId(), null);
        }

        List<Genre> genres = null;
        if (request.getGenres() != null) {
            genres = request.getGenres().stream()
                    .map(genre -> new Genre(genre.getId(), null))
                    .toList();
        }

        return new Film(
                null,
                request.getName(),
                request.getDescription(),
                request.getReleaseDate(),
                request.getDuration(),
                null,
                mpa,
                genres
        );
    }

    public static FilmDto mapToFilmDto(Film film) {
        NewMpaRequest newMpaRequest = null;
        if (film.getMpa() != null) {
            newMpaRequest = new NewMpaRequest(film.getMpa().getId());
        }

        List<NewGenreRequest> genres = null;
        if (film.getGenres() != null) {
            genres = film.getGenres().stream()
                    .map(genre -> new NewGenreRequest(genre.getId()))
                    .toList();
        }

        return new FilmDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getLikes(),
                newMpaRequest,
                genres
        );
    }

    public static Film updateFilmFields(Film film, UpdateFilmRequest request) {
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());

        if (request.hasMpa()) {
            film.setMpa(new Mpa(request.getMpa().getId(), null));
        }
        if (request.hasGenres()) {
            List<Genre> genres = request.getGenres().stream()
                    .map(NewGenreRequest::getId)
                    .map(id -> new Genre(id, null))
                    .toList();

            film.setGenres(genres);
        }

        return film;
    }

    public static FilmDtoForGet mapToFilmGetDto(Film film) {
        List<GenreDto> genreDtoSet = film.getGenres().stream()
                .map(GenreMapper::mapToGenreDto)
                .toList();
        return new FilmDtoForGet(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getLikes(),
                MpaMapper.mapToMpaDto(film.getMpa()),
                genreDtoSet
        );
    }
}
