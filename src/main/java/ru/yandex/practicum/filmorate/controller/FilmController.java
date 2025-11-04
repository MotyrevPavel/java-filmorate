package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmDtoForGet;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public FilmDto create(@Valid @RequestBody NewFilmRequest film) {
        log.info("Создание нового фильма: {}", film);
        return filmService.create(film);
    }

    @PutMapping
    public FilmDto update(@Valid @RequestBody UpdateFilmRequest newFilm) {
        log.info("Обновление фильма: {}", newFilm);
        return filmService.update(newFilm);
    }

    @GetMapping
    public Collection<FilmDto> getAllFilms() {
        log.info("Получение списка всех фильмов");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public FilmDtoForGet getFilm(@PathVariable Long id) {
        log.info("Получение фильма с id: {}", id);
        return filmService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public FilmDto addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Добавление лайка от пользователя {} к фильму {}", userId, id);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public FilmDto deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Удаление лайка пользователя {} к фильму {}", userId, id);
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<FilmDto> getTopPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        log.info("Получение ТОП популярных фильмов в количестве {}", count);
        return filmService.getTopPopularFilms(count);
    }
}
