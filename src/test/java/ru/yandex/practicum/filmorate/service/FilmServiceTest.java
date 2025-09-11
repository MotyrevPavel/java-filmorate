package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

class FilmServiceTest {

    private FilmService filmService;

    @BeforeEach
    void setUp() {
        filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateFilmWithNullName() {
        Film film = new Film(10L, null, "description",
                LocalDate.of(2025, 1, 1), 10L, new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.create(film));
        Assertions.assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateFilmWithEmptyName() {
        Film film = new Film(10L, "", "description",
                LocalDate.of(2025, 1, 1), 10L, new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.create(film));
        Assertions.assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateFilmWithLongDescription() {
        String description = "to long description".repeat(200);
        Film film = new Film(10L, "Name", description,
                LocalDate.of(2025, 1, 1), 10L, new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.create(film));
        Assertions.assertEquals("Максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateFilmWithIncorrectReleaseDate() {
        Film film = new Film(10L, "Name", "Description",
                LocalDate.of(1895, 12, 27), 10L, new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.create(film));
        Assertions.assertEquals("Дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateFilmWithZeroDuration() {
        Film film = new Film(10L, "Name", "Description",
                LocalDate.of(1895, 12, 29), 0L, new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.create(film));
        Assertions.assertEquals("Продолжительность фильма должна быть положительным числом.",
                exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateFilmWithNegativeDuration() {
        Film film = new Film(10L, "Name", "Description",
                LocalDate.of(1895, 12, 29), -1L, new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.create(film));
        Assertions.assertEquals("Продолжительность фильма должна быть положительным числом.",
                exception.getMessage());
    }

    @Test
    void shouldTReturnTrueWhenSuccessCreateFilm() {
        Film film = new Film(10L, "Name", "Description",
                LocalDate.of(1895, 12, 28), 10L, new HashSet<>());

        Film returnFilm = filmService.create(film);

        Assertions.assertEquals(film, returnFilm);
        Assertions.assertTrue(filmService.getAllFilms().contains(film));
        Assertions.assertEquals(1, filmService.getAllFilms().size());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithNullName() {
        Film film = new Film(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L, new HashSet<>());
        Film newFilm = new Film(10L, null, "description",
                LocalDate.of(2025, 1, 1), 10L, new HashSet<>());

        filmService.create(film);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.update(newFilm));
        Assertions.assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithEmptyName() {
        Film film = new Film(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L, new HashSet<>());
        Film newFilm = new Film(10L, "", "description",
                LocalDate.of(2025, 1, 1), 10L, new HashSet<>());

        filmService.create(film);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.update(newFilm));
        Assertions.assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithLongDescription() {
        String description = "to long description".repeat(200);
        Film film = new Film(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L, new HashSet<>());
        Film newFilm = new Film(10L, "Name", description,
                LocalDate.of(2025, 1, 1), 10L, new HashSet<>());

        filmService.create(film);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.update(newFilm));
        Assertions.assertEquals("Максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithIncorrectReleaseDate() {
        Film film = new Film(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L, new HashSet<>());
        Film newFilm = new Film(10L, "Name", "Description",
                LocalDate.of(1895, 12, 27), 10L, new HashSet<>());

        filmService.create(film);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.update(newFilm));
        Assertions.assertEquals("Дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithZeroDuration() {
        Film film = new Film(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L, new HashSet<>());
        Film newFilm = new Film(10L, "Name", "Description",
                LocalDate.of(1895, 12, 29), 0L, new HashSet<>());

        filmService.create(film);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.update(newFilm));
        Assertions.assertEquals("Продолжительность фильма должна быть положительным числом.",
                exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithNegativeDuration() {
        Film film = new Film(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L, new HashSet<>());
        Film newFilm = new Film(10L, "Name", "Description",
                LocalDate.of(1895, 12, 29), -1L, new HashSet<>());

        filmService.create(film);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.update(newFilm));
        Assertions.assertEquals("Продолжительность фильма должна быть положительным числом.",
                exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithoutId() {
        Film film = new Film(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L, new HashSet<>());
        Film newFilm = new Film(null, "Name", "Description",
                LocalDate.of(1895, 12, 29), 10L, new HashSet<>());

        filmService.create(film);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> filmService.update(newFilm));
        Assertions.assertEquals("Отсутствует ID",
                exception.getMessage());
    }

    @Test
    void shouldTReturnTrueWhenSuccessUpdateFilm() {
        Film film = new Film(10L, "null", "description",
                LocalDate.of(2025, 1, 1), 10L, new HashSet<>());

        filmService.create(film);

        Long id = filmService.getAllFilms().stream().findFirst().get().getId();

        Film newFilm = new Film(id, "Name", "Description",
                LocalDate.of(1895, 12, 28), 20L, new HashSet<>());


        Film returnFilm = filmService.update(newFilm);

        Assertions.assertEquals(newFilm, returnFilm);
        Assertions.assertTrue(filmService.getAllFilms().contains(newFilm));
        Assertions.assertFalse(filmService.getAllFilms().contains(film));
        Assertions.assertEquals(1, filmService.getAllFilms().size());
    }

    @Test
    void shouldReturnTrueWhenGetAllFilms() {
        Film film1 = new Film(10L, "film1", "description film 1",
                LocalDate.of(1895, 12, 28), 10L, new HashSet<>());
        Film film2 = new Film(15L, "film2", "description film 2",
                LocalDate.of(1895, 12, 29), 20L, new HashSet<>());
        Film film3 = new Film(20L, "film3", "description film 3",
                LocalDate.of(1895, 12, 30), 30L, new HashSet<>());

        filmService.create(film1);
        filmService.create(film2);
        filmService.create(film3);

        Collection<Film> films = filmService.getAllFilms();

        Assertions.assertEquals(3, films.size());
        Assertions.assertTrue(films.contains(film1));
        Assertions.assertTrue(films.contains(film2));
        Assertions.assertTrue(films.contains(film3));
    }
}