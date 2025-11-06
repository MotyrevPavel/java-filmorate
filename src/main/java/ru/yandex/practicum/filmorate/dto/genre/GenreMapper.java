package ru.yandex.practicum.filmorate.dto.genre;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.Genre;

@UtilityClass
public class GenreMapper {
    public static GenreDto mapToGenreDto(Genre genre) {
        return new GenreDto(
                genre.getId(),
                genre.getName()
        );
    }
}
