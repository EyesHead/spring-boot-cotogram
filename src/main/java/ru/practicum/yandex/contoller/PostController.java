package ru.practicum.yandex.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.exceptions.ConditionsNotMetException;
import ru.practicum.yandex.exceptions.NotFoundException;
import ru.practicum.yandex.models.post.Post;
import ru.practicum.yandex.service.PostService;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    PostService postService;

    @GetMapping
    public Collection<Post> getAllPosts() {
        return postService.findAll();
    }

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post updatePost(@RequestBody Post post) {
        return postService.update(post);
    }
}
