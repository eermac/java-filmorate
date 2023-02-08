package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@Slf4j
public class FilmComponentController {

    private final FilmService filmService;

    @Autowired
    public FilmComponentController(FilmService filmService) {
        this.filmService = filmService;
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
}
