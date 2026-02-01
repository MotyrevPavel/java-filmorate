package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreStorage;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.genre.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Slf4j
@Service
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreService(@Qualifier("genreDbStorage") GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<GenreDto> getAllGenres() {
        log.info("Получение всех жанров");
        List<GenreDto> genres = genreStorage.getAllGenres()
                .stream()
                .map(GenreMapper::mapToGenreDto)
                .toList();
        log.info("Все жанры получены");
        return genres;
    }

    public GenreDto getGenre(Long id) {
        log.info("Получение жанра по id: {}", id);
        Genre genre = genreStorage.getGenre(id);
        log.info("Жанр успешно получен по id: {}", id);
        return GenreMapper.mapToGenreDto(genre);
    }
}