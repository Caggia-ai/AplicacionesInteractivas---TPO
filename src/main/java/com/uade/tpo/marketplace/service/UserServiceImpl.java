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
import com.uade.tpo.marketplace.exceptions.InvalidRoleException;
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
        
       // Verificamos que no exista ni el username ni el email en la base de datos
       if (userRepository.findByUsername(username).isPresent() || userRepository.findByEmail(email).isPresent()) {
            throw new UserDuplicateException();
       }
            
        // Convertimos el string a Enum de forma segura (pasando a mayúsculas)
        Role userRole = parseRole(role);
        
        // La contraseña se guarda siempre encriptada con BCrypt
        User user = new User(username, name, surname, email, passwordEncoder.encode(password), userRole);
        User savedUser = userRepository.save(user);

        Cart cart = new Cart();
        cart.setUser(savedUser);
        cart.setState(true);
        cartRepository.save(cart);
        
        return savedUser;
    }

    @Override
    public User patchUser(Long userId, UserPatchRequest request, User currentUser) throws UserDuplicateException {
        
        // Validación de propiedad y rol general
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
        
        if (request.getEmail() != null) {
            Optional<User> existingEmail = userRepository.findByEmail(request.getEmail());
            if (existingEmail.isPresent() && !existingEmail.get().getId_user().equals(userId)) {
                throw new UserDuplicateException();
            }
            user.setEmail(request.getEmail());
        }

        if (request.getName() != null) user.setName(request.getName());
        if (request.getSurname() != null) user.setSurname(request.getSurname());
        if (request.getPassword() != null) user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // NUEVA VALIDACIÓN DE SEGURIDAD: Solo un ADMIN puede cambiar roles
        if (request.getRole() != null) {
            if (!currentUser.getRole().name().equals("ADMIN")) {
                throw new AccessDeniedException("No tienes permisos para cambiar el rol de la cuenta");
            }
            user.setRole(parseRole(request.getRole()));
        }
        
        return userRepository.save(user);
    }
    
    private Role parseRole(String role) {
        if (role == null || role.isBlank()) {
            throw new InvalidRoleException();
        }
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException();
        }
    }
}