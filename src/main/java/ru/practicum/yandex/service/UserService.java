package ru.practicum.yandex.service;

import org.springframework.stereotype.Service;
import ru.practicum.yandex.exceptions.ConditionsNotMetException;
import ru.practicum.yandex.exceptions.DuplicatedDataException;
import ru.practicum.yandex.exceptions.NotFoundException;
import ru.practicum.yandex.models.user.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    Map<Long, User> users = new HashMap<>();

    public Collection<User> getAll() {
        return users.values();
    }

    public User create(User user) {
        User newUser = user.toBuilder().id(getNextId()).registrationDate(Instant.now()).build();

        checkRequiredFieldForNull(newUser);
        checkEmailAlreadyUsed(newUser);

        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public User update(User user) {
        try {
            checkRequiredFieldForNull(user);
        } catch (ConditionsNotMetException e) {
            return user;
        }
        checkUserExistInMemory(user);
        checkEmailAlreadyUsed(user);
        //Создаем пользователя на основе старых данных и обновляем данные
        User newUser = users.get(user.getId()).toBuilder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
        users.put(user.getId(), newUser);
        return user;
    }

    private void checkRequiredFieldForNull(User user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            throw new ConditionsNotMetException("Required user data cant be null");
        }
    }

    public Optional<User> getUser(Long userId) {
        return Optional.ofNullable(users.get(userId));
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

    public void checkUserExistInMemory(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с указанным id не найден в системе");
        }
    }

    private long getNextId() {
        long nextId = users.keySet().stream()
                .max(Long::compareTo)
                .orElse(0L);
        return ++nextId;
    }
}
