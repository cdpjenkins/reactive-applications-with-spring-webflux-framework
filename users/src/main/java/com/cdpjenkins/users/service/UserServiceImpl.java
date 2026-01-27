package com.cdpjenkins.users.service;

import com.cdpjenkins.users.data.UserEntity;
import com.cdpjenkins.users.data.UserRepository;
import com.cdpjenkins.users.presentation.model.CreateUserRequest;
import com.cdpjenkins.users.presentation.model.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Collections;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<UserRest> createUser(Mono<CreateUserRequest> createUserRequestMono) {
        return createUserRequestMono
                .flatMap(this::convertToEntity)
                .flatMap(userRepository::save)
                .mapNotNull(UserServiceImpl::convertToRest);
    }

    @Override
    public Mono<UserRest> getUserById(UUID userId) {
        return userRepository
                .findById(userId)
                .mapNotNull(UserServiceImpl::convertToRest);
    }

    @Override
    public Flux<UserRest> getAllUsers(int start, int limit) {
        PageRequest pageable = PageRequest.of(start, limit);
        return userRepository
                .findAllBy(pageable)
                .map(UserServiceImpl::convertToRest);
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByEmail(username)
                .map(userEntity -> User
                        .withUsername(userEntity.getEmail())
                        .password(userEntity.getPassword())
                        .authorities(Collections.emptyList())
                        .build()
                );
    }

    private Mono<UserEntity> convertToEntity(CreateUserRequest req) {
        // Hashing the password is CPU-intensive... why are we bothering to do this?
        return Mono.fromCallable( () -> {
            UserEntity entity = new UserEntity();
            BeanUtils.copyProperties(req, entity);
            String unhashedPassword = req.getPassword();
            String hashedPassword = passwordEncoder.encode(unhashedPassword);
            entity.setPassword(hashedPassword);
            entity.setPassword(hashedPassword);
            return entity;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private static UserRest convertToRest(UserEntity entity) {
        UserRest rest = new UserRest();
        BeanUtils.copyProperties(entity, rest);
        return rest;
    }
}
