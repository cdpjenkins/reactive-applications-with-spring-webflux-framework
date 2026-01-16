package com.cdpjenkins.users.presentation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {
    //     "firstName": "Mr",
    //    "lastName": "Cat",
    //    "email": "mrcat@example.com",
    //    "password": "12345678"

    @NotBlank(message = "firstName cannot be empty")
    @Size(min = 2, max = 50, message = "firstName must be between 2 and 50 characters")
    @NotNull
    private String firstName;

    @NotBlank(message = "lastName cannot be empty")
    @Size(min = 2, max = 50, message = "lastName must be between 2 and 50 characters")
    @NotNull
    private String lastName;

    @NotBlank(message = "email cannot be empty")
    @Email(message = "email must be a valid email address")
    private String email;

    @NotBlank(message = "password cannot be empty")
    @Size(min = 8, max = 16, message = "password must be between 8 and 16 characters")
    private String password;

    public CreateUserRequest() {}

    public CreateUserRequest(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
