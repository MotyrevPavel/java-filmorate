package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@RestController
@RequestMapping("/films")
public class FilmController {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate START_DATE_RELEASE = LocalDate.of(1895, 12, 28);

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

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("У фильма отсутствует название");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.error("У фильма описание больше 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(START_DATE_RELEASE)) {
            log.error("У фильма дата раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.error("У фильма продолжительность 0 или отрицательная");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
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
