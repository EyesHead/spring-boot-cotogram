package ru.practicum.yandex.storage;

import ru.practicum.yandex.models.user.User;

public interface Storage {
    void put(User user);

    User get(String email);
}