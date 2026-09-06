package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.marketplace.entity.Order;
import com.uade.tpo.marketplace.entity.OrderItem;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.dto.OrderItemResponse;
import com.uade.tpo.marketplace.service.OrderItemService;
import com.uade.tpo.marketplace.service.OrderService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("orderItems")
public class OrderItemController {

    @Autowired private OrderItemService orderItemService;
    @Autowired private OrderService orderService; // Necesario para validar la orden

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemResponse>> getItemsByOrderId(
            @PathVariable Long orderId,
            @AuthenticationPrincipal User user) { 
        
        Optional<Order> orderOpt = orderService.getOrderById(orderId);
        if (orderOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        // Validación de propiedad
        if (!orderOpt.get().getUser().getId_user().equals(user.getId_user()) 
            && !user.getRole().name().equals("ADMIN")) {
            return ResponseEntity.status(403).build();
        }

        List<OrderItem> items = orderItemService.getItemsByOrderId(orderId);
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        List<OrderItemResponse> responseList = items.stream()
                                                    .map(OrderItemResponse::fromEntity)
                                                    .toList();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<OrderItemResponse> getItemById(
            @PathVariable Long itemId,
            @AuthenticationPrincipal User user) { 
        
        Optional<OrderItem> itemOpt = orderItemService.getItemById(itemId);
        
        if (itemOpt.isPresent()) {
            OrderItem item = itemOpt.get();
            // Verificamos si la orden a la que pertenece el ítem es del usuario logueado
            if (!item.getOrder().getUser().getId_user().equals(user.getId_user()) 
                && !user.getRole().name().equals("ADMIN")) {
                return ResponseEntity.status(403).build();
            }
            return ResponseEntity.ok(OrderItemResponse.fromEntity(item));
        }
        return ResponseEntity.notFound().build();
    }
}