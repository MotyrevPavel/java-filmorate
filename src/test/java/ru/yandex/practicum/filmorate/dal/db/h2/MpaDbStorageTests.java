package ru.yandex.practicum.filmorate.dal.db.h2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dal.MpaStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate.dal.db.h2")
public class MpaDbStorageTests {
    private final MpaStorage mpaStorage;

    public MpaDbStorageTests(@Qualifier("mpaDbStorage") MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Test
    public void testFindMpaById() {

        Mpa mpa = mpaStorage.getMpa(3L);

        assertThat(mpa).isNotNull()
                .hasFieldOrPropertyWithValue("id", 3L)
                .hasFieldOrPropertyWithValue("name", "PG-13");
    }

    @Test
    public void testGetAllGenres() {
        Collection<Mpa> mpas = mpaStorage.getAllMpa();
        Mpa mpa1 = mpas.stream().toList().getFirst();
        Mpa mpa5 = mpas.stream().toList().getLast();

        assertThat(mpas)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5);

        assertThat(mpa1).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "G");

        assertThat(mpa5).isNotNull()
                .hasFieldOrPropertyWithValue("id", 5L)
                .hasFieldOrPropertyWithValue("name", "NC-17");
    }
}
