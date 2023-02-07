package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.util.*;

@Service
public class UserService {
    private final UserDbStorage userDao;

    @Autowired
    public UserService(UserDbStorage userDao) {
        this.userDao = userDao;
    }

    public User add(User user) {
        return this.userDao.add(user);
    }

    public User update(User user) {
        return this.userDao.update(user);
    }

    public List<User> getAll() {
        return this.userDao.getAll();
    }

    public User delete(User user) {
        return this.userDao.delete(user);
    }

    public User addFriend(Integer id, Integer friendId) {
        return userDao.addFriend(userDao.getUser(id), friendId);
    }

    public User deleteFriend(Integer id, Integer friendId) {
        return userDao.deleteFriend(userDao.getUser(id), friendId);
    }

    public List<User> getAllFriend(Integer id) {
        return userDao.getAllFriends(userDao.getUser(id));
    }

    public List<User> getCommonFriend(Integer id, Integer otherId) {
        return userDao.getCommonFriend(userDao.getUser(id), otherId);
    }

    public User getUser(Integer id) {
        return userDao.getUser(id);
    }

}
