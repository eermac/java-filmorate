package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.util.HttpMethod;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private int idGenerate = 0;

    public UserDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }
    @Override
    public Integer setId() {
        idGenerate++;
        return this.idGenerate;
    }

    @Override
    public User add(User user) {
        if(validate(user, HttpMethod.PUT)){
            String sqlQuery = "insert into USERS(USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY) " +
                    "values (?, ?, ?, ?, ?)";
            user.setId(setId());

            jdbcTemplate.update(sqlQuery,
                    user.getId(),
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday());

            return user;
        } else throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public User update(User user) {
        if(validate(user, HttpMethod.PUT)){
            SqlRowSet checkUser = jdbcTemplate.queryForRowSet("select * from USERS where USER_ID = ?", user.getId());

            if(checkUser.next()) {
                String sqlQuery = "update users set " +
                        "EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                        "where USER_ID = ?";

                jdbcTemplate.update(sqlQuery,
                        user.getEmail(),
                        user.getLogin(),
                        user.getName(),
                        user.getBirthday(),
                        user.getId());

                return user;
            } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public List<User> getAll() {
        String sql = "select * from users";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUsers(rs));
    }

    private User makeUsers(ResultSet rs) throws SQLException {
        Integer userId = rs.getInt("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return new User(userId, email, login, name, birthday, new HashSet<>(), false);
    }

    @Override
    public User delete(User user) {
        return null;
    }

    @Override
    public User getUser(Integer id) {
        SqlRowSet checkUser = jdbcTemplate.queryForRowSet("select * from USERS where USER_ID = ?", id);

        if(checkUser.next()) {
            String sql = "select * from USERS where USER_ID = ?";

            return jdbcTemplate.queryForObject(sql, this::makeUser, id);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    private User makeUser(ResultSet rs, int id) throws SQLException {
        Integer userId = rs.getInt("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return new User(userId, email, login, name, birthday, new HashSet<>(), false);
    }

    @Override
    public User addFriend(User user, Integer id) {
        if(validate(user, HttpMethod.PUT)){
            SqlRowSet checkUser = jdbcTemplate.queryForRowSet("select * from USERS where USER_ID = ?", id);

            if(checkUser.next()) {
                String sqlQuery = "insert into FRIENDSHIP(USER_ID, FRIEND_ID, STATUS) " +
                        "values (?, ?, ?)";

                jdbcTemplate.update(sqlQuery,
                        user.getId(),
                        id,
                        true);

                return user;
            } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public User deleteFriend(User user, Integer id) {
        if(validate(user, HttpMethod.DELETE)){
            String sqlQuery = "delete from FRIENDSHIP where USER_ID = ? and FRIEND_ID = ?";

            jdbcTemplate.update(sqlQuery,
                    user.getId(),
                    id);
            return user;
        } else throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<User> getAllFriends(User user) {
        String sql = "select U.* from USERS U inner join (select FRIEND_ID from FRIENDSHIP where USER_ID = ?) F on F.FRIEND_ID = USER_ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUsers(rs), user.getId());
    }

    @Override
    public List<User> getCommonFriend(User user, Integer id) {
        String sql = "select u.* " +
                "from (" +
                "select f.FRIEND_ID u1 " +
                "from USERS u " +
                "inner join FRIENDSHIP F on u.USER_ID = F.USER_ID " +
                "where F.USER_ID = ?) user1 " +
                "inner join (" +
                "select f.FRIEND_ID u2 " +
                "from USERS u " +
                "inner join FRIENDSHIP F on u.USER_ID = F.USER_ID " +
                "where F.USER_ID = ?) user2 " +
                "on user1.u1 = user2.u2 " +
                "inner join USERS u on u.USER_ID = user1.u1";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUsers(rs), user.getId(), id);
    }

    @Override
    public boolean validate(User user, HttpMethod method) {
        if (user == null || user.getLogin().indexOf(' ') >= 0) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы", method);
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            return true;
        }

        return true;
    }
}
