package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SignupRequestTest {

    private Validator validator;
    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        signupRequest = new SignupRequest();
        signupRequest.setEmail("yoga@studio.com");
        signupRequest.setFirstName("Yoga");
        signupRequest.setLastName("Studio");
        signupRequest.setPassword("test!1234");
    }

    @Test
    void shouldCreateSignupRequestWithValidAttributes() {
        assertThat(signupRequest.getEmail()).isEqualTo("yoga@studio.com");
        assertThat(signupRequest.getFirstName()).isEqualTo("Yoga");
        assertThat(signupRequest.getLastName()).isEqualTo("Studio");
        assertThat(signupRequest.getPassword()).isEqualTo("test!1234");
    }

    @Test
    void shouldFailValidationWhenEmailIsBlank() {
        signupRequest.setEmail("");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void shouldFailValidationWhenEmailIsInvalid() {
        signupRequest.setEmail("invalid-email");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void shouldFailValidationWhenFirstNameIsBlank() {
        signupRequest.setFirstName("");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @Test
    void shouldFailValidationWhenFirstNameIsTooShort() {
        signupRequest.setFirstName("Jo");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @Test
    void shouldFailValidationWhenFirstNameIsTooLong() {
        signupRequest.setFirstName("ThisFirstNameIsWayTooLong");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @Test
    void shouldFailValidationWhenLastNameIsBlank() {
        signupRequest.setLastName("");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
    }

    @Test
    void shouldFailValidationWhenLastNameIsTooShort() {
        signupRequest.setLastName("Do");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
    }

    @Test
    void shouldFailValidationWhenLastNameIsTooLong() {
        signupRequest.setLastName("ThisLastNameIsWayTooLong");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
    }

    @Test
    void shouldFailValidationWhenPasswordIsBlank() {
        signupRequest.setPassword("");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void shouldFailValidationWhenPasswordIsTooShort() {
        signupRequest.setPassword("short");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void shouldFailValidationWhenPasswordIsTooLong() {
        signupRequest.setPassword("ThisPasswordIsWayTooLongToBeAcceptedAndShouldFail");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void shouldPassValidationWithValidAttributes() {
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    void equals_ShouldReturnTrue_ForSameAttributes() {
        SignupRequest anotherSignupRequest = new SignupRequest();
        anotherSignupRequest.setEmail("yoga@studio.com");
        anotherSignupRequest.setFirstName("Yoga");
        anotherSignupRequest.setLastName("Studio");
        anotherSignupRequest.setPassword("test!1234");

        assertThat(signupRequest).isEqualTo(anotherSignupRequest);
    }

    @Test
    void equals_ShouldReturnFalse_ForDifferentAttributes() {
        SignupRequest differentSignupRequest = new SignupRequest();
        differentSignupRequest.setEmail("other@example.com");
        differentSignupRequest.setFirstName("Jane");
        differentSignupRequest.setLastName("Doe");
        differentSignupRequest.setPassword("differentPassword");

        assertThat(signupRequest).isNotEqualTo(differentSignupRequest);
    }

    @Test
    void hashCode_ShouldBeEqual_ForSameAttributes() {
        SignupRequest anotherSignupRequest = new SignupRequest();
        anotherSignupRequest.setEmail("yoga@studio.com");
        anotherSignupRequest.setFirstName("Yoga");
        anotherSignupRequest.setLastName("Studio");
        anotherSignupRequest.setPassword("test!1234");

        assertThat(signupRequest.hashCode()).isEqualTo(anotherSignupRequest.hashCode());
    }

    @Test
    void toString_ShouldIncludeAllAttributes() {
        String signupRequestString = signupRequest.toString();

        assertThat(signupRequestString).contains("yoga@studio.com", "Yoga", "Studio", "test!1234");
    }
}
