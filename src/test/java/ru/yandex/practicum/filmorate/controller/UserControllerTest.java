package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithNullEmail() {
        User user = new User(10L, null, "login", "name",
                LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userController.create(user));
        Assertions.assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithEmptyEmail() {
        User user = new User(10L, "", "login", "name",
                LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userController.create(user));
        Assertions.assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithInvalidEmail() {
        User user = new User(10L, "invalidemail", "login", "name",
                LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userController.create(user));
        Assertions.assertEquals("Электронная почта должна содержать символ @", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithEmptyLogin() {
        User user = new User(10L, "valid@email", "", "name",
                LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userController.create(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithNullLogin() {
        User user = new User(10L, "valid@email", null, "name",
                LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userController.create(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithGapLogin() {
        User user = new User(10L, "valid@email", "log in", "name",
                LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userController.create(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void shouldReturnTrueWhenLoginReplaceEmptyName() {
        User user = new User(10L, "valid@email", "login", "",
                LocalDate.of(2025, 7, 26));

        userController.create(user);
        User resultUser = userController.getUsers().values().stream()
                .findFirst().get();

        User userWithReplacedName = new User(resultUser.getId(), "valid@email", "login", "login",
                LocalDate.of(2025, 7, 26));



        Assertions.assertEquals(userWithReplacedName, resultUser);
    }

    @Test
    void shouldReturnTrueWhenLoginReplaceNullName() {
        User user = new User(10L, "valid@email", "login", null,
                LocalDate.of(2025, 7, 26));

        userController.create(user);
        User resultUser = userController.getUsers().values().stream()
                .findFirst().get();

        User userWithReplacedName = new User(resultUser.getId(), "valid@email", "login", "login",
                LocalDate.of(2025, 7, 26));



        Assertions.assertEquals(userWithReplacedName, resultUser);
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithFutureBirthday() {
        User user = new User(10L, "valid@email", "login", "name",
                LocalDate.of(2025, 9, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userController.create(user));
        Assertions.assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithNullBirthday() {
        User user = new User(10L, "valid@email", "login", "name",
                null);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userController.create(user));
        Assertions.assertEquals("Дата рождения не может быть null.", exception.getMessage());
    }

    @Test
    void shouldTReturnTrueWhenSuccessCreateUser() {
        User user = new User(10L, "valid@email", "login", "name",
                LocalDate.of(2025, 7, 26));


        User returnUser = userController.create(user);

        Assertions.assertEquals(user, returnUser);
        Assertions.assertTrue(userController.getAllUsers().contains(user));
        Assertions.assertEquals(1, userController.getUsers().size());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithNullEmail() {
        User user = new User(10L, null, "login", "name",
                LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userController.update(user));
        Assertions.assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithEmptyEmail() {
        User user = new User(10L, "", "login", "name",
                LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userController.update(user));
        Assertions.assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithInvalidEmail() {
        User user = new User(10L, "invalidemail", "login", "name",
                LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userController.update(user));
        Assertions.assertEquals("Электронная почта должна содержать символ @", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithEmptyLogin() {
        User user = new User(10L, "valid@email", "", "name",
                LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userController.update(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithNullLogin() {
        User user = new User(10L, "valid@email", null, "name",
                LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userController.update(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithGapLogin() {
        User user = new User(10L, "valid@email", "log in", "name",
                LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userController.create(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void shouldReturnTrueWhenLoginReplaceEmptyNameInUpdateMethod() {
        User user = new User(10L, "valid@email", "login", "",
                LocalDate.of(2025, 7, 26));

        userController.update(user);
        User resultUser = userController.getUsers().values().stream()
                .findFirst().get();

        User userWithReplacedName = new User(resultUser.getId(), "valid@email", "login", "login",
                LocalDate.of(2025, 7, 26));



        Assertions.assertEquals(userWithReplacedName, resultUser);
    }

    @Test
    void shouldReturnTrueWhenLoginReplaceNullNameInUpdateMethod() {
        User user = new User(10L, "valid@email", "login", null,
                LocalDate.of(2025, 7, 26));

        userController.update(user);
        User resultUser = userController.getUsers().values().stream()
                .findFirst().get();

        User userWithReplacedName = new User(resultUser.getId(), "valid@email", "login", "login",
                LocalDate.of(2025, 7, 26));



        Assertions.assertEquals(userWithReplacedName, resultUser);
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithFutureBirthday() {
        User user = new User(10L, "valid@email", "login", "name",
                LocalDate.of(2025, 9, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userController.update(user));
        Assertions.assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithNullBirthday() {
        User user = new User(10L, "valid@email", "login", "name",
                null);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userController.update(user));
        Assertions.assertEquals("Дата рождения не может быть null.", exception.getMessage());
    }

    @Test
    void shouldTReturnTrueWhenSuccessUpdateUser() {
        User user = new User(10L, "valid@email", "login", "name",
                LocalDate.of(2025, 7, 26));

        userController.create(user);

        Long id = userController.getUsers().values().stream().findFirst().get().getId();

        User newUser = new User(id, "valid@email.ru", "loginName", "name",
                LocalDate.of(2025, 7, 23));

        User returnUser = userController.update(newUser);


        Assertions.assertEquals(newUser, returnUser);
        Assertions.assertTrue(userController.getAllUsers().contains(newUser));
        Assertions.assertFalse(userController.getAllUsers().contains(user));
        Assertions.assertEquals(1, userController.getUsers().size());
    }

    @Test
    void shouldReturnTrueWhenGetAllUsers() {
        User user1 = new User(10L, "user1@email.ru", "user1", "name1",
                LocalDate.of(2025, 4, 5));
        User user2 = new User(15L, "user2@email.ru", "user2", "name2",
                LocalDate.of(2025, 5, 10));
        User user3 = new User(20L, "user3@email.ru", "user3", "name3",
                LocalDate.of(2025, 6, 15));

        userController.create(user1);
        userController.create(user2);
        userController.create(user3);

        Collection<User> users = userController.getAllUsers();

        Assertions.assertEquals(3, users.size());
        Assertions.assertTrue(users.contains(user1));
        Assertions.assertTrue(users.contains(user2));
        Assertions.assertTrue(users.contains(user3));
    }

}