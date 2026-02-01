package ru.yandex.practicum.filmorate.dal.db.h2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dal.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate.dal.db.h2")
public class UserDbStorageTests {
    private final UserStorage userStorage;

    public UserDbStorageTests(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Test
    public void testFindUserById() {

        User user = userStorage.getUser(1L);

        assertThat(user).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("email", "alice@example.com")
                .hasFieldOrPropertyWithValue("login", "alice")
                .hasFieldOrPropertyWithValue("name", "Алиса")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1990, 5, 15));
    }

    @Test
    public void testCreateNewUser() {
        User test = new User(null, "test@test.ru", "loginTest", "nameTest",
                LocalDate.of(1990, 5, 15), new HashSet<>());
        User createdUser = userStorage.create(test);
        User receiveUser = userStorage.getUser(createdUser.getId());

        assertThat(receiveUser).isNotNull()
                .hasFieldOrPropertyWithValue("id", createdUser.getId())
                .hasFieldOrPropertyWithValue("email", "test@test.ru")
                .hasFieldOrPropertyWithValue("login", "loginTest")
                .hasFieldOrPropertyWithValue("name", "nameTest")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1990, 5, 15));
    }

    @Test
    public void testUpdateUser() {
        User test = new User(null, "test@test.ru", "loginTest", "nameTest",
                LocalDate.of(1990, 5, 15), new HashSet<>());
        User createdUser = userStorage.create(test);
        User udpateUser = new User(createdUser.getId(), "my.email@email.ru", "login", "name",
                LocalDate.of(1990, 5, 20), new HashSet<>());
        userStorage.update(udpateUser);
        User updatedUser = userStorage.getUser(createdUser.getId());

        assertThat(updatedUser).isNotNull()
                .hasFieldOrPropertyWithValue("id", createdUser.getId())
                .hasFieldOrPropertyWithValue("email", "my.email@email.ru")
                .hasFieldOrPropertyWithValue("login", "login")
                .hasFieldOrPropertyWithValue("name", "name")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1990, 5, 20));
    }

    @Test
    public void testGetAllUsers() {
        Collection<User> users = userStorage.getAllUsers();
        User user1 = users.stream().toList().getFirst();
        User user5 = users.stream().toList().getLast();

        assertThat(users)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5);

        assertThat(user1).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("email", "alice@example.com")
                .hasFieldOrPropertyWithValue("login", "alice")
                .hasFieldOrPropertyWithValue("name", "Алиса")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1990, 5, 15));

        assertThat(user5).isNotNull()
                .hasFieldOrPropertyWithValue("id", 5L)
                .hasFieldOrPropertyWithValue("email", "eve@example.com")
                .hasFieldOrPropertyWithValue("login", "eve")
                .hasFieldOrPropertyWithValue("name", "Ева")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1995, 7, 27));
    }
}
