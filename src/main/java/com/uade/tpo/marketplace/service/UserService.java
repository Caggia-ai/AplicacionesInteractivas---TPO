package com.uade.tpo.marketplace.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.dto.UserPatchRequest;
import com.uade.tpo.marketplace.exceptions.UserDuplicateException;

public interface UserService {
    public Page<User> getUsers(PageRequest pageRequest);
    public Optional<User> getUserById(Long userId);
    public User createUser(String username, String name, String surname, String email, String password, String role) throws UserDuplicateException;
    public User updateUser(Long userId, UserPatchRequest request) throws UserDuplicateException;
}
