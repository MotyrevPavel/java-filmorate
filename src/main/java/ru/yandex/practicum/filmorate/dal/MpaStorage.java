package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaStorage {

    public Collection<Mpa> getAllMpa();

    public Mpa getMpa(Long id);
}
