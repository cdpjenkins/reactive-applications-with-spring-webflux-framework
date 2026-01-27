package com.cdpjenkins.users.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DuplicateKeyException.class)
    public Mono<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException ex) {
        return Mono.just(ErrorResponse.builder(ex, HttpStatus.CONFLICT, ex.getMessage()).build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Mono<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return Mono.just(ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage()).build());
    }

    @ExceptionHandler(Exception.class)
    public Mono<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("Unexpected error occurred", ex);

        return Mono.just(ErrorResponse.builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred").build());
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ErrorResponse> handleWebExchangeBindException(WebExchangeBindException ex) {
        String errors = allErrorsFrom(ex);

        return Mono.just(ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, errors).build());
    }

    @NonNull
    private static String allErrorsFrom(WebExchangeBindException exception) {
        return exception.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
    }
}
