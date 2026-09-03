package com.uade.tpo.marketplace.entity.dto;

import com.uade.tpo.marketplace.entity.OrderItem;
import lombok.Data;

@Data
public class OrderItemResponse {
    private Long id; // Recordá que lo pasaste a Long
    private String productName;
    private int unitPrice;
    private int quantity;
    private int subtotal;

    public static OrderItemResponse fromEntity(OrderItem item) {
        OrderItemResponse dto = new OrderItemResponse();
        dto.setId(item.getId_order_item()); // Si usaste getId_order_item() en lombok
        
        if (item.getProduct() != null) {
            dto.setProductName(item.getProduct().getName());
        }
        
        dto.setUnitPrice(item.getUnit_price());
        dto.setQuantity(item.getQuantity());
        dto.setSubtotal(item.getUnit_price() * item.getQuantity());
        return dto;
    }
}