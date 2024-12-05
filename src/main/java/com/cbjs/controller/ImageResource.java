package com.cbjs.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cbjs.service.ImageService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/images")
@Tag(name = "Image")
public class ImageResource {
    
    @Autowired
    private ImageService imageService;
    
    @GetMapping
    public ResponseEntity<Map<String, String>> getImage(
            @RequestParam("file") String imageUrl,
            @RequestParam(required = false) Boolean resize) {
        try {
            byte[] imageBytes = imageService.loadImageFromUrl(imageUrl, resize);
            String contentType = imageService.getContentType(imageUrl);
            String base64Data = Base64.getEncoder().encodeToString(imageBytes);
            
            Map<String, String> response = new HashMap<>();
            response.put("data", "data:" + contentType + ";base64," + base64Data);
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse);
        }
    }
}
