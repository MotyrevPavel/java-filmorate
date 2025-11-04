package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    public User create(User user);

    public User update(User newUser);

    public Collection<User> getAllUsers();

    public User getUser(Long id);
}