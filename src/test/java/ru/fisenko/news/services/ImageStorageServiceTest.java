package ru.fisenko.news.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImageStorageServiceTest {

    @InjectMocks
    private ImageStorageService imageStorageService;

    @Test
    void saveImage_ShouldSaveFile() {
        String uploadPath = "/images/";
        imageStorageService.setUploadPath(uploadPath);

        String fileName = "test-image.jpg";
        byte[] fileContent = "some image".getBytes();
        MockMultipartFile file = new MockMultipartFile("image", fileName, "image/jpeg", fileContent);

        imageStorageService.saveImage(file, fileName);

        Path savedFilePath = Paths.get(uploadPath, fileName);
        assertTrue(Files.exists(savedFilePath));
    }
}
