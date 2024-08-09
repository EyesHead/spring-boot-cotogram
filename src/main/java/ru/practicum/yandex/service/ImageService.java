package ru.practicum.yandex.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.yandex.models.Image;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final Map<Long, Image> images = new HashMap<>();

    public List<Image> getPostImages(long postId) {
        return images.values().stream()
                .filter(image -> image.getPostId() == postId)
                .toList();
    }
}
