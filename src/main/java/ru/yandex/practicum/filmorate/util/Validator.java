package ru.yandex.practicum.filmorate.util;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

/**
 * Класс для валидации объектов Film и User.
 * Содержит методы проверки корректности данных перед сохранением в систему.
 */

@Slf4j
public class Validator {

    /**
     * Максимальная длина описания фильма.
     */
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    /**
     * Самая ранняя возможная дата
     * выхода фильма(дата первого публичного кинопоказа).
     */
    private static final LocalDate START_DATE_RELEASE = LocalDate.of(1895, 12, 28);

    /**
     * Валидирует объект Film .
     *
     * @param film объект фильма для проверки
     * @throws ValidationException если данные фильма не соответствуют требованиям
     */
    public static void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("У фильма отсутствует название");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.error("У фильма описание больше 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(START_DATE_RELEASE)) {
            log.error("У фильма дата раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.error("У фильма продолжительность 0 или отрицательная");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
    }

    /**
     * Валидирует объект User .
     *
     * @param user объект пользователя для проверки
     * @throws ValidationException если данные пользователя не соответствуют требованиям
     */
    public static void validate(User user) {
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

    /**
     * Устанавливает имя пользователя, если оно не задано.
     * В качестве имени используется логин пользователя.
     *
     * @param user объект пользователя
     */

    public static void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}