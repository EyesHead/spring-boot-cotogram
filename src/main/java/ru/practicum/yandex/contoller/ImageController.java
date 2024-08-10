package ru.practicum.yandex.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.yandex.models.Image;
import ru.practicum.yandex.service.ImageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("posts/{postId}/images")
    public List<Image> getPostsImages(@PathVariable("postId") long postId) {
        return imageService.getPostImages(postId);
    }
}
