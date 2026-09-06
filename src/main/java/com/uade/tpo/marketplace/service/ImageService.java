package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.entity.Image;
import org.springframework.web.multipart.MultipartFile;
import com.uade.tpo.marketplace.entity.User;

import java.io.IOException;
import java.sql.SQLException;

public interface ImageService {
    Image addImageToProduct(Long productId, MultipartFile file, User currentUser) throws IOException, SQLException;
    Image viewById(Long id);
    void deleteImage(Long id, User currentUser);
}
