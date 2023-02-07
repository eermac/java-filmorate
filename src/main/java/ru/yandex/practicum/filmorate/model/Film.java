package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Builder
@Data
public class Film {
    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    @NotNull
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    private long duration;
    @JsonIgnore
    private int countLike;
    @JsonIgnore
    private Set<Integer> usersLike;
    private Mpa mpa;
    private Set<Genres> genres;
}
