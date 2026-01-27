package com.cdpjenkins.users.presentation;

import com.cdpjenkins.users.presentation.model.AuthenticationRequest;
import com.cdpjenkins.users.service.AuthenticationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class AuthenticationController {

    final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<ResponseEntity>> login(@RequestBody Mono<AuthenticationRequest> authenticationRequestMono) {
        return authenticationRequestMono
                .flatMap(authenticationRequest ->
                        authenticationService.authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword()))
                .map(authResultMap ->
                        ResponseEntity.ok()
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authResultMap.get("JWT"))
                                .header("UserId", authResultMap.get("userId"))
                                .build());
    }
}
