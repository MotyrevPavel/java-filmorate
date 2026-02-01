package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

/**
 * Интерфейс хранилища моделей Film. Определяет контракт для работы с моделями Film:
 * создание, обновление, получение всех записей и получение по идентификатору.
 * Реализация интерфейса должна обеспечивать уникальность идентификаторов (id) для каждой записи
 * Валидацию данных перед сохранением/обновлением на:
 * 1) наличие названия фильма
 * 2) Описание должно быть не более 200 символов
 * 3) Дата релиза не ранее 28 декабря 1895 года
 * 4) Продолжительность фильма больше 0
 * Корректную обработку ошибок
 * Конкретная технология хранения (БД, память и т. п.) определяется реализацией.
 */


public interface FilmStorage {
    /**
     * Создаёт новую запись о фильме в хранилище.
     *
     * @param film объект фильма для сохранения (не должен быть null)
     * @return Film сохранённый объект фильма (содержит сгенерированное поле id)
     * @throws ValidationException     если film не прошел валидацию
     * @throws InternalServerException если film не удалось сохранить
     */
    public Film create(Film film);

    /**
     * Обновляет существующую запись о фильме.
     *
     * @param newFilm объект фильма с обновлёнными данными (не должен быть null,
     *                должен содержать корректный id для поиска записи)
     * @return Film обновлённый объект фильма после сохранения изменений
     * @throws ValidationException     если film не прошел валидацию
     * @throws InternalServerException если не удалось обновить данные
     */
    public Film update(Film newFilm);

    /**
     * Возвращает коллекцию всех фильмов.
     *
     * @return коллекция объектов Film (может быть пустой, если записей нет)
     */
    public Collection<Film> getAllFilms();

    /**
     * Получает объект Film по его уникальному идентификатору.
     *
     * @param id идентификатор фильма
     * @return Film - найденный объект фильма
     * @throws NotFoundException если фильм с id не найден
     */
    public Film getFilm(Long id);
}
