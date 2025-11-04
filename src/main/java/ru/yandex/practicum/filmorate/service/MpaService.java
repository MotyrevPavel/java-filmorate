package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaStorage;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.dto.mpa.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    public MpaService(@Qualifier("mpaDbStorage") MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<MpaDto> getAllMpas() {
        log.info("Получение всех рейтингов");
        List<MpaDto> mpas = mpaStorage.getAllMpa()
                .stream()
                .map(MpaMapper::mapToMpaDto)
                .toList();
        log.info("Все жанры получены");
        return mpas;
    }

    public MpaDto getMpa(Long id) {
        log.info("Получение рейтинга по id: {}", id);
        Mpa mpa = mpaStorage.getMpa(id);
        log.info("Рейтинг успешно получен по id: {}", id);
        return MpaMapper.mapToMpaDto(mpa);
    }
}