package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dal.ram.InMemoryUserStorage;

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
        NewUserRequest user = new NewUserRequest(10L, null, "login", "name",
                LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(user));
        Assertions.assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithEmptyEmail() {
        NewUserRequest user = new NewUserRequest(10L, "", "login", "name",
                LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(user));
        Assertions.assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithInvalidEmail() {
        NewUserRequest user = new NewUserRequest(10L, "invalidemail", "login", "name",
                LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(user));
        Assertions.assertEquals("Электронная почта должна содержать символ @", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithEmptyLogin() {
        NewUserRequest user = new NewUserRequest(10L, "valid@email", "", "name",
                LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithNullLogin() {
        NewUserRequest user = new NewUserRequest(10L, "valid@email", null, "name",
                LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithGapLogin() {
        NewUserRequest user = new NewUserRequest(10L, "valid@email", "log in", "name",
                LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void shouldReturnTrueWhenLoginReplaceEmptyName() {
        NewUserRequest user = new NewUserRequest(10L, "valid@email", "login", "",
                LocalDate.of(2025, 7, 26));

        userService.create(user);
        UserDto resultUserDto = userService.getAllUsers().stream()
                .findFirst().get();

        User resultUser = new User(resultUserDto.getId(), resultUserDto.getEmail(), resultUserDto.getLogin(),
                resultUserDto.getName(), resultUserDto.getBirthday(), resultUserDto.getFriends());

        User userWithReplacedName = new User(resultUser.getId(), "valid@email", "login", "login",
                LocalDate.of(2025, 7, 26), new HashSet<>());


        Assertions.assertEquals(userWithReplacedName, resultUser);
    }

    @Test
    void shouldReturnTrueWhenLoginReplaceNullName() {
        NewUserRequest user = new NewUserRequest(10L, "valid@email", "login", null,
                LocalDate.of(2025, 7, 26));

        userService.create(user);
        UserDto resultUserDto = userService.getAllUsers().stream()
                .findFirst().get();

        User resultUser = new User(resultUserDto.getId(), resultUserDto.getEmail(), resultUserDto.getLogin(),
                resultUserDto.getName(), resultUserDto.getBirthday(), resultUserDto.getFriends());

        User userWithReplacedName = new User(resultUser.getId(), "valid@email", "login", "login",
                LocalDate.of(2025, 7, 26), new HashSet<>());


        Assertions.assertEquals(userWithReplacedName, resultUser);
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithFutureBirthday() {
        NewUserRequest user = new NewUserRequest(10L, "valid@email", "login", "name",
                LocalDate.of(2030, 9, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(user));
        Assertions.assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateUserWithNullBirthday() {
        NewUserRequest user = new NewUserRequest(10L, "valid@email", "login", "name",
                null);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(user));
        Assertions.assertEquals("Дата рождения не может быть null.", exception.getMessage());
    }

    @Test
    void shouldTReturnTrueWhenSuccessCreateUser() {
        NewUserRequest user = new NewUserRequest(10L, "valid@email", "login", "name",
                LocalDate.of(2025, 7, 26));
        UserDto resultUserDto = userService.create(user);
        UserDto resultUser = new UserDto(resultUserDto.getId(), "valid@email", "login", "name",
                LocalDate.of(2025, 7, 26), new HashSet<>());

        UserDto userDto = new UserDto(user.getId(), user.getEmail(), user.getLogin(), user.getName(),
                user.getBirthday(), new HashSet<>());

        Assertions.assertEquals(resultUser, resultUserDto);
        Assertions.assertTrue(userService.getAllUsers().contains(resultUser));
        Assertions.assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithNullEmail() {
        NewUserRequest user = new NewUserRequest(10L, "valid@email", "login", null,
                LocalDate.of(2025, 7, 26));

        UserDto userDto = userService.create(user);

        UpdateUserRequest newUser = new UpdateUserRequest();
        newUser.setId(userDto.getId());
        newUser.setEmail(null);
        newUser.setLogin("login");
        newUser.setName("name");
        newUser.setBirthday(LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.update(newUser));
        Assertions.assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithEmptyEmail() {
        NewUserRequest user = new NewUserRequest(10L, "valid@email", "login", null,
                LocalDate.of(2025, 7, 26));

        UserDto userDto = userService.create(user);

        UpdateUserRequest newUser = new UpdateUserRequest();
        newUser.setId(userDto.getId());
        newUser.setEmail("");
        newUser.setLogin("login");
        newUser.setName("name");
        newUser.setBirthday(LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.update(newUser));
        Assertions.assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithInvalidEmail() {
        NewUserRequest user = new NewUserRequest(10L, "valid@email", "login", null,
                LocalDate.of(2025, 7, 26));

        UserDto userDto = userService.create(user);

        UpdateUserRequest newUser = new UpdateUserRequest();
        newUser.setId(userDto.getId());
        newUser.setEmail("invalidemail");
        newUser.setLogin("login");
        newUser.setName("name");
        newUser.setBirthday(LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.update(newUser));
        Assertions.assertEquals("Электронная почта должна содержать символ @", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithEmptyLogin() {
        NewUserRequest user = new NewUserRequest(10L, "valid@email", "login", null,
                LocalDate.of(2025, 7, 26));

        UserDto userDto = userService.create(user);

        UpdateUserRequest newUser = new UpdateUserRequest();
        newUser.setId(userDto.getId());
        newUser.setEmail("valid@email");
        newUser.setLogin("");
        newUser.setName("name");
        newUser.setBirthday(LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.update(newUser));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithNullLogin() {
        NewUserRequest user = new NewUserRequest(10L, "valid@email", "login", null,
                LocalDate.of(2025, 7, 26));

        UserDto userDto = userService.create(user);

        UpdateUserRequest newUser = new UpdateUserRequest();
        newUser.setId(userDto.getId());
        newUser.setEmail("valid@email");
        newUser.setLogin(null);
        newUser.setName("name");
        newUser.setBirthday(LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.update(newUser));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithGapLogin() {
        NewUserRequest user = new NewUserRequest(10L, "valid@email", "log in", "name",
                LocalDate.of(2025, 7, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void shouldReturnTrueWhenLoginReplaceEmptyNameInUpdateMethod() {
        NewUserRequest user = new NewUserRequest(10L, "valid@email", "login", "",
                LocalDate.of(2025, 7, 26));

        userService.create(user);

        Long id = userService.getAllUsers().stream().findFirst().get().getId();
        UpdateUserRequest userRequest = new UpdateUserRequest();
        userRequest.setId(id);
        userRequest.setEmail("valid@email");
        userRequest.setLogin("login");
        userRequest.setName("");
        userRequest.setBirthday(LocalDate.of(2025, 7, 26));

        userService.update(userRequest);

        UserDto userDto = userService.getUser(id);
        NewUserRequest updatedUser = new NewUserRequest(userDto.getId(), userDto.getEmail(), userDto.getLogin(),
                userDto.getName(), userDto.getBirthday());

        NewUserRequest userWithReplacedName = new NewUserRequest(id, "valid@email", "login", "login",
                LocalDate.of(2025, 7, 26));

        Assertions.assertEquals(userWithReplacedName, updatedUser);
    }

    @Test
    void shouldReturnTrueWhenLoginReplaceNullNameInUpdateMethod() {
        NewUserRequest user = new NewUserRequest(10L, "valid@email", "login", null,
                LocalDate.of(2025, 7, 26));

        userService.create(user);

        Long id = userService.getAllUsers().stream().findFirst().get().getId();
        UpdateUserRequest userRequest = new UpdateUserRequest();
        userRequest.setId(id);
        userRequest.setEmail("valid@email");
        userRequest.setLogin("login");
        userRequest.setName("");
        userRequest.setBirthday(LocalDate.of(2025, 7, 26));

        userService.update(userRequest);

        UserDto userDto = userService.getUser(id);
        NewUserRequest updatedUser = new NewUserRequest(userDto.getId(), userDto.getEmail(), userDto.getLogin(),
                userDto.getName(), userDto.getBirthday());

        NewUserRequest userWithReplacedName = new NewUserRequest(id, "valid@email", "login", "login",
                LocalDate.of(2025, 7, 26));

        Assertions.assertEquals(userWithReplacedName, updatedUser);
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithFutureBirthday() {
        NewUserRequest user = new NewUserRequest(10L, "valid@email", "login", null,
                LocalDate.of(2025, 7, 26));

        UserDto userDto = userService.create(user);

        UpdateUserRequest userRequest = new UpdateUserRequest();
        userRequest.setId(userDto.getId());
        userRequest.setEmail("valid@email");
        userRequest.setLogin("login");
        userRequest.setName("name");
        userRequest.setBirthday(LocalDate.of(2030, 9, 26));

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.update(userRequest));
        Assertions.assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithNullBirthday() {
        NewUserRequest user = new NewUserRequest(10L, "valid@email", "login", null,
                LocalDate.of(2025, 7, 26));

        UserDto userDto = userService.create(user);

        UpdateUserRequest userRequest = new UpdateUserRequest();
        userRequest.setId(userDto.getId());
        userRequest.setEmail("valid@email");
        userRequest.setLogin("login");
        userRequest.setName("name");
        userRequest.setBirthday(null);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.update(userRequest));
        Assertions.assertEquals("Дата рождения не может быть null.", exception.getMessage());
    }

    @Test
    void shouldTReturnTrueWhenSuccessUpdateUser() {
        NewUserRequest user = new NewUserRequest(10L, "valid@email", "login", "name",
                LocalDate.of(2025, 7, 26));

        UserDto userDto = new UserDto(10L, "valid@email", "login", "name",
                LocalDate.of(2025, 7, 26), new HashSet<>());

        userService.create(user);

        Long id = userService.getAllUsers().stream().findFirst().get().getId();

        UpdateUserRequest newUser = new UpdateUserRequest();
        newUser.setId(id);
        newUser.setEmail("valid@email.ru");
        newUser.setLogin("loginName");
        newUser.setName("name");
        newUser.setBirthday(LocalDate.of(2025, 7, 23));

        UserDto returnUserDto = userService.update(newUser);

        UpdateUserRequest updatedUser = new UpdateUserRequest();
        updatedUser.setId(returnUserDto.getId());
        updatedUser.setEmail(returnUserDto.getEmail());
        updatedUser.setLogin(returnUserDto.getLogin());
        updatedUser.setName(returnUserDto.getName());
        updatedUser.setBirthday(returnUserDto.getBirthday());


        Assertions.assertEquals(newUser, updatedUser);
        Assertions.assertTrue(userService.getAllUsers().contains(returnUserDto));
        Assertions.assertFalse(userService.getAllUsers().contains(userDto));
        Assertions.assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void shouldReturnTrueWhenGetAllUsers() {
        NewUserRequest user1 = new NewUserRequest(10L, "user1@email.ru", "user1", "name1",
                LocalDate.of(2025, 4, 5));
        NewUserRequest user2 = new NewUserRequest(15L, "user2@email.ru", "user2", "name2",
                LocalDate.of(2025, 5, 10));
        NewUserRequest user3 = new NewUserRequest(20L, "user3@email.ru", "user3", "name3",
                LocalDate.of(2025, 6, 15));

        UserDto userDto1 = userService.create(user1);
        UserDto userDto2 = userService.create(user2);
        UserDto userDto3 = userService.create(user3);

        UserDto user4 = new UserDto(userDto1.getId(), "user1@email.ru", "user1", "name1",
                LocalDate.of(2025, 4, 5), new HashSet<>());
        UserDto user5 = new UserDto(userDto2.getId(), "user2@email.ru", "user2", "name2",
                LocalDate.of(2025, 5, 10), new HashSet<>());
        UserDto user6 = new UserDto(userDto3.getId(), "user3@email.ru", "user3", "name3",
                LocalDate.of(2025, 6, 15), new HashSet<>());


        Collection<UserDto> users = userService.getAllUsers();

        Assertions.assertEquals(3, users.size());
        Assertions.assertTrue(users.contains(user4));
        Assertions.assertTrue(users.contains(user5));
        Assertions.assertTrue(users.contains(user6));
    }

}