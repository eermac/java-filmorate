package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private HashMap<Integer, User> users = new HashMap<>();

    @PostMapping(value = "/user")
    public User addUser(@RequestBody User user) {
        log.info("Добавляем пользователя");

        if(validate(user)){
            users.put(user.getId(), user);
        }

        return user;
    }

    @PutMapping(value = "/user")
    public User updateFilm(@RequestBody User user) {
        log.info("Обновляем данные пользователя");

        if(validate(user)){
            users.put(user.getId(), user);
        }

        return user;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        List<User> userList = new ArrayList<>();

        for(User next: users.values()){
            userList.add(next);
        }

        return userList;
    }

    public boolean validate(User user){
        try {
            if(user.getEmail().isEmpty() | (user.getEmail().indexOf('@') < 0)){
                throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
            } else if(user.getLogin().isEmpty() | (user.getLogin().indexOf(' ') >= 0)){
                throw new ValidationException("Логин не может быть пустым и содержать пробелы");
            } else if(user.getBirthday().isAfter(LocalDate.now())){
                throw new ValidationException("Дата рождения не может быть в будущем.");
            } else if(user.getName().isEmpty()){
                user.setName(user.getLogin());
                return true;
            }
        } catch (ValidationException exception){
            System.out.println(exception.getMessage());
            return false;
        }

        return true;
    }
}
