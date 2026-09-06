package com.uade.tpo.marketplace.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.dto.ProductPatchRequest;
import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.repository.CategoryRepository;
import com.uade.tpo.marketplace.repository.UserRepository;

import com.uade.tpo.marketplace.exceptions.ProductDuplicateException;
import com.uade.tpo.marketplace.repository.ProductRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Page<Product> getProducts(Long categoryId, Integer minPrice, Integer maxPrice, String keyword, PageRequest pageable) {
        return productRepository.findWithFilters(categoryId, minPrice, maxPrice, keyword, pageable);
    }

    public Optional<Product> getProductById(Long productId) {
        return productRepository.findByIdAndState(productId, true);
    }

    public Product createProduct(String name, String description, int price, int stock, int discount_percentage, Long id_category, Long id_user) throws ProductDuplicateException {
        Optional<Product> productos = productRepository.findByName(name);
        if (productos.isEmpty()){
            Category category = categoryRepository.findById(id_category).orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
                
            User user = userRepository.findById(id_user).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            Product product = new Product(name, description, price, stock, discount_percentage);
            
            product.setCategory(category);
            product.setUser(user);
            
            return productRepository.save(product);
        }
        throw new ProductDuplicateException();
    }

    public Product patchProduct(Long id, ProductPatchRequest request, User currentUser) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Verificamos que el usuario logueado sea el dueño del producto (o un ADMIN)
        if (!product.getUser().getId_user().equals(currentUser.getId_user()) 
            && !currentUser.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("No tienes permiso para modificar este producto");
        }

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getStock() != null) {
            product.setStock(request.getStock());
        }
        if (request.getDiscount_percentage() != null) {
            product.setDiscount_percentage(request.getDiscount_percentage());
        }

        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long productId, User currentUser) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Validación anti-IDOR: ¿Es el dueño del producto o un ADMIN?
        if (!product.getUser().getId_user().equals(currentUser.getId_user()) 
            && !currentUser.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("No tienes permiso para eliminar este producto");
        }

        // Aplicamos la baja lógica (oculta el producto sin romper historiales de compras)
        product.setState(false);
        productRepository.save(product);
    }
}