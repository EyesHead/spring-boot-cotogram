package ru.practicum.yandex.models.post;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Post {
    Long id;
    String title;
    String content;
    String author;
    Instant postDate;
    String description;
}
