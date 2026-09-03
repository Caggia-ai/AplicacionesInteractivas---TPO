package com.uade.tpo.marketplace.controllers;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.dto.UserRequest;
import com.uade.tpo.marketplace.entity.dto.UserResponse;
import com.uade.tpo.marketplace.entity.dto.UserPatchRequest;
import com.uade.tpo.marketplace.exceptions.UserDuplicateException;
import com.uade.tpo.marketplace.service.UserService;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        
        PageRequest pageRequest = (page == null || size == null) 
            ? PageRequest.of(0, Integer.MAX_VALUE) 
            : PageRequest.of(page, size);
            
        Page<UserResponse> userPage = userService.getUsers(pageRequest)
                                                 .map(UserResponse::fromEntity);
        return ResponseEntity.ok(userPage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        Optional<User> result = userService.getUserById(userId);
        
        return result.map(user -> ResponseEntity.ok(UserResponse.fromEntity(user)))
                     .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserRequest request) throws UserDuplicateException{
        User result = userService.createUser(
            request.getUsername(),
            request.getName(),
            request.getSurname(),
            request.getEmail(),
            request.getPassword(),
            request.getRole()
        );
        return ResponseEntity.created(URI.create("/users/" + result.getId_user())).body(result);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<User> patchUser(@PathVariable Long userId, @RequestBody UserPatchRequest request) throws UserDuplicateException{
        User result = userService.patchUser(userId, request);
        return ResponseEntity.ok(result);
    }
}
