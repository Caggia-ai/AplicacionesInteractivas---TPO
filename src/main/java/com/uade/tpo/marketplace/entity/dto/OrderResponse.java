package com.uade.tpo.marketplace.entity.dto;

import com.uade.tpo.marketplace.entity.Order;
import java.time.LocalDate;
import lombok.Data;

@Data
public class OrderResponse {
    private Long id;
    private String paymentMethod;
    private String deliveryMethod;
    private LocalDate date;
    private int total;
    private String buyerUsername;

    public static OrderResponse fromEntity(Order order) {
        OrderResponse dto = new OrderResponse();
        dto.setId(order.getId_order());
        dto.setPaymentMethod(order.getPayment_method());
        dto.setDeliveryMethod(order.getDelivery_method());
        dto.setDate(order.getDate());
        dto.setTotal(order.getTotal());
        
        if (order.getUser() != null) {
            dto.setBuyerUsername(order.getUser().getUsername());
        }
        return dto;
    }
}