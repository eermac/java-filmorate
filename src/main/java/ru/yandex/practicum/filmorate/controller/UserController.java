package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.HttpMethod;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private HashMap<Integer, User> users = new HashMap<>();
    private int idGenerate = 0;

    public Integer setId(){
        idGenerate++;
        return this.idGenerate;
    }

    @PostMapping(value = "/users")
    public User addUser(@RequestBody User user) {
        log.info("Добавляем пользователя");

        if(validate(user, HttpMethod.POST)){
            user.setId(setId());
            users.put(user.getId(), user);
        }

        return user;
    }

    @PutMapping(value = "/users")
    public User updateFilm(@RequestBody User user) {
        log.info("Обновляем данные пользователя");

        if(validate(user, HttpMethod.PUT) & users.containsKey(user.getId())){
            users.put(user.getId(), user);
        } else throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

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

    public boolean validate(User user, HttpMethod method){

        try {
            if(user.getEmail().isEmpty() | (user.getEmail().indexOf('@') < 0)){
                throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @", method);
            } else if(user.getLogin().isEmpty() | (user.getLogin().indexOf(' ') >= 0)){
                throw new ValidationException("Логин не может быть пустым и содержать пробелы", method);
            } else if(user.getBirthday().isAfter(LocalDate.now())){
                throw new ValidationException("Дата рождения не может быть в будущем.", method);
            } else if(user.getName() == null){
                System.out.println("test");
                user.setName(user.getLogin());
                return true;
            }
        } catch (ValidationException exception){
            System.out.println(exception.getMessage());
        }

        return true;
    }
}
