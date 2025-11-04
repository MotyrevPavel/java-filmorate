package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.dto.user.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dal.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserDto create(NewUserRequest newUser) {
        log.info("Создание пользователя: {}", newUser);
        User user = UserMapper.mapToUser(newUser);
        log.info("Пользователь после mapper: {}", user);
        User createdUser = userStorage.create(user);
        log.info("Пользователь успешно создан: {}", createdUser);
        return UserMapper.mapToUserDto(createdUser);
    }

    public UserDto update(UpdateUserRequest newUser) {
        log.info("Обновление пользователя: {}", newUser);
        User user = userStorage.getUser(newUser.getId());
        log.info("Получили из базы пользователя для обновления: {}", newUser);
        User userMapped = UserMapper.updateUserFields(user, newUser);
        log.info("Получили пользователя с обновленными полями: {}", userMapped);
        User updateUser = userStorage.update(userMapped);
        log.info("Пользователь успешно обновлен: {}", updateUser);
        return UserMapper.mapToUserDto(updateUser);
    }

    public List<UserDto> getAllUsers() {
        log.info("Получение всех пользователей");
        List<UserDto> users = userStorage.getAllUsers()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
        log.info("Все пользователи получены");
        return users;
    }

    public UserDto getUser(Long id) {
        log.info("Получение пользователя по id: {}", id);
        User user = userStorage.getUser(id);
        log.info("Пользователь успешно получен по id: {}", id);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto addFriend(Long id, Long friendId) {
        log.info("Добавление в друзья пользователю с id {} пользователя с id {}", id, friendId);
        User user = userStorage.getUser(id);
        log.info("Получен пользователь из БД:{}", user);
        Set<Long> friendList = user.getFriends();
        friendList = Optional.ofNullable(friendList).orElse(new HashSet<>());
        friendList.add(friendId);
        user.setFriends(friendList);
        userStorage.update(user);
        log.info("Пользователь с id {} успешно добавлен в друзья пользователю с id {}", friendId, id);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto deleteFriend(Long id, Long friendId) {
        log.info("Удаление из друзей пользователя с id {} пользователя с id {}", id, friendId);
        User user = userStorage.getUser(id);
        log.info("Получен пользлватель {} из БД по ID {}", friendId, id);
        User userFriend = userStorage.getUser(friendId);

        Set<Long> userFriendList = Optional.ofNullable(user.getFriends()).orElse(new HashSet<>());
        userFriendList.remove(friendId);
        user.setFriends(userFriendList);
        userStorage.update(user);

        Set<Long> friendUserFriendList = Optional.ofNullable(userFriend.getFriends()).orElse(new HashSet<>());
        friendUserFriendList.remove(id);
        userFriend.setFriends(friendUserFriendList);

        log.info("Пользователь с id {} успешно удален из друзей пользователя с id {}", friendId, id);

        return UserMapper.mapToUserDto(user);
    }

    public List<UserDto> getUserFriends(Long id) {
        log.info("Получение друзей пользователя с id {}", id);
        User user = userStorage.getUser(id);
        log.info("Получение пользлвателя {} из БД по ID {}", user, id);
        Set<Long> userFriends = Optional.ofNullable(user.getFriends()).orElse(new HashSet<>());
        Collection<User> friendList = userFriends.stream()
                .map(userStorage::getUser)
                .toList();
        log.info("Получена коллекция друзей пользователя с id {}", id);
        return friendList.stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }


    public List<UserDto> getCommonFriends(Long id, Long otherId) {
        log.info("Получение общих друзей пользователей с id {} и id {}", id, otherId);
        User user = userStorage.getUser(id);
        Set<Long> userFriends = Optional.ofNullable(user.getFriends()).orElse(new HashSet<>());
        User otherUser = userStorage.getUser(otherId);
        Set<Long> otherUserFriends = Optional.ofNullable(otherUser.getFriends()).orElse(new HashSet<>());
        Collection<User> commonFriendList = userFriends.stream()
                .filter(otherUserFriends::contains)
                .map(userStorage::getUser)
                .toList();
        log.info("Список общих друзей пользователей с id {} и id {} успешно получен", id, otherId);
        return commonFriendList.stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }
}