package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateFilmWithNullName() {
        Film film = new Film(10L, null, "description",
                LocalDate.of(2025, 1, 1), 10L);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmController.create(film));
        Assertions.assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateFilmWithEmptyName() {
        Film film = new Film(10L, "", "description",
                LocalDate.of(2025, 1, 1), 10L);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmController.create(film));
        Assertions.assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateFilmWithLongDescription() {
        String description = "to long description".repeat(200);
        Film film = new Film(10L, "Name", description,
                LocalDate.of(2025, 1, 1), 10L);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmController.create(film));
        Assertions.assertEquals("Максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateFilmWithIncorrectReleaseDate() {
        Film film = new Film(10L, "Name", "Description",
                LocalDate.of(1895, 12, 27), 10L);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmController.create(film));
        Assertions.assertEquals("Дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateFilmWithZeroDuration() {
        Film film = new Film(10L, "Name", "Description",
                LocalDate.of(1895, 12, 29), 0L);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmController.create(film));
        Assertions.assertEquals("Продолжительность фильма должна быть положительным числом.",
                exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateFilmWithNegativeDuration() {
        Film film = new Film(10L, "Name", "Description",
                LocalDate.of(1895, 12, 29), -1L);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmController.create(film));
        Assertions.assertEquals("Продолжительность фильма должна быть положительным числом.",
                exception.getMessage());
    }

    @Test
    void shouldTReturnTrueWhenSuccessCreateFilm() {
        Film film = new Film(10L, "Name", "Description",
                LocalDate.of(1895, 12, 28), 10L);

        Film returnFilm = filmController.create(film);

        Assertions.assertEquals(film, returnFilm);
        Assertions.assertTrue(filmController.getFilms().containsValue(film));
        Assertions.assertEquals(1, filmController.getFilms().size());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithNullName() {
        Film film = new Film(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L);
        Film newFilm = new Film(10L, null, "description",
                LocalDate.of(2025, 1, 1), 10L);

        filmController.create(film);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmController.update(newFilm));
        Assertions.assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithEmptyName() {
        Film film = new Film(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L);
        Film newFilm = new Film(10L, "", "description",
                LocalDate.of(2025, 1, 1), 10L);

        filmController.create(film);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmController.update(newFilm));
        Assertions.assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithLongDescription() {
        String description = "to long description".repeat(200);
        Film film = new Film(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L);
        Film newFilm = new Film(10L, "Name", description,
                LocalDate.of(2025, 1, 1), 10L);

        filmController.create(film);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmController.update(newFilm));
        Assertions.assertEquals("Максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithIncorrectReleaseDate() {
        Film film = new Film(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L);
        Film newFilm = new Film(10L, "Name", "Description",
                LocalDate.of(1895, 12, 27), 10L);

        filmController.create(film);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmController.update(newFilm));
        Assertions.assertEquals("Дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithZeroDuration() {
        Film film = new Film(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L);
        Film newFilm = new Film(10L, "Name", "Description",
                LocalDate.of(1895, 12, 29), 0L);

        filmController.create(film);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmController.update(newFilm));
        Assertions.assertEquals("Продолжительность фильма должна быть положительным числом.",
                exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithNegativeDuration() {
        Film film = new Film(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L);
        Film newFilm = new Film(10L, "Name", "Description",
                LocalDate.of(1895, 12, 29), -1L);

        filmController.create(film);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmController.update(newFilm));
        Assertions.assertEquals("Продолжительность фильма должна быть положительным числом.",
                exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithoutId() {
        Film film = new Film(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L);
        Film newFilm = new Film(null, "Name", "Description",
                LocalDate.of(1895, 12, 29), 10L);

        filmController.create(film);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmController.update(newFilm));
        Assertions.assertEquals("Отсутствует ID",
                exception.getMessage());
    }

    @Test
    void shouldTReturnTrueWhenSuccessUpdateFilm() {
        Film film = new Film(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L);

        filmController.create(film);

        Long id = filmController.getFilms().values().stream().findFirst().get().getId();

        Film newFilm = new Film(id, "Name", "Description",
                LocalDate.of(1895, 12, 28), 20L);


        Film returnFilm = filmController.update(newFilm);

        Assertions.assertEquals(newFilm, returnFilm);
        Assertions.assertTrue(filmController.getFilms().containsValue(newFilm));
        Assertions.assertFalse(filmController.getFilms().containsValue(film));
        Assertions.assertEquals(1, filmController.getFilms().size());
    }

    @Test
    void shouldReturnTrueWhenGetAllFilms() {
        Film film1 = new Film(10L, "film1", "description film 1",
                LocalDate.of(1895, 12, 28), 10L);
        Film film2 = new Film(15L, "film2", "description film 2",
                LocalDate.of(1895, 12, 29), 20L);
        Film film3 = new Film(20L, "film3", "description film 3",
                LocalDate.of(1895, 12, 30), 30L);

        filmController.create(film1);
        filmController.create(film2);
        filmController.create(film3);

        Collection<Film> films = filmController.getAllFilms();

        Assertions.assertEquals(3, films.size());
        Assertions.assertTrue(films.contains(film1));
        Assertions.assertTrue(films.contains(film2));
        Assertions.assertTrue(films.contains(film3));
    }
}