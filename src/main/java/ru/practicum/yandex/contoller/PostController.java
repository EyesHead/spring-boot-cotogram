package ru.practicum.yandex.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.models.Post;
import ru.practicum.yandex.service.PostService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Post> getPosts(@RequestParam(required = false, defaultValue = "asc") String sort,
                               @RequestParam Optional<Integer> from,
                               @RequestParam(required = false, defaultValue = "10") Integer size) {
        return postService.findPosts(sort, from, size);
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) {
        return postService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postService.create(post ));
    }

    @PutMapping
    public Post updatePost(@RequestBody Post post) {
        return postService.update(post);
    }
}
