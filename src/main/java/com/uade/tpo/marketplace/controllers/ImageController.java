package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.marketplace.entity.Image;
import com.uade.tpo.marketplace.entity.dto.AddFileRequest;
import com.uade.tpo.marketplace.entity.dto.ImageResponse;
import com.uade.tpo.marketplace.service.ImageService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;

@RestController
@RequestMapping("images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping
    public ResponseEntity<String> addImage(AddFileRequest request) throws IOException, SQLException {
        imageService.addImageToProduct(request.getProductId(), request.getFile());
        return ResponseEntity.ok("created");
    }

    @GetMapping
    public ResponseEntity<ImageResponse> displayImage(@RequestParam("id") Long id) throws IOException, SQLException {
        Image image = imageService.viewById(id);
        String encodedString = Base64.getEncoder()
                .encodeToString(image.getImage().getBytes(1, (int) image.getImage().length()));

        ImageResponse response = new ImageResponse();
        response.setId(id);
        response.setFile(encodedString);
        return ResponseEntity.ok().body(response);

    @DeleteMapping
    public ResponseEntity<String> deleteImage(@RequestParam("id") Long id) {
        imageService.deleteImage(id);
        return ResponseEntity.ok("Imagen eliminada correctamente");
    }
}
