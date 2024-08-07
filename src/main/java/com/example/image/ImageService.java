package com.example.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ImageService {

    @Value("${image.upload.dir}")
    private String uploadDir;

    @Autowired
    private ImageRepository imageRepository;

    public String saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        // Ensure the directory exists
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Save the file
        Path path = Paths.get(uploadDir, file.getOriginalFilename());
        Files.write(path, file.getBytes());

        // Save metadata to the database
        Image image = new Image();
        image.setUrl(path.toString());
        imageRepository.save(image);

        // Return the path or URL of the saved image
        return path.toString();
    }

    public Image getImage(Long id) {
        return imageRepository.findById(id).orElse(null);
    }

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }
}
