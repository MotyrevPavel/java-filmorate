package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserStorage userStorage;

    public User create(User user) {
        log.info("Создание пользователя: {}", user);
        User createUser = userStorage.create(user);
        log.info("Пользователь успешно создан: {}", createUser);
        return createUser;
    }

    public User update(User newUser) {
        log.info("Обновление пользователя: {}", newUser);
        User updateUser = userStorage.update(newUser);
        log.info("Пользователь успешно обновлен: {}", updateUser);
        return updateUser;
    }

    public Collection<User> getAllUsers() {
        log.info("Получение всех пользователей");
        Collection<User> users = userStorage.getAllUsers();
        log.info("Все пользователи получены");
        return users;
    }

    public User getUser(Long id) {
        log.info("Получение пользователя по id: {}", id);
        User user = userStorage.getUser(id);
        log.info("Пользователь успешно получен по id: {}", id);
        return user;
    }

    public User addFriend(Long id, Long friendId) {
        log.info("Добавление в друзья пользователю с id {} пользователя с id {}", id, friendId);

        User user = userStorage.getUser(id);
        User userFriend = userStorage.getUser(friendId);

        Set<Long> newFriendList = user.getFriends();
        newFriendList = Optional.ofNullable(newFriendList).orElse(new HashSet<>());
        newFriendList.add(friendId);
        user.setFriends(newFriendList);

        Set<Long> newUserFriendList = userFriend.getFriends();
        newUserFriendList = Optional.ofNullable(newUserFriendList).orElse(new HashSet<>());
        newUserFriendList.add(id);
        userFriend.setFriends(newUserFriendList);

        log.info("Пользователь с id {} успешно добавлен в друзья пользователю с id {}", friendId, id);

        return user;
    }

    public User deleteFriend(Long id, Long friendId) {
        log.info("Удаление из друзей пользователя с id {} пользователя с id {}", id, friendId);

        User user = userStorage.getUser(id);
        User userFriend = userStorage.getUser(friendId);

        Set<Long> userFriendList = Optional.ofNullable(user.getFriends()).orElse(new HashSet<>());
        userFriendList.remove(friendId);
        user.setFriends(userFriendList);

        Set<Long> friendUserFriendList = Optional.ofNullable(userFriend.getFriends()).orElse(new HashSet<>());
        friendUserFriendList.remove(id);
        userFriend.setFriends(friendUserFriendList);

        log.info("Пользователь с id {} успешно удален из друзей пользователя с id {}", friendId, id);

        return user;
    }

    public Collection<User> getUserFriends(Long id) {
        log.info("Получение друзей пользователя с id {}", id);

        User user = userStorage.getUser(id);
        Set<Long> userFriends = Optional.ofNullable(user.getFriends()).orElse(new HashSet<>());

        Collection<User> friendList = userFriends.stream()
                .map(userStorage::getUser)
                .toList();

        log.info("Получена коллекция друзей пользователя с id {}", id);

        return friendList;
    }


    public Collection<User> getCommonFriends(Long id, Long otherId) {
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

        return commonFriendList;
    }
}
