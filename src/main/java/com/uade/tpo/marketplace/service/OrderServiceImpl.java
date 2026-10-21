package com.uade.tpo.marketplace.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public Order createOrderFromCart(Long userId, String paymentMethod, String deliveryMethod) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("El usuario no tiene carrito activo"));

        if (cart.getProductosCarrito() == null || cart.getProductosCarrito().isEmpty()) {
            throw new RuntimeException("No se puede comprar con el carrito vacío");
        }

        // 1. Filtramos solo los ítems cuyos productos siguen activos (state == true)
        List<CartItem> validItems = cart.getProductosCarrito().stream()
            .filter(ci -> ci.getProduct() != null && ci.getProduct().isState())
            .toList();

        // 2. Si todos los productos del carrito fueron eliminados, vaciamos el carrito y frenamos la orden
        if (validItems.isEmpty()) {
            cartItemRepository.deleteAll(cart.getProductosCarrito());
            throw new RuntimeException("Los productos de tu carrito ya no están disponibles.");
        }

        // 3. Validamos el stock antes de crear la orden
        for (CartItem cartItem : validItems) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Sin stock suficiente para el producto: " + product.getName());
            }
        }

        // 4. Recién acá creamos la orden porque sabemos que hay al menos 1 producto válido
        Order order = new Order();
        order.setUser(user);
        order.setPayment_method(paymentMethod);
        order.setDelivery_method(deliveryMethod);
        order.setDate(LocalDate.now());
        order = orderRepository.save(order);

        int totalDeLaOrden = 0;

        for (CartItem cartItem : validItems) {
            Product product = cartItem.getProduct();

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            int descuento = (product.getPrice() * product.getDiscount_percentage()) / 100;
            int precioCongelado = product.getPrice() - descuento;

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnit_price(precioCongelado);
            orderItemRepository.save(orderItem);
            
            totalDeLaOrden += (precioCongelado * cartItem.getQuantity());
        }

        order.setTotal(totalDeLaOrden);
        orderRepository.save(order);

        // Limpiamos todos los ítems del carrito (válidos e inactivos)
        cartItemRepository.deleteAll(cart.getProductosCarrito());

        return order;
    }
    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
}
