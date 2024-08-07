package ru.practicum.yandex.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.yandex.exceptions.ConditionsNotMetException;
import ru.practicum.yandex.exceptions.DuplicatedDataException;
import ru.practicum.yandex.exceptions.NotFoundException;
import ru.practicum.yandex.models.user.User;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    Map<Long, User> users;

    public Collection<User> getAll() {
        return users.values();
    }

    public User create(User user) {
        User newUser = user.toBuilder().id(getNextId()).registrationDate(Instant.now()).build();
        validateOnPOST(newUser);
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public User update(@RequestBody User user) {
        if (isRequestUserValid(user)) return user;
        validateOnPUT(user);

        User newUser = user.toBuilder()
                .id(user.getId())
                .registrationDate(Instant.now())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public Optional<User> getUser(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    private static boolean isRequestUserValid(User updatedUser) {
        return updatedUser.getUsername() == null || updatedUser.getEmail() == null
                || updatedUser.getPassword() == null;
    }

    private void checkEmailAlreadyUsed(User user) {
        if (users.isEmpty()) return;

        users.values().stream()
                .filter(streamUser -> !streamUser.getId().equals(user.getId())
                        && streamUser.getEmail().equalsIgnoreCase(user.getEmail()))
                .filter(userStream -> userStream.getEmail().equalsIgnoreCase(user.getEmail()))
                .findAny()
                .ifPresent(streamUser -> {
                    throw new DuplicatedDataException("Этот имейл уже используется");
                });
    }

    public void validateOnPUT(User updateUser) {
        if (!users.containsKey(updateUser.getId())) {
            throw new NotFoundException("Пользователь с указанным id не найден в системе");
        }
        if (updateUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        checkEmailAlreadyUsed(updateUser);
    }

    private void validateOnPOST(User newUser) {
        if (newUser.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        checkEmailAlreadyUsed(newUser);
    }

    private long getNextId() {
        long nextId = users.keySet().stream()
                .max(Long::compareTo)
                .orElse(0L);
        return ++nextId;
    }
}
