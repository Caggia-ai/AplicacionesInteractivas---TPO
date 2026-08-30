package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.marketplace.entity.Image;
import com.uade.tpo.marketplace.entity.dto.ImageRequest;
import com.uade.tpo.marketplace.service.ImageService;

@RestController
@RequestMapping("images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping
    public ResponseEntity<Image> addImage(@RequestBody ImageRequest request) {
        Image image = imageService.addImageToProduct(request.getProductId(), request.getUrl());
        return ResponseEntity.ok(image);
    }
}