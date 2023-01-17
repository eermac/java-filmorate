package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.HttpMethod;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.util.List;

public interface FilmStorage {
    Integer setId();

    Film add(Film film);

    Film update(Film film);

    List<Film> getAll();

    public Film getFilm(Integer id);

    public Integer addLike(Integer id, Integer userId);

    public Integer deleteLike(Integer id, Integer userId);

    public List<Film> getPopularFilmsCount(Integer count);

    public List<Film> getPopularFilms();

    boolean validate(Film film, HttpMethod method);
}
