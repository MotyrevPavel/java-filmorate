package ru.yandex.practicum.filmorate.dal.db.h2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.MpaStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository("mpaDbStorage")
public class MpaDbStorage extends BaseDbStorage<Mpa> implements MpaStorage {
    private static final String FIND_BY_ID_QUERY = "SELECT id, name FROM rating WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT id, name FROM rating";

    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Mpa getMpa(Long id) {
        log.info("Получение рейтинга по ID:{}", id);
        Optional<Mpa> optionalMpa = findOne(FIND_BY_ID_QUERY, id);
        if (optionalMpa.isPresent()) {
            Mpa mpa = optionalMpa.get();
            log.info("Получен рейтинг:{}", mpa);
            return mpa;
        } else {
            log.error("Рейтинг с указанным id {} отсутствует", id);
            throw new NotFoundException("Рейтинг с указанным ID отсутствует");
        }
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        log.info("Получение всех рейтингов");
        return findMany(FIND_ALL_QUERY);
    }
}
