package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Data
public class Film {
    private int id;
    @NotBlank
    @NotNull
    private String name;
    @Size(max = 200)
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @Positive
    private long duration;
}
