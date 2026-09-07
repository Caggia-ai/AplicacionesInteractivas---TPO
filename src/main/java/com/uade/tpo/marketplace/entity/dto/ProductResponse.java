package com.uade.tpo.marketplace.entity.dto;

import com.uade.tpo.marketplace.entity.Product;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.uade.tpo.marketplace.entity.Image; 
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Data
@JsonPropertyOrder({
    "name",
    "categoryName",
    "price",
    "discountPercentage",
    "finalPrice",
    "stock",
    "description",
    "sellerUsername",
    "id",
    "imageIds"
})
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private int price;
    private int finalPrice; 
    private int stock;
    private int discountPercentage;
    private String categoryName;
    private String sellerUsername;
    private List<Long> imageIds;

    public static ProductResponse fromEntity(Product product) {
        ProductResponse dto = new ProductResponse();
        dto.setId(product.getId_product());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setDiscountPercentage(product.getDiscount_percentage());
        
        int descuentoApli = (product.getPrice() * product.getDiscount_percentage()) / 100;
        dto.setFinalPrice(product.getPrice() - descuentoApli);
        
        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getDescription());
        }
        if (product.getUser() != null) {
            dto.setSellerUsername(product.getUser().getUsername());
        }
        
        // --- MAPEAMOS LAS IMÁGENES ---
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            dto.setImageIds(product.getImages().stream()
                                   .map(Image::getId_image)
                                   .toList());
        } else {
            dto.setImageIds(new ArrayList<>());
        }
        
        return dto;
    }
}