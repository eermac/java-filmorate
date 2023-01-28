package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        return this.userStorage.add(user);
    }

    public User update(User user) {
        return this.userStorage.update(user);
    }

    public List<User> getAll() {
        return this.userStorage.getAll();
    }

    public User delete(User user) {
        return this.userStorage.delete(user);
    }

    public User addFriend(Integer id, Integer friendId) {
        userStorage.addFriend(userStorage.getUser(friendId), id);
        return userStorage.addFriend(userStorage.getUser(id), friendId);
    }

    public User deleteFriend(Integer id, Integer friendId) {
        return userStorage.deleteFriend(userStorage.getUser(id), friendId);
    }

    public List<User> getAllFriend(Integer id) {
        return userStorage.getAllFriends(userStorage.getUser(id));
    }

    public List<User> getCommonFriend(Integer id, Integer otherId) {
        return userStorage.getCommonFriend(userStorage.getUser(id), otherId);
    }

    public User getUser(Integer id) {
        return userStorage.getUser(id);
    }

}
