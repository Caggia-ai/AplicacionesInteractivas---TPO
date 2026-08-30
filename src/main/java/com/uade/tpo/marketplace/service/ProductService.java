
package com.uade.tpo.marketplace.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.exceptions.ProductDuplicateException;

public interface ProductService {
    public Page<Product> getProducts(PageRequest pageRequest);

    public Optional<Product> getProductById(Long productId);

    public Product createProduct(String nombre, String descripcion, int precio, int stock, int porcentaje_descuento, Long id_category, Long id_user) throws ProductDuplicateException;
}