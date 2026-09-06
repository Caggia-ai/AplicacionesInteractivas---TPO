package com.uade.tpo.marketplace.service;
import com.uade.tpo.marketplace.entity.dto.auth.AuthenticationRequest;
import com.uade.tpo.marketplace.entity.dto.auth.AuthenticationResponse;
import com.uade.tpo.marketplace.entity.dto.auth.RegisterRequest;
import com.uade.tpo.marketplace.exceptions.UserDuplicateException;


public interface AuthenticationService {
    public AuthenticationResponse register(RegisterRequest request) throws UserDuplicateException;
    public AuthenticationResponse authenticate(AuthenticationRequest request);
}
