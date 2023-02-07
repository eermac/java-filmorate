package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;

import java.util.HashMap;
import java.util.List;

@Service
public class FilmService {
    private final FilmDbStorage filmDao;

    @Autowired
    public FilmService(FilmDbStorage filmDao) {
        this.filmDao = filmDao;
    }

    public Film add(Film film) {
        return this.filmDao.add(film);
    }

    public Film update(Film film) {
        return this.filmDao.update(film);
    }

    public List<Film> getAll() {
        return this.filmDao.getAll();
    }

    public Film getFilm(Integer id) {
        return this.filmDao.getFilm(id);
    }

    public Integer addLike(Integer id, Integer userId) {
        return this.filmDao.addLike(id, userId);
    }

    public Integer deleteLike(Integer id, Integer userId) {
        return this.filmDao.deleteLike(id, userId);
    }

    public List<Film> getPopularFilmsCount(Integer count) {
        return this.filmDao.getPopularFilmsCount(count);
    }

    public List<Film> getPopularFilms() {
        return this.filmDao.getPopularFilms();
    }

    public Mpa getMpa(Integer Id) {
        return this.filmDao.getMpa(Id);
    }

    public Genres getGenre(Integer Id) {
        return this.filmDao.getGenre(Id);
    }

    public List<Mpa> getAllMpa() {
        return this.filmDao.getAllMpa();
    }

    public List<Genres> getAllGenres() {
        return this.filmDao.getAllGenres();
    }

}
