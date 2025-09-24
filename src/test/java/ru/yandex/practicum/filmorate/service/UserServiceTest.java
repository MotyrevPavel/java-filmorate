package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(new InMemoryUserStorage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithNullEmail() {
        User user = new User(10L, null, "login", "name",
                LocalDate.of(2025, 7, 26), new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(user));
        Assertions.assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithEmptyEmail() {
        User user = new User(10L, "", "login", "name",
                LocalDate.of(2025, 7, 26), new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(user));
        Assertions.assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithInvalidEmail() {
        User user = new User(10L, "invalidemail", "login", "name",
                LocalDate.of(2025, 7, 26), new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(user));
        Assertions.assertEquals("Электронная почта должна содержать символ @", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithEmptyLogin() {
        User user = new User(10L, "valid@email", "", "name",
                LocalDate.of(2025, 7, 26), new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithNullLogin() {
        User user = new User(10L, "valid@email", null, "name",
                LocalDate.of(2025, 7, 26), new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithGapLogin() {
        User user = new User(10L, "valid@email", "log in", "name",
                LocalDate.of(2025, 7, 26), new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void shouldReturnTrueWhenLoginReplaceEmptyName() {
        User user = new User(10L, "valid@email", "login", "",
                LocalDate.of(2025, 7, 26), new HashSet<>());

        userService.create(user);
        User resultUser = userService.getAllUsers().stream()
                .findFirst().get();

        User userWithReplacedName = new User(resultUser.getId(), "valid@email", "login", "login",
                LocalDate.of(2025, 7, 26), new HashSet<>());


        Assertions.assertEquals(userWithReplacedName, resultUser);
    }

    @Test
    void shouldReturnTrueWhenLoginReplaceNullName() {
        User user = new User(10L, "valid@email", "login", null,
                LocalDate.of(2025, 7, 26), new HashSet<>());

        userService.create(user);
        User resultUser = userService.getAllUsers().stream()
                .findFirst().get();

        User userWithReplacedName = new User(resultUser.getId(), "valid@email", "login", "login",
                LocalDate.of(2025, 7, 26), new HashSet<>());


        Assertions.assertEquals(userWithReplacedName, resultUser);
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithFutureBirthday() {
        User user = new User(10L, "valid@email", "login", "name",
                LocalDate.of(2025, 9, 26), new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(user));
        Assertions.assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithNullBirthday() {
        User user = new User(10L, "valid@email", "login", "name",
                null, new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(user));
        Assertions.assertEquals("Дата рождения не может быть null.", exception.getMessage());
    }

    @Test
    void shouldTReturnTrueWhenSuccessCreateUser() {
        User user = new User(10L, "valid@email", "login", "name",
                LocalDate.of(2025, 7, 26), new HashSet<>());


        User returnUser = userService.create(user);

        Assertions.assertEquals(user, returnUser);
        Assertions.assertTrue(userService.getAllUsers().contains(user));
        Assertions.assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithNullEmail() {
        User user = new User(10L, null, "login", "name",
                LocalDate.of(2025, 7, 26), new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.update(user));
        Assertions.assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithEmptyEmail() {
        User user = new User(10L, "", "login", "name",
                LocalDate.of(2025, 7, 26), new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.update(user));
        Assertions.assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithInvalidEmail() {
        User user = new User(10L, "invalidemail", "login", "name",
                LocalDate.of(2025, 7, 26), new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.update(user));
        Assertions.assertEquals("Электронная почта должна содержать символ @", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithEmptyLogin() {
        User user = new User(10L, "valid@email", "", "name",
                LocalDate.of(2025, 7, 26), new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.update(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithNullLogin() {
        User user = new User(10L, "valid@email", null, "name",
                LocalDate.of(2025, 7, 26), new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.update(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithGapLogin() {
        User user = new User(10L, "valid@email", "log in", "name",
                LocalDate.of(2025, 7, 26), new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void shouldReturnTrueWhenLoginReplaceEmptyNameInUpdateMethod() {
        User user = new User(10L, "valid@email", "login", "",
                LocalDate.of(2025, 7, 26), new HashSet<>());

        userService.create(user);

        Long id = userService.getAllUsers().stream().findFirst().get().getId();
        user.setId(id);

        userService.update(user);

        User userWithReplacedName = new User(id, "valid@email", "login", "login",
                LocalDate.of(2025, 7, 26), new HashSet<>());


        Assertions.assertEquals(userWithReplacedName, user);
    }

    @Test
    void shouldReturnTrueWhenLoginReplaceNullNameInUpdateMethod() {
        User user = new User(10L, "valid@email", "login", null,
                LocalDate.of(2025, 7, 26), new HashSet<>());

        userService.create(user);

        Long id = userService.getAllUsers().stream().findFirst().get().getId();
        user.setId(id);

        userService.update(user);

        User userWithReplacedName = new User(id, "valid@email", "login", "login",
                LocalDate.of(2025, 7, 26), new HashSet<>());

        Assertions.assertEquals(userWithReplacedName, user);
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithFutureBirthday() {
        User user = new User(10L, "valid@email", "login", "name",
                LocalDate.of(2025, 9, 26), new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.update(user));
        Assertions.assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithNullBirthday() {
        User user = new User(10L, "valid@email", "login", "name",
                null, new HashSet<>());

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.update(user));
        Assertions.assertEquals("Дата рождения не может быть null.", exception.getMessage());
    }

    @Test
    void shouldTReturnTrueWhenSuccessUpdateUser() {
        User user = new User(10L, "valid@email", "login", "name",
                LocalDate.of(2025, 7, 26), new HashSet<>());

        userService.create(user);

        Long id = userService.getAllUsers().stream().findFirst().get().getId();

        User newUser = new User(id, "valid@email.ru", "loginName", "name",
                LocalDate.of(2025, 7, 23), new HashSet<>());

        User returnUser = userService.update(newUser);


        Assertions.assertEquals(newUser, returnUser);
        Assertions.assertTrue(userService.getAllUsers().contains(newUser));
        Assertions.assertFalse(userService.getAllUsers().contains(user));
        Assertions.assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void shouldReturnTrueWhenGetAllUsers() {
        User user1 = new User(10L, "user1@email.ru", "user1", "name1",
                LocalDate.of(2025, 4, 5), new HashSet<>());
        User user2 = new User(15L, "user2@email.ru", "user2", "name2",
                LocalDate.of(2025, 5, 10), new HashSet<>());
        User user3 = new User(20L, "user3@email.ru", "user3", "name3",
                LocalDate.of(2025, 6, 15), new HashSet<>());

        userService.create(user1);
        userService.create(user2);
        userService.create(user3);

        Collection<User> users = userService.getAllUsers();

        Assertions.assertEquals(3, users.size());
        Assertions.assertTrue(users.contains(user1));
        Assertions.assertTrue(users.contains(user2));
        Assertions.assertTrue(users.contains(user3));
    }

}