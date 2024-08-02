package ru.practicum.yandex.models.user;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
public class User {
    Long id;
    String username;
    String email;
    String password;
    Instant registrationDate;
    // детализированная информация о пользователе
//    @Builder.Default
//    Details details = Details.builder().gender(Gender.UNKNOWN).build();
    // информация о покупках пользователя
//    @Builder.Default
//    PurchasesInformation purchasesInformation = PurchasesInformation.builder().build();
}