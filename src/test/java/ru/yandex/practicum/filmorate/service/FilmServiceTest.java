package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.dto.genre.NewGenreRequest;
import ru.yandex.practicum.filmorate.dto.mpa.NewMpaRequest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.dal.ram.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class FilmServiceTest {

    private FilmService filmService;
    private List<NewGenreRequest> genres;

    @BeforeEach
    void setUp() {
        filmService = new FilmService(new InMemoryFilmStorage());
        genres = new ArrayList<>();
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateFilmWithNullName() {
        NewFilmRequest film = new NewFilmRequest(10L, null, "description",
                LocalDate.of(2025, 1, 1), 10L, new NewMpaRequest(1L), genres);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.create(film));
        Assertions.assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateFilmWithEmptyName() {
        NewFilmRequest film = new NewFilmRequest(10L, "", "description",
                LocalDate.of(2025, 1, 1), 10L, new NewMpaRequest(1L), genres);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.create(film));
        Assertions.assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateFilmWithLongDescription() {
        String description = "to long description".repeat(200);
        NewFilmRequest film = new NewFilmRequest(10L, "Name", description,
                LocalDate.of(2025, 1, 1), 10L, new NewMpaRequest(1L), genres);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.create(film));
        Assertions.assertEquals("Максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateFilmWithIncorrectReleaseDate() {
        NewFilmRequest film = new NewFilmRequest(10L, "Name", "Description",
                LocalDate.of(1895, 12, 27), 10L, new NewMpaRequest(1L), genres);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.create(film));
        Assertions.assertEquals("Дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateFilmWithZeroDuration() {
        NewFilmRequest film = new NewFilmRequest(10L, "Name", "Description",
                LocalDate.of(1895, 12, 29), 0L, new NewMpaRequest(1L), genres);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.create(film));
        Assertions.assertEquals("Продолжительность фильма должна быть положительным числом.",
                exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateFilmWithNegativeDuration() {
        NewFilmRequest film = new NewFilmRequest(10L, "Name", "Description",
                LocalDate.of(1895, 12, 29), -1L, new NewMpaRequest(1L), genres);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.create(film));
        Assertions.assertEquals("Продолжительность фильма должна быть положительным числом.",
                exception.getMessage());
    }

    @Test
    void shouldTReturnTrueWhenSuccessCreateFilm() {
        NewFilmRequest film = new NewFilmRequest(10L, "Name", "Description",
                LocalDate.of(1895, 12, 28), 10L, new NewMpaRequest(1L), genres);

        FilmDto returnFilm = filmService.create(film);

        FilmDto filmDto = new FilmDto(returnFilm.getId(), "Name", "Description",
                LocalDate.of(1895, 12, 28), 10L, null, new NewMpaRequest(1L),
                genres);


        Assertions.assertEquals(filmDto, returnFilm);
        Assertions.assertTrue(filmService.getAllFilms().contains(filmDto));
        Assertions.assertEquals(1, filmService.getAllFilms().size());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithNullName() {
        NewFilmRequest film = new NewFilmRequest(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L, new NewMpaRequest(1L), genres);

        FilmDto filmDto = filmService.create(film);

        UpdateFilmRequest newFilm = new UpdateFilmRequest(
                filmDto.getId(),
                null,
                "description",
                LocalDate.of(2025, 1, 1),
                10L,
                new NewMpaRequest(1L),
                genres
        );


        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.update(newFilm));
        Assertions.assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithEmptyName() {
        NewFilmRequest film = new NewFilmRequest(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L, new NewMpaRequest(1L), genres);

        FilmDto filmDto = filmService.create(film);

        UpdateFilmRequest newFilm = new UpdateFilmRequest(
                filmDto.getId(),
                "",
                "description",
                LocalDate.of(2025, 1, 1),
                10L,
                new NewMpaRequest(1L),
                genres
        );

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.update(newFilm));
        Assertions.assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithLongDescription() {
        String description = "to long description".repeat(200);
        NewFilmRequest film = new NewFilmRequest(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L, new NewMpaRequest(1L), genres);

        FilmDto filmDto = filmService.create(film);

        UpdateFilmRequest newFilm = new UpdateFilmRequest(
                filmDto.getId(),
                "Name",
                description,
                LocalDate.of(2025, 1, 1),
                10L,
                new NewMpaRequest(1L),
                genres
        );


        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.update(newFilm));
        Assertions.assertEquals("Максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithIncorrectReleaseDate() {
        NewFilmRequest film = new NewFilmRequest(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L, new NewMpaRequest(1L), genres);

        FilmDto filmDto = filmService.create(film);

        UpdateFilmRequest newFilm = new UpdateFilmRequest(
                filmDto.getId(),
                "Name",
                "Description",
                LocalDate.of(1895, 12, 27),
                10L,
                new NewMpaRequest(1L),
                genres
        );

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.update(newFilm));
        Assertions.assertEquals("Дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithZeroDuration() {
        NewFilmRequest film = new NewFilmRequest(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L, new NewMpaRequest(1L), genres);

        FilmDto filmDto = filmService.create(film);

        UpdateFilmRequest newFilm = new UpdateFilmRequest(
                filmDto.getId(),
                "Name",
                "Description",
                LocalDate.of(1895, 12, 29),
                0L,
                new NewMpaRequest(1L),
                genres
        );

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.update(newFilm));
        Assertions.assertEquals("Продолжительность фильма должна быть положительным числом.",
                exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithNegativeDuration() {
        NewFilmRequest film = new NewFilmRequest(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L, new NewMpaRequest(1L), genres);

        FilmDto filmDto = filmService.create(film);

        UpdateFilmRequest newFilm = new UpdateFilmRequest(
                filmDto.getId(),
                "Name",
                "Description",
                LocalDate.of(1895, 12, 29),
                -1L,
                new NewMpaRequest(1L),
                genres
        );

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.update(newFilm));
        Assertions.assertEquals("Продолжительность фильма должна быть положительным числом.",
                exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithoutId() {
        NewFilmRequest film = new NewFilmRequest(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L, new NewMpaRequest(1L), genres);

        filmService.create(film);

        UpdateFilmRequest newFilm = new UpdateFilmRequest(
                null,
                "Name",
                "Description",
                LocalDate.of(1895, 12, 29),
                10L,
                new NewMpaRequest(1L),
                genres
        );

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.update(newFilm));
        Assertions.assertEquals("Отсутствует ID",
                exception.getMessage());
    }

    @Test
    void shouldTReturnTrueWhenSuccessUpdateFilm() {
        NewFilmRequest film = new NewFilmRequest(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L, null, genres);

        FilmDto filmDto = filmService.create(film);

        UpdateFilmRequest newFilm = new UpdateFilmRequest(
                filmDto.getId(),
                "Name",
                "Description",
                LocalDate.of(1895, 12, 28),
                20L,
                null,
                genres
        );

        FilmDto newFilmDto = new FilmDto(newFilm.getId(), newFilm.getName(), newFilm.getDescription(),
                newFilm.getReleaseDate(), newFilm.getDuration(), null, newFilm.getMpa(),
                newFilm.getGenres());

        FilmDto returnFilm = filmService.update(newFilm);

        Assertions.assertEquals(newFilmDto, returnFilm);
        Assertions.assertTrue(filmService.getAllFilms().contains(newFilmDto));
        Assertions.assertFalse(filmService.getAllFilms().contains(filmDto));
        Assertions.assertEquals(1, filmService.getAllFilms().size());
    }

    @Test
    void shouldReturnTrueWhenGetAllFilms() {
        NewFilmRequest film1 = new NewFilmRequest(10L, "film1", "description film 1",
                LocalDate.of(1895, 12, 28), 10L, new NewMpaRequest(1L), genres);
        NewFilmRequest film2 = new NewFilmRequest(15L, "film2", "description film 2",
                LocalDate.of(1895, 12, 29), 20L, new NewMpaRequest(1L), genres);
        NewFilmRequest film3 = new NewFilmRequest(20L, "film3", "description film 3",
                LocalDate.of(1895, 12, 30), 30L, new NewMpaRequest(1L), genres);

        FilmDto filmDto1 = filmService.create(film1);
        FilmDto filmDto2 = filmService.create(film2);
        FilmDto filmDto3 = filmService.create(film3);

        FilmDto film4 = new FilmDto(filmDto1.getId(), "film1", "description film 1",
                LocalDate.of(1895, 12, 28), 10L, null,
                new NewMpaRequest(1L), genres);
        FilmDto film5 = new FilmDto(filmDto2.getId(), "film2", "description film 2",
                LocalDate.of(1895, 12, 29), 20L, null,
                new NewMpaRequest(1L), genres);
        FilmDto film6 = new FilmDto(filmDto3.getId(), "film3", "description film 3",
                LocalDate.of(1895, 12, 30), 30L, null,
                new NewMpaRequest(1L), genres);

        Collection<FilmDto> films = filmService.getAllFilms();

        Assertions.assertEquals(3, films.size());
        Assertions.assertTrue(films.contains(film4));
        Assertions.assertTrue(films.contains(film5));
        Assertions.assertTrue(films.contains(film6));
    }
}