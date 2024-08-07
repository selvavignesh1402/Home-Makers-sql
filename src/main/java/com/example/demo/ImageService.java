package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    private final Path rootLocation = Paths.get("src/main/resources/static/images");

    public ImageService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public String storeImages(MultipartFile[] files) {
        StringBuilder fileUrls = new StringBuilder();
        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            try {
                Files.copy(file.getInputStream(), this.rootLocation.resolve(fileName));
                fileUrls.append("/images/").append(fileName).append(",");
            } catch (IOException e) {
                throw new RuntimeException("Failed to store image", e);
            }
        }
        return fileUrls.toString();
    }
}

