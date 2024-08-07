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

    public final Post create(Post post) throws ConditionsNotMetException {
        checkForCreate(post);

        Post createdPost = post.toBuilder()
                .id(getNextId())
                .postDate(Instant.now())
                .build();
        // сохраняем новую публикацию в памяти приложения
        posts.put(createdPost.getId(), createdPost);
        return createdPost;
    }

    public final Collection<Post> findAll() {
        return posts.values();
    }

    public final Post update(Post post) throws ConditionsNotMetException, NotFoundException {
        checkForUpdate(post);

        Post updatedPost = posts.get(post.getId()).toBuilder()
                .description(post.getDescription())
                .build();
        posts.put(updatedPost.getId(), updatedPost);
        return updatedPost;
    }

    private void checkForCreate(Post post) {
        // проверяем выполнение необходимых условий
        checkDescriptionNotBlank(post);
        //проверяем, что автор поста существует в репозитории
        checkAuthorOfPostExistAsUser(post);
    }

    private void checkForUpdate(Post post) throws ConditionsNotMetException, NotFoundException {
        // проверяем выполнение необходимых условий
        checkDescriptionNotBlank(post);
        if (post.getId() == null) {
            throw new ConditionsNotMetException("ID должно быть указано для обновления");
        }
        //проверяем, что автор поста существует в репозитории
        checkAuthorOfPostExistAsUser(post);

        if (!posts.containsKey(post.getId())) {
            throw new NotFoundException("Пост с ID = " + post.getId() + " не найден");
        }
    }

    private void checkAuthorOfPostExistAsUser(Post post) throws ConditionsNotMetException {
        Long authorId = post.getAuthorId();
        if (userService.getUser(authorId).isEmpty()) {
            throw new NotFoundException("Автор с ID = " + authorId + " не найден");
        }
    }

    private static void checkDescriptionNotBlank(Post post) throws ConditionsNotMetException {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .max(Long::compareTo)
                .orElse(0L);
        return ++currentMaxId;
    }
}
