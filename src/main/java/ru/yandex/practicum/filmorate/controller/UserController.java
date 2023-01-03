package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.HttpMethod;
import ru.yandex.practicum.filmorate.util.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private int idGenerate = 0;

    public Integer setId(){
        idGenerate++;
        return this.idGenerate;
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        log.info("Добавляем пользователя");

        if(validate(user, HttpMethod.POST)){
            user.setId(setId());
            users.put(user.getId(), user);
        }

        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Обновляем данные пользователя");

        if(validate(user, HttpMethod.PUT) & users.containsKey(user.getId())){
            users.put(user.getId(), user);
        } else throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        return user;
    }

    @GetMapping
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public boolean validate(User user, HttpMethod method){
            if(user.getEmail() == null || user.getEmail().isBlank() || (user.getEmail().indexOf('@') < 0)){
                throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @", method);
            } else if(user.getLogin().isBlank() || (user.getLogin().indexOf(' ') >= 0)){
                throw new ValidationException("Логин не может быть пустым и содержать пробелы", method);
            } else if(user.getBirthday().isAfter(LocalDate.now())){
                throw new ValidationException("Дата рождения не может быть в будущем.", method);
            } else if(user.getName() == null || user.getName().isBlank()){
                user.setName(user.getLogin());
                return true;
            }

        return true;
    }
}
