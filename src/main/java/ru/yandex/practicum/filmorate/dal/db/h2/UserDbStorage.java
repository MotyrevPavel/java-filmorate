package ru.yandex.practicum.filmorate.dal.db.h2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Timestamp;
import java.util.*;

import static ru.yandex.practicum.filmorate.util.Validator.checkName;
import static ru.yandex.practicum.filmorate.util.Validator.validate;

@Slf4j
@Repository("userDbStorage")
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private static final String INSERT_QUERY = "INSERT INTO users(email, login, name, birthday) " +
            "VALUES (?, ?, ?, ?)";
    private static final String INSERT_USER_FRIENDS_QUERY = "INSERT INTO user_friends(user_id, friend_id) " +
            "VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, " +
            "birthday = ? WHERE id = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT u.id, u.email, u.login, u.name, u.birthday, " +
            "STRING_AGG(DISTINCT CAST(uf.friend_id AS VARCHAR), ',') AS friend_ids, " +
            "FROM users u " +
            "LEFT JOIN user_friends uf ON u.id = uf.user_id " +
            "WHERE u.id = ? " +
            "GROUP BY u.id, u.email, u.login, u.name, u.birthday";
    private static final String FIND_ALL_QUERY = "SELECT u.id, u.email, u.login, u.name, u.birthday, " +
            "STRING_AGG(DISTINCT CAST(uf.friend_id AS VARCHAR), ',') AS friend_ids, " +
            "FROM users u " +
            "LEFT JOIN user_friends uf ON u.id = uf.user_id " +
            "GROUP BY u.id, u.email, u.login, u.name, u.birthday";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public User create(User user) {
        log.info("Создание в базе данных пользователя: {}", user);
        validate(user);
        checkName(user);
        log.info("Валидация пройдена при создании: {}", user);
        Timestamp birthday = Timestamp.valueOf(user.getBirthday().atStartOfDay());
        Long id = insert(INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                birthday
        );
        user.setId(id);
        log.info("Новый пользователь успешно добавлен в базу {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        log.info("Обновление в базе данных пользователя: {}", user);
        validate(user);
        checkName(user);
        if (user.getId() == null) {
            log.error("У пользователя отсутствует ID");
            throw new ValidationException("Отсутствует ID");
        }
        log.info("Валидация пройдена при обновлении: {}", user);
        update(UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        jdbc.update("DELETE FROM user_friends WHERE user_id = ?", user.getId());
        log.info("Очищена таблица друзьями пользователя: {}", user);
        insertManyQuery(INSERT_USER_FRIENDS_QUERY, user.getId(), user.getFriends());
        log.info("В таблице обновлены друзья пользователя: {}", user);
        log.info("Данные пользователя успешно обновлены");
        return user;
    }

    @Override
    public User getUser(Long id) {
        log.info("Получение пользователя по id: {}", id);
        Optional<User> optionalUser = findOne(FIND_BY_ID_QUERY, id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            log.info("Пользователь по id получен: {}", user);
            return user;
        } else {
            log.error("Пользователь с указанным id отсутствует");
            throw new NotFoundException("Пользователь с указанным id отсутствует");
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        log.error("Получение всех пользователей");
        return findMany(FIND_ALL_QUERY);
    }
}
