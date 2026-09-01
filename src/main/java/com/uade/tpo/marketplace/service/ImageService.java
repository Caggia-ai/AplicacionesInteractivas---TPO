package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

public interface ImageService {
    Image addImageToProduct(Long productId, MultipartFile file) throws IOException, SQLException;
    Image viewById(Long id);
}
