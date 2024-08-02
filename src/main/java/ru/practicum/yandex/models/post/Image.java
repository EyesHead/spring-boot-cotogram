package ru.practicum.yandex.models.post;

import lombok.Data;

@Data
public class Image {
    Long id;
    long postId;
    String originalFieldName;
    String filePath;
}
