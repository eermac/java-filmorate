package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.HttpMethod;
import ru.yandex.practicum.filmorate.util.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();
    private int idGenerate = 0;
    private final static LocalDate FIRST_RELEASE_FILM = LocalDate.of(1895, 12, 28);

    public Integer setId(){
        idGenerate++;
        return this.idGenerate;
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        log.info("Добавляем фильм");

        if(validate(film, HttpMethod.POST)) {
            film.setId(setId());
            films.put(film.getId(), film);
        }

        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновляем фильм");

        if(validate(film, HttpMethod.PUT) & films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        return film;
    }

    @GetMapping
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    public boolean validate(Film film, HttpMethod method){
        if(film.getReleaseDate().isBefore(FIRST_RELEASE_FILM)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года", method);
        }

        return true;
    }
}

