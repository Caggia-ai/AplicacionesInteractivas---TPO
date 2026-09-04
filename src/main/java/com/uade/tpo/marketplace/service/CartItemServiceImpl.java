package com.uade.tpo.marketplace.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.CartItem;
import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.repository.CartItemRepository;
import com.uade.tpo.marketplace.repository.CartRepository;
import com.uade.tpo.marketplace.repository.ProductRepository;


@Service
public class CartItemServiceImpl implements CartItemService {
@Autowired private CartItemRepository cartItemRepository;
@Autowired private CartRepository cartRepository;
@Autowired private ProductRepository productRepository;

@Transactional
public Cart addItemToCart(Long userId, Long productId, int quantityToAdd) {
    if (quantityToAdd <= 0) {
        throw new RuntimeException("La cantidad a agregar debe ser mayor a cero.");
    }

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    if (!product.isState()) {
        throw new RuntimeException("El producto ya no está disponible.");
    }
    Cart cart = cartRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("El usuario no tiene carrito"));

    CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId_cart(), productId)
        .orElseGet(() -> {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(0);
            newItem.setUnit_price(product.getPrice());
            return newItem;
        });

    int newQuantity = item.getQuantity() + quantityToAdd;
    // Antes no se validaba el stock acá: se podía cargar el carrito con más
    // unidades de las que había en stock y el error recién aparecía al comprar.
    if (newQuantity > product.getStock()) {
        throw new RuntimeException("No hay suficiente stock de " + product.getName()
            + " (disponible: " + product.getStock() + ").");
    }

    item.setQuantity(newQuantity);
    cartItemRepository.save(item);

    cart.setTotal(cart.getTotal() + (item.getUnit_price() * quantityToAdd));
    return cartRepository.save(cart);
}

@Transactional
public Optional<CartItem> removeItemFromCart(Long userId, Long productId) {
    
    Cart cart = cartRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("Error crítico: el usuario no tiene carrito asignado"));
        
    CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId_cart(), productId)
        .orElseThrow(() -> new RuntimeException("Error crítico: el producto no está en el carrito"));
        
    cart.setTotal(cart.getTotal() - item.getUnit_price());
    cartRepository.save(cart);

    item.setQuantity(item.getQuantity() - 1); 

    if (item.getQuantity() <= 0) {
        cartItemRepository.delete(item); 
        return Optional.empty(); 
    } 
    
    cartItemRepository.save(item);
    return Optional.of(item);
}
    
}
