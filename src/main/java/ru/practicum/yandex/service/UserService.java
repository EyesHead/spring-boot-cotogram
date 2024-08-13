package ru.practicum.yandex.service;

import org.springframework.stereotype.Service;
import ru.practicum.yandex.exceptions.ConditionsNotMetException;
import ru.practicum.yandex.exceptions.DuplicatedDataException;
import ru.practicum.yandex.exceptions.NotFoundException;
import ru.practicum.yandex.models.User;

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

    public Optional<User> getUser(Long userId) throws ConditionsNotMetException {
        return Optional.ofNullable(users.get(userId));
    }

    public User create(User user) throws ConditionsNotMetException, DuplicatedDataException {
        User newUser = user.toBuilder().id(getNextId()).registrationDate(Instant.now()).build();

        checkEmailNotNull(user.getEmail());
        checkPasswordNotNull(user.getPassword());
        checkEmailAlreadyUsed(newUser);

        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public User update(User user) throws DuplicatedDataException, NotFoundException {
        try {
            checkEmailNotNull(user.getEmail());
            checkPasswordNotNull(user.getPassword());
        } catch (ConditionsNotMetException e) {
            return user;
        }
        checkIsUserExistInMemory(user.getId());
        checkEmailAlreadyUsed(user);
        //Создаем пользователя на основе старых данных и обновляем данные
        User newUser = users.get(user.getId()).toBuilder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
        users.put(user.getId(), newUser);
        return user;
    }

    private static void checkPasswordNotNull(String password) {
        if (password == null) {
            throw new ConditionsNotMetException("Password is required");
        }
    }

    private static void checkEmailNotNull(String email) {
        if (email == null) {
            throw new ConditionsNotMetException("Email is required");
        }
    }

    private void checkEmailAlreadyUsed(User user) throws DuplicatedDataException {
        if (users.isEmpty()) return;

        users.values().stream()
                .filter(userStream -> //поиск пользователя по полям id и email в репозитории
                        !userStream.getId().equals(user.getId())
                        && userStream.getEmail().equalsIgnoreCase(user.getEmail()))
                .findAny()
                .ifPresent(streamUser -> {
                    throw new DuplicatedDataException("Email {" + streamUser.getEmail() + "} already used");
                });
    }

    public void checkIsUserExistInMemory(Long userId) throws NotFoundException {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("User with ID= " + userId + " was not found");
        }
    }

    private long getNextId() {
        long nextId = users.keySet().stream()
                .max(Long::compareTo)
                .orElse(0L);
        return ++nextId;
    }
}
