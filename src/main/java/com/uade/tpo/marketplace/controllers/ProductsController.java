package com.uade.tpo.marketplace.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.entity.dto.ProductPatchRequest;
import com.uade.tpo.marketplace.entity.dto.ProductRequest;
import com.uade.tpo.marketplace.entity.dto.ProductResponse;
import com.uade.tpo.marketplace.exceptions.ProductDuplicateException;
import com.uade.tpo.marketplace.service.ProductService;

import java.net.URI;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        
        PageRequest pageRequest = (page == null || size == null) 
            ? PageRequest.of(0, Integer.MAX_VALUE) 
            : PageRequest.of(page, size);
            
        // Pasamos todos los filtros al servicio
        Page<ProductResponse> productPage = productService.getProducts(categoryId, minPrice, maxPrice, keyword, pageRequest)
                                                          .map(ProductResponse::fromEntity);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        Optional<Product> result = productService.getProductById(productId);
        
        // Si está presente, lo mapeamos. Si no, devolvemos 404 Not Found 
        return result.map(product -> ResponseEntity.ok(ProductResponse.fromEntity(product)))
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductRequest productRequest,
            @AuthenticationPrincipal User user) throws ProductDuplicateException {
        
        // Pasamos user.getId_user() directamente desde el token
        Product result = productService.createProduct(
            productRequest.getName(),
            productRequest.getDescription(), 
            productRequest.getPrice(), 
            productRequest.getStock(), 
            productRequest.getDiscount_percentage(), 
            productRequest.getId_category(), 
            user.getId_user() 
        );
        return ResponseEntity.created(URI.create("/products/" + result.getId_product()))
                              .body(ProductResponse.fromEntity(result));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> patchProduct(
            @PathVariable Long id, 
            @RequestBody ProductPatchRequest request,
            @AuthenticationPrincipal User user) { // Inyectamos el creador/editor
        
        return ResponseEntity.ok(ProductResponse.fromEntity(productService.patchProduct(id, request, user)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
            
        // El controlador delega ciegamente. Si no es el dueño, el servicio lanza 403 Forbidden.
        productService.deleteProduct(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
