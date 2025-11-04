package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenresController {
    private final GenreService genreService;

    @GetMapping
    public Collection<GenreDto> getAllGenres() {
        Collection<GenreDto> genres = genreService.getAllGenres();
        log.info("Получение всех жанров");
        return genres;
    }

    @GetMapping("/{id}")
    public GenreDto getGenre(@PathVariable Long id) {
        log.info("Получение жанра по id: {}", id);
        return genreService.getGenre(id);
    }
}
