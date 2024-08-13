package ru.practicum.yandex.models;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder(toBuilder = true)
public class User {
    Long id;
    String username;
    String email;
    String password;
    Instant registrationDate;
}