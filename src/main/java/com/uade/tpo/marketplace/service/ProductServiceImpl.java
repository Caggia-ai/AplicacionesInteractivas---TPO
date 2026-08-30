package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.repository.CategoryRepository;
import com.uade.tpo.marketplace.repository.UserRepository;

import com.uade.tpo.marketplace.exceptions.ProductDuplicateException;
import com.uade.tpo.marketplace.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Product> getProducts(PageRequest pageable) {
        return productoRepository.findAll(pageable);
    }

    public Optional<Product> getProductById(Long productId) {
        return productoRepository.findById(productId);
    }

    public Product createProduct(String name, String description, int price, int stock, int discount_percentage, Long id_category, Long id_user) throws ProductDuplicateException {
        List<Product> productos = productoRepository.findByName(name);
        if (productos.isEmpty()){
            // 1. Buscamos las entidades usando los parámetros exactos de tu método
            Category category = categoryRepository.findById(id_category).orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
                
            User user = userRepository.findById(id_user).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // 2. Creamos el producto
            Product product = new Product(name, description, price, stock, discount_percentage);
            
            // 3. Asignamos las Foreign Keys correctamente (reemplaza el "product.setca")
            product.setCategory(category);
            product.setUser(user);
            
            return productoRepository.save(product);
        }
        throw new ProductDuplicateException();
    }
}