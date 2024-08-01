package ru.practicum.yandex.models.user;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;

// детализированная информация о пользователе
@Data
@Builder
public class Details {
    // электронная почта
    @NonNull
    private String email;
    // имя
    @NonNull
    private String firstName;
    // фамилия
    @NonNull
    private String lastName;

    // дополнительная информация
    private String information;
    // дата рождения
    private Date dayOfBirthday;
    // пол
    @Builder.Default
    private Gender gender = Gender.UNKNOWN;
}