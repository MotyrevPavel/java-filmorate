package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody NewUserRequest user) {
        log.info("Создание нового пользователя: {}", user);
        return userService.create(user);
    }

    @PutMapping
    public UserDto update(@Valid @RequestBody UpdateUserRequest newUser) {
        log.info("Обновление пользователя: {}", newUser);
        return userService.update(newUser);
    }

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        log.info("Получение всех пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        log.info("Получение пользователя по id: {}", id);
        return userService.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public UserDto addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Добавление пользователю с id {} в друзья пользователя с id {}", id, friendId);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public UserDto deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Удаление из друзей у пользователя с id {} пользователя с id {}", id, friendId);
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<UserDto> getUserFriends(@PathVariable Long id) {
        log.info("Получений всех друзей пользователя с id {}", id);
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<UserDto> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Получениe общих друзей пользователя с id: {} и пользователя с id: {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
