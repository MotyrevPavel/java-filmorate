package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage {

    public Collection<Genre> getAllGenres();

    public Genre getGenre(Long id);
}
