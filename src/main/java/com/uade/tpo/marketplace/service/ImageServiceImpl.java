package com.uade.tpo.marketplace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.marketplace.entity.Image;
import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.repository.ImageRepository;
import com.uade.tpo.marketplace.repository.ProductRepository;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;
    
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Image addImageToProduct(Long productId, MultipartFile file) throws IOException, SQLException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Blob blob = new SerialBlob(file.getBytes());
        
        Image image = new Image();
        image.setImage(blob);
        image.setProduct(product); 
        
        return imageRepository.save(image);
    }

    @Override
    public Image viewById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
    }

    @Override
    public void deleteImage(Long id) {
        Image image = viewById(id);
        imageRepository.delete(image);
    }
}
    
