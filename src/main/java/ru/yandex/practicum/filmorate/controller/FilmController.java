package ru.yandex.practicum.filmorate.controller;


import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.HttpMethod;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private HashMap<Integer, Film> films = new HashMap<>();
    private int idGenerate = 0;
    private final static LocalDate FIRST_RELEASE_FILM = LocalDate.of(1895, 12, 28);

    public Integer setId(){
        idGenerate++;
        return this.idGenerate;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Добавляем фильм");

        if(validate(film, HttpMethod.POST)) {
            film.setId(setId());
            films.put(film.getId(), film);
        }

        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Обновляем фильм");

        if(validate(film, HttpMethod.PUT) & films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public boolean validate(Film film, HttpMethod method){
            if(film.getName() == null || film.getName().isBlank()){
                throw new ValidationException("Название фильма не может быть пустым", method);
            } else if(film.getDescription() == null || film.getDescription().length() > 200){
                throw new ValidationException("Описание фильма не может быть больше 200 символов", method);
            } else if(film.getReleaseDate() == null || film.getReleaseDate().isBefore(FIRST_RELEASE_FILM)){
                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года", method);
            } else if(film.getDuration() < 1){
                throw new ValidationException("Продолжительность фильма должна быть положительной", method);
            }

        return true;
    }
}

