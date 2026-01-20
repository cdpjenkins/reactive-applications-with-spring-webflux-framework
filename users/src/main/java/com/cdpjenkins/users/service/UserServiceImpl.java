package com.cdpjenkins.users.service;

import com.cdpjenkins.users.data.UserEntity;
import com.cdpjenkins.users.data.UserRepository;
import com.cdpjenkins.users.presentation.CreateUserRequest;
import com.cdpjenkins.users.presentation.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RequestedContentTypeResolver requestedContentTypeResolver;

    public UserServiceImpl(UserRepository userRepository, RequestedContentTypeResolver requestedContentTypeResolver) {
        this.userRepository = userRepository;
        this.requestedContentTypeResolver = requestedContentTypeResolver;
    }

    @Override
    public Mono<UserRest> createUser(Mono<CreateUserRequest> createUserRequestMono) {
        return createUserRequestMono
                .mapNotNull(UserServiceImpl::convertToEntity)
                .flatMap(userRepository::save)
                .mapNotNull(UserServiceImpl::convertToRest)
                .onErrorMap(throwable ->
                        switch (throwable) {
                            case DuplicateKeyException ignored -> new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
                            case DataIntegrityViolationException ignored -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data integrity violation");
                            default -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
                        }
                );
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

    private static UserEntity convertToEntity(CreateUserRequest req) {
        UserEntity entity = new UserEntity();
        BeanUtils.copyProperties(req, entity);
        return entity;
    }

    private static UserRest convertToRest(UserEntity entity) {
        UserRest rest = new UserRest();
        BeanUtils.copyProperties(entity, rest);
        return rest;
    }
}
