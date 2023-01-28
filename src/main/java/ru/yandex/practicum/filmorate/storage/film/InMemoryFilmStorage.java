package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.HttpMethod;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> films = new HashMap<>();

    Comparator<Film> userComparator = new Comparator<>() {
        @Override
        public int compare(Film film1, Film film2) {
            if(film1.getCountLike() > (film2.getCountLike())) return 1;
            else return -1;
        }
    };

    Map<Film, Integer> filmPopular = new TreeMap<>(userComparator);
    private int idGenerate = 0;
    private final static LocalDate FIRST_RELEASE_FILM = LocalDate.of(1895, 12, 28);

    @Override
    public Integer setId() {
        idGenerate++;
        return this.idGenerate;
    }

    @Override
    public Film add(Film film) {
        log.info("Добавляем фильм");

        if (validate(film, HttpMethod.POST)) {
            film.setId(setId());
            film.setCountLike(0);
            films.put(film.getId(), film);
            return film;
        } else throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public Film update(Film film) {
        log.info("Обновляем фильм");

        if (validate(film, HttpMethod.PUT) & films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public boolean validate(Film film, HttpMethod method){
        if (film.getReleaseDate().isBefore(FIRST_RELEASE_FILM)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года", method);
        }
        return true;
    }

    public Film getFilm(Integer id) {
        log.info("Получаем фильм по id");

        if (id != null & films.containsKey(id)) {
            return films.get(id);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public Integer addLike(Integer id, Integer userId) {
        log.info("Пользователь ставит лайк фильму");
        Film currentFilm = films.get(id);

        if (currentFilm.getUsersLike() != null ) {
            if (!currentFilm.getUsersLike().contains(userId)) {
                currentFilm.getUsersLike().add(userId);
                currentFilm.setCountLike(currentFilm.getCountLike() + 1);
            }
        } else {
            Set<Integer> newList = new HashSet<>();
            newList.add(userId);
            currentFilm.setUsersLike(newList);
            currentFilm.setCountLike(currentFilm.getCountLike() + 1);
        }
        return userId;
    }

    public Integer deleteLike(Integer id, Integer userId) {
        log.info("Пользователь убирает лайк фильму");
        Film currentFilm = films.get(id);

        if (currentFilm.getUsersLike() != null ) {
            if (currentFilm.getUsersLike().contains(userId)) {
                currentFilm.setCountLike(currentFilm.getCountLike() - 1);
                currentFilm.getUsersLike().remove(userId);
                return userId;
            } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public List<Film> getPopularFilmsCount(Integer count) {
        log.info("Получаем список из " + count + " самых популярных фильмов");
        List<Film> filmList = new ArrayList<>();

        filmPopular = new TreeMap<>(userComparator);

        for (Film next: films.values()) {
            filmPopular.put(next, next.getCountLike());
        }

        int i = 0;

        for (Film next: filmPopular.keySet()) {
            if (i == count) {
                break;
            } else {
                if (next.getUsersLike() != null) {
                    filmList.add(next);
                    i++;
                }
            }
        }
        return filmList;
    }

    public List<Film> getPopularFilms() {
        log.info("Получаем список самых популярных фильмов");
        List<Film> filmList = new ArrayList<>();

        filmPopular = new TreeMap<>(userComparator);

        for (Film next: films.values()) {
            filmPopular.put(next, next.getCountLike());
        }

        for (Film next: filmPopular.keySet()) {
                filmList.add(next);
        }
        return filmList;
    }
}
