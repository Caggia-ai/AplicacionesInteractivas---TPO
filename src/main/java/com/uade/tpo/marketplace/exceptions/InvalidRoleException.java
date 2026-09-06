package com.uade.tpo.marketplace.exceptions;
 
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
 
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El rol enviado no es válido. Roles permitidos: BUYER, SELLER, ADMIN")
public class InvalidRoleException extends RuntimeException {
 
}
