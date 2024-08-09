package ru.practicum.yandex.service;

import java.util.Optional;

public enum SortOrder {
    ASCENDING,
    DESCENDING;
    public static Optional<SortOrder> from(String order) {
        return switch (order.toLowerCase()) {
            case "ascending", "asc" -> Optional.of(ASCENDING);
            case "descending", "desc" -> Optional.of(DESCENDING);
            default -> Optional.empty();
        };
    }
}
