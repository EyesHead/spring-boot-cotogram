package ru.practicum.yandex.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.yandex.exceptions.ConditionsNotMetException;
import ru.practicum.yandex.exceptions.ImageFileIOException;
import ru.practicum.yandex.exceptions.NotFoundException;
import ru.practicum.yandex.models.Image;
import ru.practicum.yandex.models.ImageData;
import ru.practicum.yandex.models.Post;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final PostService postService;

    private final Map<Long, Image> images = new HashMap<>();

    // директория для хранения изображений
    @Value("${catsgram.image-directory}")
    private String imageDirectory;

    // сохранение списка изображений, связанных с указанным постом
    public List<Image> saveImages(long postId, List<MultipartFile> files) {
        return files.stream().map(file -> saveImage(postId, file)).collect(Collectors.toList());
    }

    public ImageData getImageData(long imageId) {
        // Проверяем есть ли искомый image в мапе images
        Optional<Image> foundImage = Optional.ofNullable(images.get(imageId));
        if (foundImage.isEmpty()) {
            throw new NotFoundException(String.format("Image with ID=%s doesn't exist", imageId));
        }

        byte[] imageData = getImageBytes(foundImage.get());

        return ImageData.builder()
                .imageBytes(imageData)
                .name(foundImage.get().getOriginalFileName())
                .build();
    }

    private byte[] getImageBytes(Image image) throws ImageFileIOException {
        Path path = Paths.get(image.getFilePath());

        if (!Files.exists(path)) {
            throw new ImageFileIOException(
                    String.format("Image doesn't exist on server: name=%s", image.getOriginalFileName()));
        }

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new ImageFileIOException(
                    String.format("Error while reading file with path=%s", path));
        }
    }

    // сохранение отдельного изображения, связанного с указанным постом
    private Image saveImage(long postId, MultipartFile file) {
        Post post = postService.findById(postId)
            .orElseThrow(() ->
                new ConditionsNotMetException(String.format("Post with ID=%s was not found", postId)));

        // сохраняем изображение на диск и возвращаем путь к файлу
        Path filePath = saveFile(file, post);

        // создаём объект для хранения данных изображения
        long imageId = getNextId();

        // создание объекта изображения и заполнение его данными
        Image image = Image.builder()
            .id(imageId)
            .filePath(filePath.toString())
            .postId(postId)
            .originalFileName(file.getOriginalFilename())
            .build();
        images.put(imageId, image);

        return image;
    }

    // сохранение файла изображения
    private Path saveFile(MultipartFile file, Post post) {
        try {
            // формирование уникального названия файла на основе текущего времени и расширения оригинального файла
            String uniqueFileName = String.format("%d.%s", Instant.now().toEpochMilli(),
                    StringUtils.getFilenameExtension(file.getOriginalFilename()));

            // формирование пути для сохранения файла с учётом идентификаторов автора и поста
            Path uploadPath = Paths.get(imageDirectory, String.valueOf(post.getAuthorId()), post.getId().toString());
            Path filePath = uploadPath.resolve(uniqueFileName);

            // создаём директории, если они ещё не созданы
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // сохраняем файл по сформированному пути
            file.transferTo(filePath);
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long getNextId() {
        long currentMaxId = images.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}