package com.uade.tpo.marketplace.service;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.exceptions.UserDuplicateException;
import com.uade.tpo.marketplace.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public Page<User> getUsers(PageRequest pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    
    public User createUser(String username, String name, String surname, String email, String password, String role) throws UserDuplicateException {
        
       if (userRepository.findByUsername(username).isEmpty()) {
            User user = new User(username, name, surname, email, password, role);
            return userRepository.save(user);
        }
        throw new UserDuplicateException();
    }
}