package ru.practicum.yandex.models;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
@FieldDefaults(makeFinal = true)
public class User {
    Long id;
    String username;
    String email;
    String password;
    Instant registrationDate;
}