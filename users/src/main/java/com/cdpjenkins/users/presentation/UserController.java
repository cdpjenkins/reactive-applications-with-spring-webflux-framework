package com.cdpjenkins.users.presentation;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public Mono<ResponseEntity<UserRest>> createUser(@RequestBody @Valid Mono<CreateUserRequest> createUserRequest) {

        return createUserRequest.map(req ->
                new UserRest(UUID.randomUUID(), req.getFirstName(), req.getLastName(), req.getEmail())
        ).map( user -> ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/users/" + user.getId()))
                .body(user));
    }

    @GetMapping("/{userId}")
    public Mono<UserRest> getUYser(@PathVariable UUID userId) {
        return Mono.just(new UserRest(userId, "hardcoded firstName", "hardcoded lastName", "hardcoded email"));
    }
}
