package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

/**
 * Интерфейс хранилища моделей User. Определяет контракт для работы с моделями User:
 * создание, обновление, получение всех записей и получение по идентификатору.
 * Реализация интерфейса должна обеспечивать уникальность идентификаторов (id) для каждой записи
 * Валидацию данных перед сохранением/обновлением на:
 * 1) Наличие и правильное написание (присутствие символа '@') почты
 * 2) Логин не может быть пустым и содержать пробелы
 * 3) Дата рождения не может быть null.
 * 4) Дата рождения не может быть в будущем.
 * Корректную обработку ошибок
 * Конкретная технология хранения (БД, память и т. п.) определяется реализацией.
 */
public interface UserStorage {

    /**
     * Создаёт новую запись о пользователе в хранилище.
     *
     * @param user объект пользователя для сохранения (не должен быть null)
     * @return User сохранённый объект пользователя (содержит сгенерированное поле id)
     * @throws ValidationException     если user не прошел валидацию
     * @throws InternalServerException если user не удалось сохранить
     */
    public User create(User user);

    /**
     * Обновляет существующую запись о пользователе.
     *
     * @param newUser объект пользователя с обновлёнными данными (не должен быть null,
     *                должен содержать корректный id для поиска записи)
     * @return User обновлённый объект пользователя после сохранения изменений
     * @throws ValidationException     если user не прошел валидацию
     * @throws InternalServerException если не удалось обновить данные
     */
    public User update(User newUser);

    /**
     * Возвращает коллекцию всех пользователей.
     *
     * @return коллекция объектов User (может быть пустой, если записей нет)
     */
    public Collection<User> getAllUsers();

    /**
     * Получает объект User по его уникальному идентификатору.
     *
     * @param id идентификатор пользователя
     * @return User - найденный объект пользователя
     * @throws NotFoundException если пользователь с id не найден
     */
    public User getUser(Long id);
}