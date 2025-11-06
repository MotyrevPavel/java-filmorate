package ru.yandex.practicum.filmorate.dto.user;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;

@UtilityClass
public final class UserMapper {
    public static User mapToUser(NewUserRequest request) {
        return new User(
                request.getId(),
                request.getEmail(),
                request.getLogin(),
                request.getName(),
                request.getBirthday(),
                new HashSet<>()
        );
    }

    public static UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getFriends()
        );
    }

    public static User updateUserFields(User user, UpdateUserRequest request) {
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setName(request.getName());
        user.setBirthday(request.getBirthday());
        return user;
    }
}
