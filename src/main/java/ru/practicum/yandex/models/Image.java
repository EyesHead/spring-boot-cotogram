package ru.practicum.yandex.models;

import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(makeFinal = true)
public class Image {
    private Long id;
    private long postId;
    private String originalFileName;
    private String filePath;
}
