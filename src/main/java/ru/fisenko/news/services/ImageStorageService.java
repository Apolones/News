package ru.fisenko.news.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fisenko.news.controllers.NewsController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageStorageService {

    @Value("${upload.path}")
    private String uploadPath;
    private static final Logger logger = LoggerFactory.getLogger(ImageStorageService.class);

    //Save image on file system
    public void saveImage(MultipartFile file, String fileName) {
        try {
            Files.write(Paths.get(uploadPath, fileName), file.getBytes());
        } catch (IOException e) {
            logger.error("Could not save the file: " + fileName, e);
        }
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }
}
