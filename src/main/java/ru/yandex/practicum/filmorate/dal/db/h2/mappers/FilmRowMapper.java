package ru.yandex.practicum.filmorate.dal.db.h2.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        Long duration = rs.getLong("duration");
        Set<Long> likes = parseLikesIds(rs.getString("likes_ids"));
        Mpa rating = parseMpa(rs.getLong("rating_id"), rs.getString("rating_name"));
        List<Genre> genres = parseGenre(rs.getString("genre_ids"),
                rs.getString("genre_names"));

        return new Film(id, name, description, releaseDate, duration, likes, rating, genres);
    }

    private Mpa parseMpa(Long ratingId, String ratingName) {
        return new Mpa(ratingId, ratingName);
    }

    private Set<Long> parseLikesIds(String likesIdsStr) {
        Set<Long> likes = new HashSet<>();
        if (likesIdsStr != null && !likesIdsStr.isEmpty()) {
            for (String idStr : likesIdsStr.split(",")) {
                likes.add(Long.valueOf(idStr.trim()));
            }
        }
        return likes;
    }

    private List<Genre> parseGenre(String genreIdsStr, String genreNames) {
        List<Genre> genres = new ArrayList<>();
        if (genreIdsStr != null && !genreIdsStr.isEmpty()) {
            String[] genreIdsArray = genreIdsStr.split(",");
            String[] genreNamesArray = genreNames.split(",");
            for (int i = 0; i < genreIdsArray.length; i++) {
                Long idGenre = Long.valueOf(genreIdsArray[i]);
                String nameGenre = genreNamesArray[i];
                genres.add(new Genre(idGenre, nameGenre));
            }
        }
        return genres;
    }
}
