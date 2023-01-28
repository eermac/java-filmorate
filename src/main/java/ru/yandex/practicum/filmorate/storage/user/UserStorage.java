package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.HttpMethod;

import java.util.List;

public interface UserStorage {
    Integer setId();

    User add(User user);

    User update(User user);

    List<User> getAll();

    User delete(User user);

    User getUser(Integer id);

    User addFriend(User user, Integer id);

    User deleteFriend(User user, Integer id);

    List<User> getAllFriends(User user);

    List<User> getCommonFriend(User user, Integer id);
    boolean validate(User user, HttpMethod method);
}
