package ru.yandex.practicum.filmorate.dal.db.h2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dal.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate.dal.db.h2")
public class FilmDbStorageTests {
    private final FilmStorage filmStorage;

    public FilmDbStorageTests(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Test
    public void testFindFilmById() {
        Film film = filmStorage.getFilm(1L);

        assertThat(film).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Начало")
                .hasFieldOrPropertyWithValue("description", "Сложный психологический триллер о сновидениях.")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2010, 7, 16))
                .hasFieldOrPropertyWithValue("duration", 148L);
    }

    @Test
    public void testCreateNewFilm() {
        Film test = new Film(null, "TestName", "its the description",
                LocalDate.of(1990, 5, 15), 100L, new HashSet<>(),
                new Mpa(4L, null), new ArrayList<>());
        Film createdFilm = filmStorage.create(test);
        Film receivedFilm = filmStorage.getFilm(createdFilm.getId());

        assertThat(receivedFilm).isNotNull()
                .hasFieldOrPropertyWithValue("id", receivedFilm.getId())
                .hasFieldOrPropertyWithValue("name", "TestName")
                .hasFieldOrPropertyWithValue("description", "its the description")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1990, 5, 15))
                .hasFieldOrPropertyWithValue("duration", 100L)
                .hasFieldOrPropertyWithValue("mpa", new Mpa(4L, null));
    }

    @Test
    public void testUpdateUser() {
        Film film = new Film(null, "TestName", "its the description",
                LocalDate.of(1990, 5, 15), 100L, new HashSet<>(),
                new Mpa(4L, null), new ArrayList<>());
        Film createdFilm = filmStorage.create(film);
        Film updateFilm = new Film(createdFilm.getId(), "The Best Film", "Film about the best people",
                LocalDate.of(1990, 5, 20), 110L, new HashSet<>(),
                new Mpa(3L, null), new ArrayList<>());

        filmStorage.update(updateFilm);
        Film updatedFilm = filmStorage.getFilm(createdFilm.getId());

        assertThat(updatedFilm).isNotNull()
                .hasFieldOrPropertyWithValue("id", createdFilm.getId())
                .hasFieldOrPropertyWithValue("name", "The Best Film")
                .hasFieldOrPropertyWithValue("description", "Film about the best people")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1990, 5, 20))
                .hasFieldOrPropertyWithValue("duration", 110L)
                .hasFieldOrPropertyWithValue("mpa", new Mpa(3L, null));
    }

    @Test
    public void testGetAllUsers() {
        Collection<Film> films = filmStorage.getAllFilms();
        Film film1 = films.stream().toList().getFirst();
        Film film5 = films.stream().toList().getLast();

        assertThat(films)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5);

        assertThat(film1).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Начало")
                .hasFieldOrPropertyWithValue("description", "Сложный психологический триллер о сновидениях.")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2010, 7, 16))
                .hasFieldOrPropertyWithValue("duration", 148L);

        assertThat(film5).isNotNull()
                .hasFieldOrPropertyWithValue("id", 5L)
                .hasFieldOrPropertyWithValue("name", "Властелин колец: Братство кольца")
                .hasFieldOrPropertyWithValue("description", "Эпическая фэнтези‑сага.")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2001, 12, 19))
                .hasFieldOrPropertyWithValue("duration", 178L);
    }
}
