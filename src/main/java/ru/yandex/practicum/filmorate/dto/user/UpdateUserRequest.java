package ru.yandex.practicum.filmorate.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UpdateUserRequest {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}