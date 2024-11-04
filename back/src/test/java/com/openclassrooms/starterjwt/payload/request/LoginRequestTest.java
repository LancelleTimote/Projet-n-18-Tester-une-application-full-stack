package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestTest {

    private Validator validator;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");
    }

    @Test
    void shouldCreateLoginRequestWithValidAttributes() {
        assertThat(loginRequest.getEmail()).isEqualTo("yoga@studio.com");
        assertThat(loginRequest.getPassword()).isEqualTo("test!1234");
    }

    @Test
    void shouldFailValidationWhenEmailIsBlank() {
        loginRequest.setEmail("");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void shouldFailValidationWhenPasswordIsBlank() {
        loginRequest.setPassword("");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void shouldPassValidationWithValidEmailAndPassword() {
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertThat(violations).isEmpty();
    }
}
