package ru.practicum.yandex.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.yandex.exceptions.ConditionsNotMetException;
import ru.practicum.yandex.exceptions.NotFoundException;
import ru.practicum.yandex.models.Post;

import java.time.Instant;
import java.util.*;

@Slf4j
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

    public final List<Post> findPosts(String sort, Optional<Integer> from, Integer size) {
        SortOrder sortOrder = SortOrder.from(sort)
                .orElseThrow(() -> new ConditionsNotMetException("Sort order = " + sort + " is not supported"));

        // Сортируем посты в зависимости от sortOrder
        List<Post> sortedPosts = posts.values().stream()
                .sorted(sortOrder == SortOrder.ASCENDING
                        ? Comparator.comparing(Post::getPostDate)
                        : Comparator.comparing(Post::getPostDate).reversed())
                .toList();

        // Определяем значение по умолчанию для пропуска постов
        int startIndex = from.orElseGet(() -> Math.max(0, sortedPosts.size() - size));

        return sortedPosts.stream()
                .skip(startIndex)
                .limit(size)
                .toList();
    }

    public final Post findById(Long postId) throws ConditionsNotMetException {
        Post post = posts.get(postId);
        if (post == null) {
            throw new ConditionsNotMetException("Post with ID=" + postId + " doesn't exist");
        }
        return post;
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
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Description is required");
        }
        //проверяем, что автор поста существует в репозитории
        userService.checkUserExistInMemory(post.getAuthorId());
    }

    private void checkForUpdate(Post post) throws ConditionsNotMetException, NotFoundException {
        if (!posts.containsKey(post.getId())) {
            throw new NotFoundException("Post with ID = " + post.getId() + " doesn't exist");
        }

        userService.checkUserExistInMemory(post.getAuthorId());

        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Description is required");
        }

        if (post.getId() == null) {
            throw new ConditionsNotMetException("ID is required");
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
