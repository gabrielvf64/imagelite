package io.github.dougllasfps.imageliteapi.application.images;

import io.github.dougllasfps.imageliteapi.domain.entity.Image;
import io.github.dougllasfps.imageliteapi.domain.enums.ImageExtension;
import io.github.dougllasfps.imageliteapi.domain.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/images")
@RequiredArgsConstructor
public class ImagesController {

    private final ImageService service;
    private final ImageMapper mapper;

    @PostMapping
    public ResponseEntity save(@RequestParam("file") MultipartFile file,
                               @RequestParam("name") String name,
                               @RequestParam("tags") List<String> tags) throws IOException {
        log.info("Recevied image: name: {}, size: {}", file.getOriginalFilename(), file.getSize());

        Image image = mapper.mapToImage(file, name, tags);

        Image savedImage = service.save(image);

        URI location = buildImageURL(savedImage);

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        Optional<Image> optionalImage = service.getById(id);

        if (optionalImage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Image image = optionalImage.get();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(image.getExtension().getMediaType());
        headers.setContentLength(image.getSize());
        headers.setContentDispositionFormData("inline; filename=\"" + image.getFileNameWithFileExtension() + "\"", image.getFileNameWithFileExtension());

        return new ResponseEntity<>(image.getFile(), headers, HttpStatus.OK);
    }

    // localhost:8080/v1/images?extension=PNG&query=Natu
    @GetMapping
    public ResponseEntity<List<ImageDTO>> search(
            @RequestParam(value = "extension", required = false) String extension,
            @RequestParam(value = "query", required = false) String query) {

        List<Image> result = service.search(ImageExtension.valueOf(extension), query);

        // convert all images to imageDTO
        List<ImageDTO> imageDTOList = result.stream().map(image -> {
            URI uri = buildImageURL(image);
            return mapper.imageToDTO(image, uri.toString());
        }).collect(Collectors.toList());

        return ResponseEntity.ok(imageDTOList);
    }

    private URI buildImageURL(Image image) {
        String imagePath = "/" + image.getId();
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(imagePath)
                .build()
                .toUri();
    }
}
