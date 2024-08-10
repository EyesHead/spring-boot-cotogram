package ru.practicum.yandex.models;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Builder
public class Image {
    Long id;
    long postId;
    String originalFileName;
    String filePath;
}
