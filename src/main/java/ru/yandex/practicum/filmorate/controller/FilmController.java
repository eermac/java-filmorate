package ru.yandex.practicum.filmorate.controller;


import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
public class FilmController {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private HashMap<Integer, Film> films = new HashMap<>();
    private int idGenerate = 10000;

    public Integer setId(){
        idGenerate++;
        return this.idGenerate;
    }

    @PostMapping(value = "/films")
    public Film addFilm(@RequestBody Film film) {
        log.info("Добавляем фильм");

        if(validate(film, HttpMethod.POST)) {
            film.setId(setId());
            films.put(film.getId(), film);
        }

        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        log.info("Обновляем фильм");

        if(validate(film, HttpMethod.PUT) & films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        }

        return film;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        List<Film> filmList = new ArrayList<>();

        for(Film next: films.values()){
            filmList.add(next);
        }

        return filmList;
    }

    public boolean validate(Film film, HttpMethod method){
        try {
            if(film.getName().isEmpty()){
                throw new ValidationException("Название фильма не может быть пустым", method);
            } else if(film.getDescription().length() > 200){
                throw new ValidationException("Описание фильма не может быть больше 200 символов", method);
            } else if(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))){
                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года", method);
            } else if(film.getDuration() < 1 ){
                throw new ValidationException("Продолжительность фильма должна быть положительной", method);
            }
        } catch (ValidationException exception){
            System.out.println(exception.getMessage());
        }

        return true;
    }
}

