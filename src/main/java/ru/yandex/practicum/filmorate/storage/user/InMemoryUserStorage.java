package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.HttpMethod;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> users = new HashMap<>();
    private int idGenerate = 0;

    @Override
    public Integer setId() {
        idGenerate++;
        return this.idGenerate;
    }

    @Override
    public User add(User user) {
        log.info("Добавляем пользователя");

        if (validate(user, HttpMethod.POST)) {
            user.setId(setId());
            users.put(user.getId(), user);
            return user;
        } else throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public User update(User user) {
        log.info("Обновляем данные пользователя");

        if (validate(user, HttpMethod.PUT) & users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public List<User> getAll() {
        log.info("Получаем список всех пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public User delete(User user) {
        log.info("Удаляем пользователя");

        if (validate(user, HttpMethod.DELETE) & users.containsKey(user.getId())) {
            users.remove(user.getId());
            return user;
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public User getUser(Integer id) {
        log.info("Получаем пользователя по id");

        if (id != null & users.containsKey(id)) {
            return users.get(id);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public User addFriend(User user, Integer friendId) {
        log.info("Добавляем пользователя в друзья");

        if (validate(user, HttpMethod.PUT) & users.containsKey(friendId)) {
            if (user.getFriendList() == null) {
                Set<Integer> newFriendList = new HashSet<>();
                newFriendList.add(friendId);
                user.setFriendList(newFriendList);
            } else {
                user.getFriendList().add(friendId);
            }

            return users.get(friendId);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public User deleteFriend(User user, Integer friendId) {
        log.info("Удаляем пользователя из друзей");

        if (validate(user, HttpMethod.DELETE) & users.containsKey(friendId)) {
            user.getFriendList().remove(friendId);
            users.get(friendId).getFriendList().remove(user.getId());
            return users.get(friendId);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public List<User> getAllFriends(User user) {
        log.info("Возвращаем список друзей пользователя");

        if (validate(user, HttpMethod.GET) & user.getFriendList() != null) {
            List<User> friendList = new ArrayList<>();

            for (Integer next: user.getFriendList()) {
                if (users.containsKey(next)){
                    friendList.add(users.get(next));
                }
            }
            return friendList;
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public List<User> getCommonFriend(User user, Integer id) {
        log.info("Возвращаем общий список друзей пользователей");

        if (validate(user, HttpMethod.GET) & user.getFriendList() != null & users.containsKey(id) & users.get(id).getFriendList() != null) {
            List<User> commonFriendList = new ArrayList<>();

            for (Integer userFriend: user.getFriendList()) {
                for (Integer otherUserFriend: users.get(id).getFriendList()) {
                    if (userFriend.equals(otherUserFriend)) {
                        commonFriendList.add(users.get(userFriend));
                    }
                }
            }
            return commonFriendList;
        } else return new ArrayList<>();
    }

    @Override
    public boolean validate(User user, HttpMethod method) {
        if (user.getLogin().indexOf(' ') >= 0) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы", method);
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            return true;
        }

        return true;
    }
}
