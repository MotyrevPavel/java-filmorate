package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Film create(Film film);

    public Film update(Film newFilm);

    public Collection<Film> getAllFilms();

    public Film getFilm(Long id);
}
