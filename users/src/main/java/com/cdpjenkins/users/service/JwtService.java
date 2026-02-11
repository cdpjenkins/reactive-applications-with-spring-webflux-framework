package com.cdpjenkins.users.service;

public interface JwtService {

    String generateJwt(String subject);

}
