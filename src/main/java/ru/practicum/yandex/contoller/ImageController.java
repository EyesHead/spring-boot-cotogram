package ru.practicum.yandex.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.yandex.models.Image;
import ru.practicum.yandex.models.ImageData;
import ru.practicum.yandex.service.ImageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/posts/{postId}/images")
    public List<Image> addPostImages(@PathVariable("postId") long postId,
                                     @RequestParam("image") List<MultipartFile> files) {
        return imageService.saveImages(postId, files);
    }

    // Content-Type : application/octet-stream
    @GetMapping(value = "/images/{imageId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadImage(@PathVariable("imageId") long imageId) {
        ImageData imageData = imageService.getImageData(imageId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().build());
        return new ResponseEntity<>(imageData.getImageBytes(), headers, HttpStatus.OK);
    }
}
