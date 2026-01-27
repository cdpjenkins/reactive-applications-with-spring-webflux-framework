package com.cdpjenkins.users.service;

import com.cdpjenkins.users.data.UserEntity;
import com.cdpjenkins.users.data.UserRepository;
import com.cdpjenkins.users.presentation.model.CreateUserRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final ReactiveAuthenticationManager reactiveAuthenticationManager;
    private final UserRepository userRepository;

    public AuthenticationServiceImpl(ReactiveAuthenticationManager reactiveAuthenticationManager, UserRepository userRepository) {
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
        this.userRepository = userRepository;
    }

    @Override
    public Mono<Map<String, String>> authenticate(String username, String password) {
        return reactiveAuthenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password))
                .then(getUserDetails(username))
                .map(this::createAuthResponse);
    }

    private Mono<UserEntity> getUserDetails(String username) {
        return userRepository.findByEmail(username);
    }

    private Map<String, String> createAuthResponse(UserEntity userEntity) {
        HashMap<String, String> result = new HashMap<>();
        result.put("userId", userEntity.getId().toString());
        result.put("token", "JWT");                                 // this needs to be replaced with actual JWT

        return result;
    }
}
