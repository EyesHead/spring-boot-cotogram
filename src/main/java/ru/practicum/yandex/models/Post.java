package ru.practicum.yandex.models;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
@FieldDefaults(makeFinal = true)
public class Post {
    Long id;
    Long authorId;
    String description;
    Instant postDate;
}
