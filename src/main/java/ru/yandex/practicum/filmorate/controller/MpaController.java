package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public Collection<MpaDto> getAllMpas() {
        Collection<MpaDto> mpas = mpaService.getAllMpas();
        log.info("Получение всех рейтингов");
        return mpas;
    }

    @GetMapping("/{id}")
    public MpaDto getMpa(@PathVariable Long id) {
        log.info("Получение рейтинга по id: {}", id);
        return mpaService.getMpa(id);
    }
}
