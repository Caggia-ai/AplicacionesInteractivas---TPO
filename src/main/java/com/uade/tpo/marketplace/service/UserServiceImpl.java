package com.uade.tpo.marketplace.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.Role;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.dto.UserPatchRequest;
import com.uade.tpo.marketplace.exceptions.UserDuplicateException;
import com.uade.tpo.marketplace.repository.CartRepository;
import com.uade.tpo.marketplace.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<User> getUsers(PageRequest pageable) {
        return userRepository.findAll(pageable);
    }
    
    @Override 
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override 
    public User createUser(String username, String name, String surname, String email, String password, String role) throws UserDuplicateException {
        
       if (userRepository.findByUsername(username).isEmpty()) {
            
            // Convertimos el string a Enum de forma segura (pasando a mayúsculas)
            Role userRole = Role.valueOf(role.toUpperCase());
            
            // La contraseña se guarda siempre encriptada con BCrypt
            User user = new User(username, name, surname, email, passwordEncoder.encode(password), userRole);
            User savedUser = userRepository.save(user);

            Cart cart = new Cart();
            cart.setUser(savedUser);
            cart.setState(true);
            cartRepository.save(cart);
            
            return savedUser;
        }
        throw new UserDuplicateException();
    }

    @Override
    public User patchUser(Long userId, UserPatchRequest request, User currentUser) throws UserDuplicateException {
        
        // Validación de propiedad y rol
        if (!userId.equals(currentUser.getId_user()) && !currentUser.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("No tienes permisos para modificar este perfil");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (request.getUsername() != null) {
            Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
            if (existingUser.isPresent() && !existingUser.get().getId_user().equals(userId)) {
                throw new UserDuplicateException();
            }
            user.setUsername(request.getUsername());
        }
        
        if (request.getName() != null) user.setName(request.getName());
        if (request.getSurname() != null) user.setSurname(request.getSurname());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPassword() != null) user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getRole() != null) user.setRole(Role.valueOf(request.getRole().toUpperCase()));
        
        return userRepository.save(user);
    }
}