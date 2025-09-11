package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.util.Validator.validate;

@Slf4j
@Getter
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validate(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Новый фильм успешно добавлен в базу");
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        validate(newFilm);
        if (newFilm.getId() == null) {
            log.error("Обновление данных фильма без ID");
            throw new ValidationException("Отсутствует ID");
        }
        if (!films.containsKey(newFilm.getId())) {
            log.error("Попытка обновления несуществующего фильма");
            throw new ValidationException("Попытка обновления несуществующего фильма");
        }
        films.put(newFilm.getId(), newFilm);
        log.info("Данные фильма успешно обновлены");
        return newFilm;
    }

    @GetMapping
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
