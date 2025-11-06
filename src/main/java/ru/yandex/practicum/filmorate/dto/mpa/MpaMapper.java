package ru.yandex.practicum.filmorate.dto.mpa;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.Mpa;

@UtilityClass
public class MpaMapper {
    public static MpaDto mapToMpaDto(Mpa mpa) {
        return new MpaDto(
                mpa.getId(),
                mpa.getName()
        );
    }
}
