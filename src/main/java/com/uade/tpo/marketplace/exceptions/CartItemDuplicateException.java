package com.uade.tpo.marketplace.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El producto en el carrito que se intenta agregar esta duplicado")
public class CartItemDuplicateException extends Exception {
    
}
