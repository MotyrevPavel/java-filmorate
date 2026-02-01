package ru.yandex.practicum.filmorate.dal.db.h2.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        Set<Long> friends = parseLongIds(rs.getString("friend_ids"));

        return new User(id, email, login, name, birthday, friends);
    }

    private Set<Long> parseLongIds(String idsStr) {
        Set<Long> longIds = new HashSet<>();
        if (idsStr != null && !idsStr.isEmpty()) {
            for (String idStr : idsStr.split(",")) {
                longIds.add(Long.valueOf(idStr.trim()));
            }
        }
        return longIds;
    }
}
