package com.cdpjenkins.users.presentation;

import com.cdpjenkins.users.presentation.model.AuthenticationRequest;
import com.cdpjenkins.users.service.AuthenticationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AuthenticationController {

    final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Object>> login(@RequestBody Mono<AuthenticationRequest> authenticationRequestMono) {
        return authenticationRequestMono
                .flatMap(authenticationRequest ->
                        authenticationService.authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword()))
                .map(authResultMap ->
                        ResponseEntity.ok()
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authResultMap.get("token"))
                                .header("UserId", authResultMap.get("userId"))
                                .build());
    }
}
