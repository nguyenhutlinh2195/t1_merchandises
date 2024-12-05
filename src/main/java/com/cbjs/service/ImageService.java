package com.cbjs.service;

import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

@Service
public class ImageService {
    private static final int DEFAULT_WIDTH = 100;  // Default resize width
    private static final int DEFAULT_HEIGHT = 100; // Default resize height

    public byte[] loadImageFromUrl(String imageUrl, Boolean resize) throws IOException {
        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();
        
        try (InputStream inputStream = connection.getInputStream()) {
            // If no resize needed, return original
            if (resize == null || !resize) {
                return inputStream.readAllBytes();
            }
            
            // Read the image
            BufferedImage originalImage = ImageIO.read(inputStream);
            if (originalImage == null) {
                throw new IOException("Invalid image format");
            }
            
            // Resize image to default dimensions
            Image resultingImage = originalImage.getScaledInstance(DEFAULT_WIDTH, DEFAULT_HEIGHT, Image.SCALE_SMOOTH);
            BufferedImage outputImage = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
            outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
            
            // Convert to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(outputImage, "jpg", outputStream);
            return outputStream.toByteArray();
        }
    }

    public String getContentType(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();
        return connection.getContentType();
    }
} 