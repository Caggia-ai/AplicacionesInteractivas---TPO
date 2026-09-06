package com.uade.tpo.marketplace.exceptions;
 
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
 
// RuntimeException (no checked) a propósito: así no hace falta agregar "throws"
// en UserService, UserController, AuthenticationService ni AuthController.
// @ResponseStatus igual hace que Spring la traduzca a 400 Bad Request automáticamente.
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El rol enviado no es válido. Roles permitidos: BUYER, SELLER, ADMIN")
public class InvalidRoleException extends RuntimeException {
 
}
