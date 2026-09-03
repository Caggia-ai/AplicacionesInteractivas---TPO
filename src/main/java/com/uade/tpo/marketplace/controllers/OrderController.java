package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.marketplace.entity.Order;
import com.uade.tpo.marketplace.entity.dto.OrderRequest;
import com.uade.tpo.marketplace.entity.dto.OrderResponse;
import com.uade.tpo.marketplace.service.OrderService;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUser(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        // Mapeamos la lista completa de entidades a DTOs usando Streams de Java
        List<OrderResponse> responseList = orders.stream()
                                                 .map(OrderResponse::fromEntity)
                                                 .toList();
                                                 
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        Optional<Order> order = orderService.getOrderById(orderId);
        
        return order.map(o -> ResponseEntity.ok(OrderResponse.fromEntity(o)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }
}