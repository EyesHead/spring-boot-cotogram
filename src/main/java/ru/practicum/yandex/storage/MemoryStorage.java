package ru.practicum.yandex.storage;

import ru.practicum.yandex.models.user.User;

import java.util.HashMap;
import java.util.Map;

// реализация интерфейса, которая сохраняет данные в хеш-таблице в памяти
public class MemoryStorage implements Storage {
    private Map<String, User> users = new HashMap<>();

    /* Добавление нового пользователя. Должна быть передана электронная почта пользователя,
     которая не должна повторяться */
    @Override
    public void put(final User user) {
        final String email = user.getDetails().getEmail().toLowerCase();
        if (users.containsKey(email)) {
            throw new RuntimeException("User already exists");
        }
        users.put(email, user);
    }

    // получение информации о пользователе
    @Override
    public User get(final String email) {
        if (email == null) {
            return null;
        }
        return users.get(email.toLowerCase());
    }
}