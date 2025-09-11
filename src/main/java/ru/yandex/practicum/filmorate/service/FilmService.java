package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film create(Film film) {
        log.info("Создание фильма: {}", film);
        Film resultFilm = filmStorage.create(film);
        log.info("Фильм успешно создан: {}", resultFilm);
        return resultFilm;
    }

    public Film update(Film newFilm) {
        log.info("Обновление фильма: {}", newFilm);
        Film resultFilm = filmStorage.update(newFilm);
        log.info("Фильм успешно обновлен: {}", resultFilm);
        return resultFilm;
    }

    public Collection<Film> getAllFilms() {
        log.info("Получение списка фильмов");
        Collection<Film> films = filmStorage.getAllFilms();
        log.info("Список фильмов успешно получен");
        return films;
    }

    public Film getFilm(Long id) {
        log.info("Получение фильма по id: {}", id);
        Film film = filmStorage.getFilm(id);
        log.info("Фильм по id {} успешно получен", id);
        return film;
    }

    public Film addLike(Long id, Long userId) {
        log.info("Попытка добавления Лайка пользователя с id {} фильму с id {}", userId, id);
        Film film = filmStorage.getFilm(id);
        userStorage.getUser(userId);
        Set<Long> likes = film.getLikes();
        likes = Optional.ofNullable(likes).orElse(new HashSet<>());
        likes.add(userId);
        film.setLikes(likes);
        log.info("Лайк пользователя с id {} фильму с id {} успешно добавлен", userId, id);
        return film;
    }

    public Film deleteLike(Long id, Long userId) {
        log.info("Попытка удаления Лайка пользователя с id {} у фильма с id {}", userId, id);
        Film film = filmStorage.getFilm(id);
        userStorage.getUser(userId);
        Set<Long> likes = film.getLikes();
        if (likes == null || !likes.contains(userId)) {
            log.error("Попытка удаления несуществующего лайка {} у фильма {}", userId, id);
            throw new NotFoundException("У этого фильма " + id + " нет лайка от пользователя с id " + userId);
        }
        likes.remove(userId);
        film.setLikes(likes);
        log.info("Лайк пользователя с id {} у фильма с id {} успешно удален", userId, id);
        return film;
    }

    public Collection<Film> getTopPopularFilms(Long count) {
        log.info("Получение списка топ популярных фильмов в количесте {}", count);
        Collection<Film> films = filmStorage.getAllFilms().stream()
                .filter(film -> film.getLikes() != null)
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit((count))
                .toList();
        log.info("Успешно получен список топ популярных фильмов в количесте {}", films.size());
        return films;
    }
}