package ru.yandex.practicum.filmorate.dal.db.h2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class BaseDbStorage<T> {
    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;

    protected Optional<T> findOne(String query, Object... params) {
        try {
            T result = jdbc.queryForObject(query, mapper, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    protected List<T> findMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    protected void update(String query, Object... params) {
        int updateResult = jdbc.update(query, params);
        if (updateResult == 0) {
            log.error("Не удалось обновить данные");
            throw new InternalServerException("Не удалось обновить данные");
        }
    }

    protected Long insert(String query, Object... params) {
        log.info("Вставка по запросу {} с параметрами {}", query, params);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbc.update(conection -> {
                PreparedStatement ps = conection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
                return ps;
            }, keyHolder);


            Long id = keyHolder.getKeyAs(Long.class);
            log.info("Запрос выполнен. Значение ключа: {}", id);

            if (id != null) {
                return id;
            } else {
                log.error("Не удалось сохранить данные");
                throw new InternalServerException("Не удалось сохранить данные");
            }
        } catch (DataIntegrityViolationException ex) {
            log.error("Попытка вставки данных в базу с неверным ID");
            throw new NotFoundException("Попытка вставки данных в базу с неверным ID");
        }
    }

    protected void insertManyQuery(String query, Long firstParam, Set<Long> secondParam) {
        if (secondParam.isEmpty()) return;
        try {
            jdbc.batchUpdate(query, secondParam.stream()
                    .map(secondParamId -> new Object[]{firstParam, secondParamId})
                    .toList()
            );
        } catch (DataAccessException e) {
            log.error("Заполнение таблицы несуществующими данными");
            throw new NotFoundException("Заполнение таблицы несуществующими данными");
        }
    }
}
