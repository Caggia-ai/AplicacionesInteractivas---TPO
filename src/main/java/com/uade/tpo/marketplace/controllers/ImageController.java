package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType; // IMPORTANTE
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.uade.tpo.marketplace.entity.Image;
import com.uade.tpo.marketplace.entity.dto.AddFileRequest;
import com.uade.tpo.marketplace.entity.dto.ImageResponse;
import com.uade.tpo.marketplace.service.ImageService;
import com.uade.tpo.marketplace.entity.User;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

@RestController
@RequestMapping("images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping
    public ResponseEntity<String> addImage(
            AddFileRequest request, 
            @AuthenticationPrincipal User currentUser) throws IOException, SQLException {
            
        imageService.addImageToProduct(request.getProductId(), request.getFile(), currentUser);
        return ResponseEntity.ok("created");
    }

    // método para obtener la imagen codificada en Base64 (viejo)
    @GetMapping
    public ResponseEntity<ImageResponse> displayImage(@RequestParam("id") Long id) throws IOException, SQLException {
        Image image = imageService.viewById(id);
        String encodedString = Base64.getEncoder()
                .encodeToString(image.getImage().getBytes(1, (int) image.getImage().length()));

        ImageResponse response = new ImageResponse();
        response.setId(id);
        response.setFile(encodedString);
        return ResponseEntity.ok().body(response);
    }

    // Devuelve el archivo crudo para poder usarlo en <img src="URL">
    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewImageNatively(@PathVariable Long id) throws SQLException {
        Image image = imageService.viewById(id);
        Blob blob = image.getImage();
        
        // Convertimos el Blob de la BD a un array de bytes
        byte[] imageBytes = blob.getBytes(1, (int) blob.length());
        
        
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Le dice al navegador que es una imagen real
                .body(imageBytes);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteImage(
            @RequestParam("id") Long id,
            @AuthenticationPrincipal User currentUser) {
            
        imageService.deleteImage(id, currentUser);
        return ResponseEntity.ok("Imagen eliminada correctamente");
    }
}