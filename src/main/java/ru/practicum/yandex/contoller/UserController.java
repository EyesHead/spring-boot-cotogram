package ru.practicum.yandex.contoller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.exceptions.NotFoundException;
import ru.practicum.yandex.models.User;
import ru.practicum.yandex.service.UserService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable @NonNull Long id) {
        Optional<User> foundUserOpt = userService.getUser(id);
        return foundUserOpt.orElseThrow(() -> new NotFoundException("User with ID=" + id + "doesn't exist"));
    }

    @PostMapping
    public User createUser(@RequestBody @NonNull User user) {
        return userService.create(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @NonNull User user) {
        return userService.update(user);
    }
}
