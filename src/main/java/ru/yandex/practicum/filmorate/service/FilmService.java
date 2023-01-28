package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film add(Film film) {
        return this.filmStorage.add(film);
    }

    public Film update(Film film) {
        return this.filmStorage.update(film);
    }

    public List<Film> getAll() {
        return this.filmStorage.getAll();
    }

    public Film getFilm(Integer id) {
        return this.filmStorage.getFilm(id);
    }

    public Integer addLike(Integer id, Integer userId) {
        return this.filmStorage.addLike(id, userId);
    }

    public Integer deleteLike(Integer id, Integer userId) {
        return this.filmStorage.deleteLike(id, userId);
    }

    public List<Film> getPopularFilmsCount(Integer count) {
        return this.filmStorage.getPopularFilmsCount(count);
    }

    public List<Film> getPopularFilms() {
        return this.filmStorage.getPopularFilms();
    }

}
