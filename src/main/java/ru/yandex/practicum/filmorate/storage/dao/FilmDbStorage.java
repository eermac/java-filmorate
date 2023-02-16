package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.util.HttpMethod;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private int idGenerate = 0;
    private int idGenerateGroup = 0;
    private int idGenerateLike = 0;
    private final static LocalDate FIRST_RELEASE_FILM = LocalDate.of(1895, 12, 28);

    private Comparator<Genres> userComparator = new Comparator<>() {
        @Override
        public int compare(Genres genre, Genres genre2) {
            if(genre.getId() > (genre2.getId())) return 1;
            else return -1;
        }
    };

    public FilmDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }
    @Override
    public Integer setId() {
        idGenerate++;
        return this.idGenerate;
    }

    public Integer setGroupId() {
        idGenerateGroup++;
        return this.idGenerateGroup;
    }

    public Integer setGenerateLike() {
        idGenerateLike++;
        return this.idGenerateLike;
    }

    @Override
    public Film add(Film film) {
        if (validate(film, HttpMethod.POST)) {
            String sqlQuery = "insert into FILMS(FILM_ID, NAME, DESCRIPTION, RELEASEDATE, DURATION, COUNTLIKE, RESTRICTION_ID) " +
                    "values (?, ?, ?, ?, ?, ?, ?)";
            film.setId(setId());

            if (film.getGenres() != null) {
                String sqlQuery2 = "insert into GROUP_FILMS(GROUP_FILMS_ID, FILM_ID, GENRE_ID) " +
                             "values (?, ?, ?)";

                List<Genres> genres = new ArrayList<>();
                for(Genres next: film.getGenres()){
                    genres.add(next);
                }

                jdbcTemplate.batchUpdate(sqlQuery2, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement pStmt, int j) throws SQLException {
                        Genres genre = genres.get(j);
                        pStmt.setInt(1, setGroupId());
                        pStmt.setInt(2, film.getId());
                        pStmt.setInt(3, genre.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genres.size();
                    }
                });
            }

            jdbcTemplate.update(sqlQuery,
                    film.getId(),
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    0,
                    film.getMpa().getId());

            return film;
        } else throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public Film update(Film film) {
        SqlRowSet checkFilm = jdbcTemplate.queryForRowSet("select * from FILMS where FILM_ID = ?", film.getId());

        String sqlQuery3 = "delete from GROUP_FILMS where film_id = ? ";
        jdbcTemplate.update(sqlQuery3,
                film.getId());

        if(checkFilm.next()) {
            String sqlQuery = "update FILMS set " +
                    "NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, RESTRICTION_ID = ? " +
                    "where FILM_ID = ?";

            if(film.getGenres() == null || film.getGenres().isEmpty()){
                film.setGenres(new HashSet<>());
            } else {
                Set<Genres> newGenres = new TreeSet<>(userComparator);

                for(Genres next: film.getGenres()){
                    newGenres.add(next);
                }

                film.setGenres(newGenres);
                String sqlQuery2 = "insert into GROUP_FILMS(GROUP_FILMS_ID, FILM_ID, GENRE_ID) " +
                        "values (?, ?, ?)";

                List<Genres> genres = new ArrayList<>();
                for(Genres next: film.getGenres()){
                    genres.add(next);
                }

                jdbcTemplate.batchUpdate(sqlQuery2, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement pStmt, int j) throws SQLException {
                        Genres genre = genres.get(j);
                        pStmt.setInt(1, setGroupId());
                        pStmt.setInt(2, film.getId());
                        pStmt.setInt(3, genre.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genres.size();
                    }
                });
            }

            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());

            return getFilm(film.getId());
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public List<Film> getAll() {
        List<Film> filmList = new ArrayList<>();
        String queryAllFilms = "select FILM_ID from FILMS";
        List<Integer> filmId = jdbcTemplate.queryForList(queryAllFilms, Integer.class);

        for(Integer next: filmId){
            filmList.add(getFilm(next));
        }
        return filmList;
    }

    @Override
    public Film getFilm(Integer id) {
        SqlRowSet checkFilm = jdbcTemplate.queryForRowSet("select * from FILMS where FILMS.FILM_ID = ?  ", id);
        SqlRowSet sqlGenre = jdbcTemplate.queryForRowSet("select DISTINCT GENRES.NAME, GENRES.GENRE_ID " +
                                                                "from GROUP_FILMS " +
                                                                "inner join GENRES on GROUP_FILMS.GENRE_ID = GENRES.GENRE_ID " +
                                                                "where GROUP_FILMS.FILM_ID = ?", id);

        Set<Genres> genres = new TreeSet<>(userComparator);

        while(sqlGenre.next()){
            Genres genre = new Genres(sqlGenre.getInt("GENRE_ID"), sqlGenre.getString("NAME"));
            genres.add(genre);
        }

        if(checkFilm.next()) {
            String sql = "select * " +
                    "from FILMS F " +
                    "inner join MPA on MPA.RESTRICTION_ID=F.RESTRICTION_ID " +
                    "where F.FILM_ID = ?";

            Film film = jdbcTemplate.queryForObject(sql, this::makeFilm, id);
            film.setGenres(genres);

            return film;
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    private Film makeFilm(ResultSet rs, int id) throws SQLException {
        Integer filmId = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("releasedate").toLocalDate();
        Integer duration = rs.getInt("duration");
        Integer countLike = rs.getInt("countlike");
        Integer restrictionId = rs.getInt("restriction_id");
        String mpaName = rs.getString("MPA.name");

        Mpa mpa = new Mpa(restrictionId, mpaName);

        return new Film(filmId, name, description, releaseDate, duration, countLike, new HashSet<>(), mpa, new HashSet<>());
    }

    @Override
    public Integer addLike(Integer id, Integer userId) {
        SqlRowSet checkFilm = jdbcTemplate.queryForRowSet("select * from FILMS where FILM_ID = ?", id);

        if(checkFilm.next()) {

            SqlRowSet checkFilm2 = jdbcTemplate.queryForRowSet("select * from FILMS inner join USERSLIKE U on FILMS.FILM_ID = U.FILM_ID where FILMS.FILM_ID = ? and U.USER_ID = ?", id, userId);

            if(checkFilm2.next()) {
                return userId;
            } else {
                String sqlQuery = "insert into USERSLIKE(LIKE_ID, FILM_ID, USER_ID) VALUES (?, ?, ?)";
                String sqlQueryFilm = "update FILMS set COUNTLIKE = COUNTLIKE + 1 where FILM_ID = ?";
                jdbcTemplate.update(sqlQueryFilm, id);
                jdbcTemplate.update(sqlQuery, setGenerateLike(), id, userId);
                return userId;
            }
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public Integer deleteLike(Integer id, Integer userId) {
        SqlRowSet checkFilm = jdbcTemplate.queryForRowSet("select * from FILMS where FILM_ID = ?", id);

        if(checkFilm.next()) {
            SqlRowSet query = jdbcTemplate.queryForRowSet("select like_id from USERSLIKE where film_id = ? and user_id = ?", id, userId);

            if(query.next()) {
                String sqlQueryDelete = "delete from USERSLIKE where LIKE_ID = ?";
                jdbcTemplate.update(sqlQueryDelete, query.next());

                String sqlQueryFilm = "update FILMS set COUNTLIKE = COUNTLIKE - 1 where FILM_ID = ?";
                jdbcTemplate.update(sqlQueryFilm, id);
                return userId;
            } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public List<Film> getPopularFilmsCount(Integer count) {
        SqlRowSet queryAllFilms = jdbcTemplate.queryForRowSet("select FILM_ID from FILMS inner join MPA on MPA.RESTRICTION_ID=FILMS.RESTRICTION_ID order by FILMS.COUNTLIKE desc");
        List<Film> filmList = new ArrayList<>();

        for(int i = 0; i < count; i++) {
            queryAllFilms.next();
            filmList.add(getFilm(queryAllFilms.getInt("FILM_ID")));
        }

        return filmList;
    }

    @Override
    public List<Film> getPopularFilms() {
        SqlRowSet queryAllFilms = jdbcTemplate.queryForRowSet("select * from FILMS inner join MPA on MPA.RESTRICTION_ID=FILMS.RESTRICTION_ID order by FILMS.COUNTLIKE desc");
        List<Film> filmList = new ArrayList<>();

        while(queryAllFilms.next()){
            filmList.add(getFilm(queryAllFilms.getInt("FILM_ID")));
        }

        return filmList;
    }

    public Mpa getMpa(Integer Id) {
        SqlRowSet checkFilm = jdbcTemplate.queryForRowSet("select * from MPA where RESTRICTION_ID = ?", Id);

        if(checkFilm.next()) {
            String sql = "select * from MPA where RESTRICTION_ID = ?";
            return jdbcTemplate.queryForObject(sql, this::makeMpa, Id);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public List<Mpa> getAllMpa() {
        String sql = "select * from MPA";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeAllMpa(rs));
    }

    private Mpa makeMpa(ResultSet rs, int Id) throws SQLException {
        Integer restriction_id = rs.getInt("restriction_id");
        String name = rs.getString("name");
        return new Mpa(restriction_id, name);
    }

    private Mpa makeAllMpa(ResultSet rs) throws SQLException {
        Integer restriction_id = rs.getInt("restriction_id");
        String name = rs.getString("name");
        return new Mpa(restriction_id, name);
    }

    public Genres getGenre(Integer Id) {
        SqlRowSet checkFilm = jdbcTemplate.queryForRowSet("select * from GENRES where GENRE_ID = ?", Id);

        if(checkFilm.next()) {
            String sql = "select * from GENRES where GENRE_ID = ?";
            return jdbcTemplate.queryForObject(sql, this::makeGenres, Id);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    private Genres makeGenres(ResultSet rs, int Id) throws SQLException {
        Integer genre_id = rs.getInt("genre_id");
        String name = rs.getString("name");
        return new Genres(genre_id, name);
    }

    public List<Genres> getAllGenres() {
        String sql = "select * from GENRES";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeAllGenres(rs));
    }

    private Genres makeAllGenres(ResultSet rs) throws SQLException {
        Integer genre_id = rs.getInt("GENRE_ID");
        String name = rs.getString("name");
        return new Genres(genre_id, name);
    }

    @Override
    public boolean validate(Film film, HttpMethod method) {
        if (film.getReleaseDate().isBefore(FIRST_RELEASE_FILM)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года", method);
        }
        return true;
    }
}
