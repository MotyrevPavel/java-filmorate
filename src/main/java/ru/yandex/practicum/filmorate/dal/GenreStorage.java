package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.Collection;

/**
 * Интерфейс хранилища жанров (Genre).
 * Определяет контракт для работы с сущностями Genre: получение всех жанров
 * и поиск жанра по уникальному идентификатору.
 * Реализация интерфейса должна обеспечивать
 * 1) уникальность идентификаторов (id) для каждого жанра
 * 2) Корректное хранение и извлечение данных жанров
 * 3) Корректную обработку ошибок
 * Конкретная технология хранения (БД, память и т. п.) определяется реализацией.
 */

public interface GenreStorage {

    /**
     * Возвращает коллекцию всех доступных жанров в системе.
     *
     * @return Collection<Genre> - не-null. Коллекция может быть пустой,
     * если в системе нет зарегистрированных жанров
     */
    public Collection<Genre> getAllGenres();

    /**
     * Получает объект Genre по его уникальному идентификатору.
     *
     * @param id идентификатор жанра
     * @return Genre - найденный объект
     * @throws NotFoundException если жанр с указанным id не существует в хранилище
     */
    public Genre getGenre(Long id);
}
