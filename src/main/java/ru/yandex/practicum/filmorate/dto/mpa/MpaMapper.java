package ru.yandex.practicum.filmorate.dto.mpa;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Mpa;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MpaMapper {
    public static MpaDto mapToMpaDto(Mpa mpa) {
        return new MpaDto(
                mpa.getId(),
                mpa.getName()
        );
    }
}
