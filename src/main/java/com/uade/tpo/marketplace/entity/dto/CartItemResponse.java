package com.uade.tpo.marketplace.entity.dto;

import com.uade.tpo.marketplace.entity.CartItem;
import lombok.Data;

@Data
public class CartItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private int unitPrice;
    private int quantity;
    private int subtotal;

    public static CartItemResponse fromEntity(CartItem item) {
        CartItemResponse dto = new CartItemResponse();
        dto.setId(item.getId_cart_item());
        
        if (item.getProduct() != null) {
            dto.setProductId(item.getProduct().getId_product());
            dto.setProductName(item.getProduct().getName());
        }
        
        dto.setUnitPrice(item.getUnit_price());
        dto.setQuantity(item.getQuantity());
        dto.setSubtotal(item.getUnit_price() * item.getQuantity());
        return dto;
    }
}