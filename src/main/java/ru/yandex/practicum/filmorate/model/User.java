package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class User {
    private int id;
    @Email
    private String email;
    @NotBlank
    @NotNull
    private String login;
    private String name;
    @NotNull
    private LocalDate birthday;
}
