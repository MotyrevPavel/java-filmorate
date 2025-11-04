package ru.yandex.practicum.filmorate.dal.db.h2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.util.Validator.validate;

@Slf4j
@Repository("filmDbStorage")
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, " +
            "duration, rating_id) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_LIKES_QUERY = "INSERT INTO likes(film_id, user_id) " +
            "VALUES (?, ?)";
    private static final String INSERT_FILM_GENRES_QUERY = "INSERT INTO film_genres(film_id, genre_id) " +
            "VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
            "duration = ?, rating_id = ? WHERE id = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT f.id, f.name, f.description, f.release_date, f.duration, " +
            "f.rating_id, r.name AS rating_name, STRING_AGG(DISTINCT CAST(l.user_id AS VARCHAR), ',') AS likes_ids, " +
            "STRING_AGG(g.id, ',') AS genre_ids, " +
            "STRING_AGG(g.name, ',') AS genre_names " +
            "FROM films f " +
            "LEFT JOIN likes l ON f.id = l.film_id " +
            "LEFT JOIN rating r ON f.rating_id = r.id " +
            "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
            "LEFT JOIN genre g ON fg.genre_id = g.id " +
            "WHERE f.id = ? " +
            "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.rating_id, rating_name";
    private static final String FIND_ALL_QUERY = "SELECT f.id, f.name, f.description, f.release_date, f.duration, " +
            "f.rating_id, r.name AS rating_name, STRING_AGG(DISTINCT CAST(l.user_id AS VARCHAR), ',') AS likes_ids, " +
            "STRING_AGG(g.id, ',') AS genre_ids, " +
            "STRING_AGG(g.name, ',') AS genre_names " +
            "FROM films f " +
            "LEFT JOIN likes l ON f.id = l.film_id " +
            "LEFT JOIN rating r ON f.rating_id = r.id " +
            "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
            "LEFT JOIN genre g ON fg.genre_id = g.id " +
            "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.rating_id, rating_name";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Film create(Film film) {
        log.info("Создание нового фильма в базе данных:{}", film);
        validate(film);
        log.info("Влидация успешно пройдеа при создании фильма:{}", film);
        Long id = insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null
        );
        log.info("Успешное добавление в таблицу с фильмами фильа:{}", film);
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            Set<Long> genreIds = film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
            insertManyQuery(INSERT_FILM_GENRES_QUERY, id, genreIds);
            log.info("Успешное добавление в таблицу с жанрами фильа:{}", film);
        }
        film.setId(id);
        log.info("Новый фильм успешно добавлен в базу");
        return film;
    }

    @Override
    public Film update(Film film) {
        log.info("Обновление фильма в базе данных:{}", film);
        validate(film);
        log.info("Обновление фильма прошло валидацию:{}", film);
        update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        log.info("В базе обновлен фильм:{}", film);

        jdbc.update("DELETE FROM likes WHERE film_id = ?", film.getId());
        log.info("В таблице с лайками очищены лайки с фильмом:{}", film);
        insertManyQuery(INSERT_LIKES_QUERY, film.getId(), film.getLikes());
        log.info("В базе заполнены таблица с лайками фильма:{}", film);
        jdbc.update("DELETE FROM film_genres WHERE film_id = ?", film.getId());
        log.info("В таблице с жанрами очищены жанры с фильмом:{}", film);
        Set<Long> genresId = film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        insertManyQuery(INSERT_FILM_GENRES_QUERY, film.getId(), genresId);
        log.info("В базе заполнены таблица с жанрами фильма:{}", film);

        log.info("Данные фильма успешно обновлены");
        return film;
    }

    @Override
    public Film getFilm(Long id) {
        log.info("Получение фильма по ID:{}", id);
        Optional<Film> optionalFilm = findOne(FIND_BY_ID_QUERY, id);
        if (optionalFilm.isPresent()) {
            Film film = optionalFilm.get();
            log.info("Получен фильм:{}", film);
            return film;
        } else {
            log.error("Фильм с указанным id отсутствует");
            throw new NotFoundException("Фильм с указанным id отсутствует");
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Получение всех фильмов");
        return findMany(FIND_ALL_QUERY);
    }
}
