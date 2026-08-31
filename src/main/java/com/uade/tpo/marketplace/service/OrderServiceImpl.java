package com.uade.tpo.marketplace.service;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uade.tpo.marketplace.entity.*;
import com.uade.tpo.marketplace.repository.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;

    @Override
    public Order createOrderFromCart(Long userId, String paymentMethod, String deliveryMethod) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("El usuario no tiene carrito activo"));

        if (cart.getProductosCarrito() == null || cart.getProductosCarrito().isEmpty()) {
            throw new RuntimeException("No se puede comprar con el carrito vacío");
        }

        Order order = new Order();
        order.setUser(user);
        order.setPayment_method(paymentMethod);
        order.setDelivery_method(deliveryMethod);
        order.setDate(LocalDate.now().toString()); 
        order.setTotal(cart.getTotal());
        order = orderRepository.save(order);

        for (CartItem cartItem : cart.getProductosCarrito()) {
            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Sin stock suficiente para el producto: " + product.getName());
            }

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnit_price(cartItem.getUnit_price());
            orderItemRepository.save(orderItem);
        }

        cartItemRepository.deleteAll(cart.getProductosCarrito());
        cart.setTotal(0);
        cartRepository.save(cart);

        return order;
    }
}