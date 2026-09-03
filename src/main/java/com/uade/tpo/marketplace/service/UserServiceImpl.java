package com.uade.tpo.marketplace.service;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.exceptions.UserDuplicateException;
import com.uade.tpo.marketplace.repository.CartRepository;
import com.uade.tpo.marketplace.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired 
    private CartRepository cartRepository;

    public Page<User> getUsers(PageRequest pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public User createUser(String username, String name, String surname, String email, String password, String role) throws UserDuplicateException {
        
       if (userRepository.findByUsername(username).isEmpty()) {
            User user = new User(username, name, surname, email, password, role);
            User savedUser = userRepository.save(user);

            Cart cart = new Cart();
            cart.setUser(savedUser);
            cart.setTotal(0);
            cart.setState(true);
            cartRepository.save(cart);
            
            return savedUser;
        }
        throw new UserDuplicateException();
    }

    @Override
    public User updateUser(Long userId, String username, String name, String surname, String email, String password, String role) throws UserDuplicateException {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Optional<User> existingUser = userRepository.findByUsername(username);
    
        if (existingUser.isPresent() && !existingUser.get().getId_user().equals(userId)) {
            throw new UserDuplicateException();
        }

        user.setUsername(username);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        
        return userRepository.save(user);
    }
}
