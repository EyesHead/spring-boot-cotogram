package ru.practicum.yandex.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.yandex.exceptions.ConditionsNotMetException;
import ru.practicum.yandex.exceptions.NotFoundException;
import ru.practicum.yandex.exceptions.ParameterNotValidException;
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
        checkPostDescriptionField(post);
        userService.checkIsUserExistInMemory(post.getAuthorId());


        Post createdPost = post.toBuilder()
                .id(getNextId())
                .postDate(Instant.now())
                .build();
        // сохраняем новую публикацию в памяти приложения
        posts.put(createdPost.getId(), createdPost);
        return createdPost;
    }

    public final List<Post> findPosts(String sort, Optional<Integer> from, Integer size) {
        checkSizeParameter(size);
        checkFromParameter(from);
        SortOrder sortOrder = checkAndGetSortParameter(sort);
        // Сортируем посты в зависимости от sortOrder
        List<Post> sortedPosts = getSortedPosts(sortOrder);
        // Определяем индекс по умолчанию для пропуска постов
        int startIndex = from.orElseGet(() -> Math.max(0, sortedPosts.size() - size));

        return sortedPosts.stream()
                .skip(startIndex)
                .limit(size)
                .toList();
    }

    public final Post findById(Long postId) {
        return Optional.ofNullable(posts.get(postId))
                .orElseThrow(()->
                    new ConditionsNotMetException(String.format("Post with ID=%s was not found in service", postId)));
    }

    public final Post update(Post post) throws ConditionsNotMetException, NotFoundException {
        checkPostDescriptionField(post);
        long postId = Optional.ofNullable(post.getId()).orElseThrow(() ->
                new ConditionsNotMetException("ID is required")
        );
        checkIsPostExist(postId);
        userService.checkIsUserExistInMemory(post.getAuthorId());

        Post updatedPost = posts.get(post.getId()).toBuilder()
                .description(post.getDescription())
                .build();
        posts.put(updatedPost.getId(), updatedPost);
        return updatedPost;
    }

    private static SortOrder checkAndGetSortParameter(String sort) throws ParameterNotValidException {
        return SortOrder.from(sort)
                .orElseThrow(() -> new ParameterNotValidException(
                        "sort", "Параметр sort может принимать значения: asc, ascending, desc, descending"
                ));
    }

    private List<Post> getSortedPosts(SortOrder sortOrder) {
        return posts.values().stream()
                .sorted(sortOrder == SortOrder.ASCENDING
                        ? Comparator.comparing(Post::getPostDate)
                        : Comparator.comparing(Post::getPostDate).reversed())
                .toList();
    }


    private static void checkSizeParameter(Integer size) {
        if (size < 0) {
            throw new ParameterNotValidException("size", "Некорректный размер выборки. Размер должен быть больше нуля");
        }
    }

    private static void checkFromParameter(Optional<Integer> from) {
        if (from.isPresent() && from.get() < 0) {
            throw new ParameterNotValidException("from", "Недопустимое значение начального индекса - отрицательное число");
        }
    }

    private void checkPostDescriptionField(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Description is required");
        }
    }

    private void checkIsPostExist(long postId) throws NotFoundException {
        if (!posts.containsKey(postId)) {
            throw new NotFoundException("Post with ID = " + postId + " doesn't exist");
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
