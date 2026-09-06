package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.marketplace.entity.Order;
import com.uade.tpo.marketplace.entity.User;
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

    // Eliminamos el /user/{userId} de la ruta
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(
            @AuthenticationPrincipal User user, 
            @RequestBody OrderRequest request) {
        
        Order order = orderService.createOrderFromCart(
            user.getId_user(), 
            request.getPaymentMethod(), 
            request.getDeliveryMethod()
        );
        // Devolvemos el DTO
        return ResponseEntity.ok(OrderResponse.fromEntity(order)); 
    }

    // Al usar GET /orders, trae automáticamente el historial de quien hace la petición
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            @AuthenticationPrincipal User user) {
        
        List<Order> orders = orderService.getOrdersByUserId(user.getId_user());
        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        List<OrderResponse> responseList = orders.stream()
                                                 .map(OrderResponse::fromEntity)
                                                 .toList();
                                                 
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long orderId,
            @AuthenticationPrincipal User user) {
        
        Optional<Order> orderOpt = orderService.getOrderById(orderId);
        
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            
            // Verificamos que la orden pertenezca a quien la pide (o que sea ADMIN)
            if (!order.getUser().getId_user().equals(user.getId_user()) 
                && !user.getRole().name().equals("ADMIN")) {
                return ResponseEntity.status(403).build(); // 403 Forbidden
            }
            
            return ResponseEntity.ok(OrderResponse.fromEntity(order));
        }
        
        return ResponseEntity.notFound().build();
    }
}