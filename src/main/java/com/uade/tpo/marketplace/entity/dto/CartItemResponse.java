package com.uade.tpo.marketplace.entity.dto;

import com.uade.tpo.marketplace.entity.CartItem;
import com.uade.tpo.marketplace.entity.Product;

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
            Product p = item.getProduct();
            dto.setProductId(p.getId_product());
            dto.setProductName(p.getName());
            
            // Calculamos el precio actual con descuento en el momento
            int descuento = (p.getPrice() * p.getDiscount_percentage()) / 100;
            int precioActual = p.getPrice() - descuento;
            
            dto.setUnitPrice(precioActual);
            dto.setQuantity(item.getQuantity());
            dto.setSubtotal(precioActual * item.getQuantity());
        }
        return dto;
    }
}