package com.cdpjenkins.users.presentation;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public Mono<UserRest> createUser(@RequestBody @Valid Mono<CreateUserRequest> createUserRequest) {

        return createUserRequest.map(req ->
                new UserRest(UUID.randomUUID(), req.getFirstName(), req.getLastName(), req.getEmail())
        );
    }
}
