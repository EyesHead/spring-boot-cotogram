package ru.practicum.yandex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.yandex.exceptions.ConditionsNotMetException;
import ru.practicum.yandex.exceptions.NotFoundException;
import ru.practicum.yandex.models.post.Post;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();
    private final UserService userService;

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public Post create(Post post) {
        // проверяем выполнение необходимых условий
        checkDescriptionNotBlank(post);
        //проверяем, что автор поста существует в репозитории
        checkAuthorOfPostExistAsUser(post);

        Post newPost = post.toBuilder()
                .id(getNextId())
                .postDate(Instant.now())
                .build();
        // сохраняем новую публикацию в памяти приложения
        posts.put(newPost.getId(), newPost);
        return newPost;
    }

    private void checkAuthorOfPostExistAsUser(Post post) {
        Long authorId = post.getAuthorId();
        if (userService.getUser(authorId).isEmpty()) {
            throw new ConditionsNotMetException("Автор с ID = " + authorId + " не найден");
        }
    }

    private static void checkDescriptionNotBlank(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
    }

    public Post update(Post post) {
        checkDescriptionNotBlank(post);

        if (post.getId() == null) {
            throw new ConditionsNotMetException("ID должен быть указан");
        }

        if (!posts.containsKey(post.getId())) {
            throw new NotFoundException("Пост с ID = " + post.getId() + " не найден");
        }

        Post updatedPost = post.toBuilder()
                .description(post.getDescription())
                .build();
        posts.put(post.getId(), updatedPost);
        return updatedPost;
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .max(Long::compareTo)
                .orElse(0L);
        return ++currentMaxId;
    }
}
