package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.util.HttpMethod;
import ru.yandex.practicum.filmorate.util.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film add(@Valid @RequestBody Film film) {
        return this.filmService.add(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        return this.filmService.update(film);
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@RequestBody @PathVariable Integer id) {
        return this.filmService.getFilm(id);
    }

    @GetMapping("/films")
    public List<Film> getAll() {
        return this.filmService.getAll();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Integer addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return this.filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Integer deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return this.filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilmsCount(@RequestParam(required = false) Integer count) {
        if (count == null) return this.filmService.getPopularFilms();
        else return this.filmService.getPopularFilmsCount(count);
    }

    @GetMapping("/mpa/{Id}")
    public Mpa getMpa(@PathVariable Integer Id) {
        return this.filmService.getMpa(Id);
    }

    @GetMapping("/mpa")
    public List<Mpa> getAllMpa() {
        return this.filmService.getAllMpa();
    }

    @GetMapping("/genres")
    public List<Genres> getAllGenres() {
        return this.filmService.getAllGenres();
    }

    @GetMapping("/genres/{Id}")
    public Genres getGenre(@PathVariable Integer Id) {
        return this.filmService.getGenre(Id);
    }

    @ExceptionHandler
    public Map<String, String> handle(final ValidationException e) {
        return Map.of(
                "error", "Ошибка запроса.",
                "errorMessage", e.getMessage()
        );
    }
}

