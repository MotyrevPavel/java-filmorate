package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

/**
 * Интерфейс хранилища рейтингов MPAA (Mpa).
 * Определяет контракт для работы с сущностями Mpa: получение всех рейтингов
 * и поиск рейтинга по уникальному идентификатору.
 * Реализация интерфейса должна обеспечивать
 * 1) уникальность идентификаторов (id) для каждого рейтинга
 * 2) Корректное хранение и извлечение данных жанров
 * 3) Корректную обработку ошибок
 * Конкретная технология хранения (БД, память и т. п.) определяется реализацией.
 */

public interface MpaStorage {

    /**
     * Возвращает коллекцию всех доступных рейтингов MPAA в системе.
     *
     * @return Collection<Mpa> - не-null. Коллекция может быть пустой,
     * если в системе нет зарегистрированных рейтингов
     * @throws NotFoundException если рейтинг с указанным id не существует в хранилище
     */
    public Collection<Mpa> getAllMpa();

    /**
     * Получает объект Mpa по его уникальному идентификатору.
     *
     * @param id идентификатор рейтинга MPAA
     * @return Mpa - найденный объект
     * @throws NotFoundException если рейтинг с указанным id не существует в хранилище
     */
    public Mpa getMpa(Long id);
}
