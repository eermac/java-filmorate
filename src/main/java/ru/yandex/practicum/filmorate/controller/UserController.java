package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.util.HttpMethod;
import ru.yandex.practicum.filmorate.util.ValidationException;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        return this.userService.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return this.userService.update(user);
    }

    @GetMapping
    public List<User> getAll() {
        return this.userService.getAll();
    }

    @DeleteMapping
    public User delete(@Valid @RequestBody User user) {
        return this.userService.delete(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        return this.userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        return this.userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriend(@PathVariable Integer id) {
        return this.userService.getAllFriend(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriend(@PathVariable Integer id, @PathVariable Integer otherId) {
        return this.userService.getCommonFriend(id, otherId);
    }

    @GetMapping("/{id}")
    public User getUser(@RequestBody @PathVariable Integer id) {
        return this.userService.getUser(id);
    }

    @ExceptionHandler
    public Map<String, String> handle(final ValidationException e) {
        return Map.of(
                "error", "Ошибка запроса.",
                "errorMessage", e.getMessage()
        );
    }
}
