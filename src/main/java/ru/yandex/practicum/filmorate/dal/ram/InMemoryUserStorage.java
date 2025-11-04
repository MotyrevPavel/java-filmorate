package ru.yandex.practicum.filmorate.dal.ram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.util.Validator.checkName;
import static ru.yandex.practicum.filmorate.util.Validator.validate;

@Slf4j
@Repository("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public User create(User user) {
        validate(user);
        checkName(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Новый пользовтель успешно создан");
        return user;
    }

    public User update(User newUser) {
        validate(newUser);
        checkName(newUser);
        if (newUser.getId() == null) {
            log.error("У пользователя отсутствует ID");
            throw new ValidationException("Отсутствует ID");
        }
        if (!users.containsKey(newUser.getId())) {
            log.error("Попытка обновления несуществующего пользователя");
            throw new NotFoundException("Попытка обновления несуществующего пользователя");
        }
        users.put(newUser.getId(), newUser);
        log.info("Данные пользователя успешно обновлены");
        return newUser;
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public User getUser(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с указанным id отсутствует");
        }
        return user;
    }

    private Long generateId() {
        long newId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);

        return ++newId;
    }
}
