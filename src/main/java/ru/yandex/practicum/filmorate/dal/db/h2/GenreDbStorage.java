package ru.yandex.practicum.filmorate.dal.db.h2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.GenreStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Slf4j
@Repository("genreDbStorage")
public class GenreDbStorage extends BaseDbStorage<Genre> implements GenreStorage {
    private static final String FIND_BY_ID_QUERY = "SELECT id, name FROM genre WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT id, name FROM genre";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Genre getGenre(Long id) {
        log.info("Получение жанра по ID:{}", id);
        Optional<Genre> optionalGenre = findOne(FIND_BY_ID_QUERY, id);
        if (optionalGenre.isPresent()) {
            Genre genre = optionalGenre.get();
            log.info("Получен жанр:{}", genre);
            return genre;
        } else {
            log.error("Жанр с указанным id {} отсутствует", id);
            throw new NotFoundException("Жанр с указанным отсутствует");
        }
    }

    @Override
    public Collection<Genre> getAllGenres() {
        log.info("Получение всех жанров");
        return findMany(FIND_ALL_QUERY);
    }

}
