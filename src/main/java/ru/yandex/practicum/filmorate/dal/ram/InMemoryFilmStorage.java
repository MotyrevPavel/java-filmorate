package ru.yandex.practicum.filmorate.dal.ram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.util.Validator.validate;

@Slf4j
@Repository("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    public Film create(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("У фильма отсутствует название");
            throw new ValidationException("Название не может быть пустым");
        }
        validate(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Новый фильм успешно добавлен в базу");
        return film;
    }

    public Film update(Film newFilm) {
        validate(newFilm);
        if (newFilm.getId() == null) {
            log.error("Обновление данных фильма без ID");
            throw new ValidationException("Отсутствует ID");
        }
        if (!films.containsKey(newFilm.getId())) {
            log.error("Попытка обновления несуществующего фильма");
            throw new NotFoundException("Попытка обновления несуществующего фильма");
        }
        films.put(newFilm.getId(), newFilm);
        log.info("Данные фильма успешно обновлены");
        return newFilm;
    }

    public Film getFilm(Long id) {
        Film film = films.get(id);
        if (film == null) {
            log.error("Попытка получение фильма без ID");
            throw new NotFoundException("Фильм с указанным id отсутствует");
        }
        return film;
    }

    public Collection<Film> getAllFilms() {
        return films.values();
    }

    private Long generateId() {
        long newId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);

        return ++newId;
    }
}
