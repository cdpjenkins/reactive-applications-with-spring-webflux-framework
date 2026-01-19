package com.cdpjenkins.users.service;

import com.cdpjenkins.users.presentation.CreateUserRequest;
import com.cdpjenkins.users.presentation.UserRest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {
    Mono<UserRest> createUser(Mono<CreateUserRequest> createUserRequestMono);
    Mono<UserRest> getUserById(UUID userId);
    Flux<UserRest> findAll(int start, int limit);
}
