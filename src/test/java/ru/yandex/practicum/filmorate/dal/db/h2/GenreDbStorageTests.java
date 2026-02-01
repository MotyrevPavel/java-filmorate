package ru.yandex.practicum.filmorate.dal.db.h2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dal.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate.dal.db.h2")
public class GenreDbStorageTests {
    private final GenreStorage genreStorage;

    public GenreDbStorageTests(@Qualifier("genreDbStorage") GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Test
    public void testFindGenreById() {

        Genre genre = genreStorage.getGenre(3L);

        assertThat(genre).isNotNull()
                .hasFieldOrPropertyWithValue("id", 3L)
                .hasFieldOrPropertyWithValue("name", "Фантастика");
    }

    @Test
    public void testGetAllGenres() {
        Collection<Genre> genres = genreStorage.getAllGenres();
        Genre genre1 = genres.stream().toList().getFirst();
        Genre genre10 = genres.stream().toList().getLast();

        assertThat(genres)
                .isNotNull()
                .isNotEmpty()
                .hasSize(10);

        assertThat(genre1).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Драма");

        assertThat(genre10).isNotNull()
                .hasFieldOrPropertyWithValue("id", 10L)
                .hasFieldOrPropertyWithValue("name", "Детектив");
    }
}
