package ru.practicum.yandex.contoller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.models.user.User;

//TODO
@RequestMapping("/users")
public class UserController {
    @GetMapping
    public String getUsers() {
        //TODO
    }

    @PostMapping
    public User postUser(@RequestBody User user) {
        //TODO
    }

    @PutMapping
    public User putUser(@RequestBody User user) {
        //TODO
    }

}
