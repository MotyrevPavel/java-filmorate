# java-filmorate
Template repository for Filmorate project.
## Моя диаграмма
### ER-диаграмма
![ER-диаграмма](java-filmorate/images/ERdiagram.svg)

Описание схемы и примеры запросов
Основные сущности
* Пользователи
* Фильмы
* Друзья
* Запросы в друзья

### Примеры SQL-запросов

#### Получение списка друзей пользователя
```sql
SELECT f.friend_id 
FROM friends as f
WHERE f.user_id = :userId;
```

#### Поиск общих друзей
```sql
SELECT f1.friend_id 
FROM friends f1
JOIN friends f2 ON f1.friend_id = f2.friend_id
WHERE f1.user_id = :userId1
AND f2.user_id = :userId2;
```

#### Добавление нового друга
```sql
INSERT INTO friends (user_id, friend_id) 
VALUES (:userId, :friendId);
```

#### Получение информации о пользователе
```sql
SELECT * FROM users 
WHERE id = :userId;
```

### Основные операции приложения
* Управление профилями пользователей
* Работа с друзьями
* Обработка запросов в друзья
* Поиск пользователей
* Добавление лайков фильмам
* Получение ТОП фильмов по количеству лайков
```