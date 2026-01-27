package com.cdpjenkins.users.service;

import com.cdpjenkins.users.presentation.model.CreateUserRequest;
import com.cdpjenkins.users.presentation.model.UserRest;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService extends ReactiveUserDetailsService {
    Mono<UserRest> createUser(Mono<CreateUserRequest> createUserRequestMono);
    Mono<UserRest> getUserById(UUID userId);
    Flux<UserRest> getAllUsers(int start, int limit);
}
