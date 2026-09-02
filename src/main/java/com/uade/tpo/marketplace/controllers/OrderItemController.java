package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.marketplace.entity.OrderItem;
import com.uade.tpo.marketplace.service.OrderItemService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("orderItems")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    // Endpoint clave para el Frontend: Sirve para mostrar el detalle de una compra en el "Historial de Órdenes"
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItem>> getItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItem> items = orderItemService.getItemsByOrderId(orderId);
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(items);
    }

    // Endpoint para consultar un ítem específico por su ID
    @GetMapping("/{itemId}")
    public ResponseEntity<OrderItem> getItemById(@PathVariable Long itemId) {
        Optional<OrderItem> item = orderItemService.getItemById(itemId);
        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        }
        return ResponseEntity.notFound().build();
    }
}