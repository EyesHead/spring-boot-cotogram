package ru.practicum.yandex.models.user;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class User {
    // детализированная информация о пользователе
    @NonNull
    private Details details;
    // информация о покупках пользователя
    @Builder.Default
    private PurchasesInformation purchasesInformation = PurchasesInformation.builder().build();
}