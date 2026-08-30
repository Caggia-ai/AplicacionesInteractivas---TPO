package com.uade.tpo.marketplace.entity.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String paymentMethod;
    private String deliveryMethod;
}