package com.uade.tpo.marketplace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.AccessDeniedException;

import com.uade.tpo.marketplace.entity.Image;
import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.repository.ImageRepository;
import com.uade.tpo.marketplace.repository.ProductRepository;
import com.uade.tpo.marketplace.entity.User;

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
    public Image addImageToProduct(Long productId, MultipartFile file, User currentUser) throws IOException, SQLException {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Validación anti-IDOR: ¿Es el dueño del producto o un ADMIN?
        if (!product.getUser().getId_user().equals(currentUser.getId_user()) 
            && !currentUser.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("No tienes permiso para agregar imágenes a este producto");
        }

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
    public void deleteImage(Long id, User currentUser) {
        Image image = viewById(id);

        // Validación anti-IDOR: Navegamos Image -> Product -> User
        if (!image.getProduct().getUser().getId_user().equals(currentUser.getId_user()) 
            && !currentUser.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("No tienes permiso para eliminar esta imagen");
        }

        imageRepository.delete(image);
    }
}
    
