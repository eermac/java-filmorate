package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@Data
public class User {
    private int id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
    private Set<Integer> friendList;
}
