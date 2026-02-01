package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Genre.
 */

@Data
@AllArgsConstructor
public class Genre {
    private Long id;
    private String name;
}
