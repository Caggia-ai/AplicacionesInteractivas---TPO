package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.marketplace.entity.Order;
import com.uade.tpo.marketplace.entity.dto.OrderRequest;
import com.uade.tpo.marketplace.service.OrderService;

@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout/user/{userId}")
    public ResponseEntity<Order> checkout(@PathVariable Long userId, @RequestBody OrderRequest request) {
        Order order = orderService.createOrderFromCart(userId, request.getPaymentMethod(), request.getDeliveryMethod());
        return ResponseEntity.ok(order);
    }
}