package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dal.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public FilmDto create(NewFilmRequest newFilm) {
        log.info("Создание фильма: {}", newFilm);
        Film film = FilmMapper.mapToFilm(newFilm);
        log.info("Получени фильма после маппинга: {}", film);
        Film createdFilm = filmStorage.create(film);
        log.info("Фильм успешно создан: {}", createdFilm);
        return FilmMapper.mapToFilmDto(createdFilm);
    }

    public FilmDto update(UpdateFilmRequest newFilm) {
        log.info("Обновление фильма: {}", newFilm);
        if (newFilm.getId() == null) {
            log.error("Обновление данных фильма без ID");
            throw new ValidationException("Отсутствует ID");
        }
        Film film = filmStorage.getFilm(newFilm.getId());
        log.info("Получен фильм {} из базы данных по ID: {}", film, newFilm.getId());
        Film filmMapped = FilmMapper.updateFilmFields(film, newFilm);
        log.info("Получен объект обновленного фильма: {}", filmMapped);
        Film updatedFilm = filmStorage.update(filmMapped);
        log.info("Фильм успешно обновлен: {}", updatedFilm);
        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    public List<FilmDto> getAllFilms() {
        log.info("Получение списка фильмов");
        List<FilmDto> films = filmStorage.getAllFilms()
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
        log.info("Список фильмов успешно получен");
        return films;
    }

    public FilmDtoForGet getFilm(Long id) {
        log.info("Получение фильма по id: {}", id);
        Film film = filmStorage.getFilm(id);
        log.info("Фильм по id {} успешно получен", id);
        return FilmMapper.mapToFilmGetDto(film);
    }

    public FilmDto addLike(Long id, Long userId) {
        log.info("Попытка добавления Лайка пользователя с id {} фильму с id {}", userId, id);
        Film film = filmStorage.getFilm(id);
        //userStorage.getUser(userId);
        Set<Long> likes = Optional.ofNullable(film.getLikes()).orElse(new HashSet<>());
        likes.add(userId);
        film.setLikes(likes);
        filmStorage.update(film);
        log.info("Лайк пользователя с id {} фильму с id {} успешно добавлен", userId, id);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto deleteLike(Long id, Long userId) {
        log.info("Попытка удаления Лайка пользователя с id {} у фильма с id {}", userId, id);
        Film film = filmStorage.getFilm(id);
        //userStorage.getUser(userId);
        Set<Long> likes = film.getLikes();
        if (likes == null || !likes.contains(userId)) {
            log.error("Попытка удаления несуществующего лайка {} у фильма {}", userId, id);
            throw new NotFoundException("У этого фильма " + id + " нет лайка от пользователя с id " + userId);
        }
        likes.remove(userId);
        film.setLikes(likes);
        filmStorage.update(film);
        log.info("Лайк пользователя с id {} у фильма с id {} успешно удален", userId, id);
        return FilmMapper.mapToFilmDto(film);
    }

    public List<FilmDto> getTopPopularFilms(Long count) {
        log.info("Получение списка топ популярных фильмов в количесте {}", count);
        Collection<Film> films = filmStorage.getAllFilms().stream()
                .filter(film -> film.getLikes() != null)
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit((count))
                .toList();
        log.info("Успешно получен список топ популярных фильмов в количесте {}", films.size());
        log.info("Успешно получен список топ популярных фильмов в количесте {}", films);

        return films.stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }
}