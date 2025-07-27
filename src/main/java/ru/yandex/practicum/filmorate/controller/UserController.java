package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validate(user);
        checkName(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Новый пользовтель успешно создан");
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        validate(newUser);
        checkName(newUser);
        if (newUser.getId() == null) {
            log.error("У пользователя отсутствует ID");
            throw new ValidationException("Отсутствует ID");
        }
        if (!users.containsKey(newUser.getId())) {
            log.error("Попытка обновления несуществующего пользователя");
            throw new ValidationException("Попытка обновления несуществующего пользователя");
        }
        users.put(newUser.getId(), newUser);
        log.info("Данные пользователя успешно обновлены");
        return newUser;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("У пользователя пустая электронная почта");
            throw new ValidationException("Электронная почта не может быть пустой");
        }
        if (!user.getEmail().contains("@")) {
            log.error("У пользователя невалидная электронная почта");
            throw new ValidationException("Электронная почта должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("У пользователя логин пустой или содержит пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday() == null) {
            log.error("У пользователя дата рождения null");
            throw new ValidationException("Дата рождения не может быть null.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("У пользователя неверная дата рождения");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
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
