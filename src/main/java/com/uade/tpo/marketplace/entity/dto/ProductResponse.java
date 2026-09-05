package com.uade.tpo.marketplace.entity.dto;

import com.uade.tpo.marketplace.entity.Product;
import lombok.Data;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private int price; // Precio original
    private int finalPrice; // Precio con el descuento ya aplicado
    private int stock;
    private int discountPercentage;
    private String categoryName;
    private String sellerUsername;

    public static ProductResponse fromEntity(Product product) {
        ProductResponse dto = new ProductResponse();
        dto.setId(product.getId_product());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setDiscountPercentage(product.getDiscount_percentage());
        
        // --- Cálculo para el frontend ---
        int descuentoApli = (product.getPrice() * product.getDiscount_percentage()) / 100;
        dto.setFinalPrice(product.getPrice() - descuentoApli);
        
        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getDescription());
        }
        if (product.getUser() != null) {
            dto.setSellerUsername(product.getUser().getUsername());
        }
        return dto;
    }
}