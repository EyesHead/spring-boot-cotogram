package ru.practicum.yandex.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.models.user.User;
import ru.practicum.yandex.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.update(user);
    }
}
