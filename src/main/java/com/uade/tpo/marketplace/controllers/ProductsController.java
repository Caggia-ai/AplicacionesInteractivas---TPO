package com.uade.tpo.marketplace.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.entity.dto.ProductPatchRequest;
import com.uade.tpo.marketplace.entity.dto.ProductRequest;
import com.uade.tpo.marketplace.entity.dto.ProductResponse;
import com.uade.tpo.marketplace.exceptions.ProductDuplicateException;
import com.uade.tpo.marketplace.service.ProductService;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("products")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        
        PageRequest pageRequest = (page == null || size == null) 
            ? PageRequest.of(0, Integer.MAX_VALUE) 
            : PageRequest.of(page, size);
            
        // Obtenemos la página de entidades y la mapeamos a página de DTOs
        Page<ProductResponse> productPage = productService.getProducts(pageRequest)
                                                          .map(ProductResponse::fromEntity);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        Optional<Product> result = productService.getProductById(productId);
        
        // Si está presente, lo mapeamos. Si no, devolvemos 404 Not Found (o 204 No Content)
        return result.map(product -> ResponseEntity.ok(ProductResponse.fromEntity(product)))
                     .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody ProductRequest productRequest)
            throws ProductDuplicateException {
        Product result = productService.createProduct(productRequest.getName(),productRequest.getDescription(), productRequest.getPrice(), productRequest.getStock(), productRequest.getDiscount_percentage(), productRequest.getId_category(), productRequest.getId_user());
        return ResponseEntity.created(URI.create("/products/" + result.getId_product())).body(result);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> patchProduct(@PathVariable Long id, @RequestBody ProductPatchRequest request) {
        return ResponseEntity.ok(productService.patchProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
