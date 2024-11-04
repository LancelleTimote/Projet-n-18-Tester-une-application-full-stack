package com.openclassrooms.starterjwt.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoTest {

    private Validator validator;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("yoga@studio.com");
        userDto.setLastName("Studio");
        userDto.setFirstName("Yoga");
        userDto.setAdmin(true);
        userDto.setPassword("test!1234");
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void shouldCreateUserDtoWithValidAttributes() {
        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getEmail()).isEqualTo("yoga@studio.com");
        assertThat(userDto.getLastName()).isEqualTo("Studio");
        assertThat(userDto.getFirstName()).isEqualTo("Yoga");
        assertThat(userDto.isAdmin()).isTrue();
        assertThat(userDto.getPassword()).isEqualTo("test!1234");
        assertThat(userDto.getCreatedAt()).isNotNull();
        assertThat(userDto.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldFailValidationWhenEmailIsInvalid() {
        userDto.setEmail("invalid-email");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void shouldFailValidationWhenLastNameExceedsMaxSize() {
        userDto.setLastName("A".repeat(21));

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
    }

    @Test
    void shouldFailValidationWhenFirstNameExceedsMaxSize() {
        userDto.setFirstName("B".repeat(21));

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @Test
    void shouldFailValidationWhenPasswordExceedsMaxSize() {
        userDto.setPassword("C".repeat(121));

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void shouldPassValidationWithValidFields() {
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertThat(violations).isEmpty();
    }

    @Test
    void equalsAndHashCode_ShouldWorkCorrectly() {
        UserDto userDto1 = new UserDto(1L, "yoga@studio.com", "Studio", "Yoga", true, "test!1234", LocalDateTime.now(), LocalDateTime.now());
        UserDto userDto2 = new UserDto(1L, "yoga@studio.com", "Studio", "Yoga", true, "test!1234", LocalDateTime.now(), LocalDateTime.now());

        assertThat(userDto1).isEqualTo(userDto2);
        assertThat(userDto1.hashCode()).isEqualTo(userDto2.hashCode());
    }

    @Test
    void toString_ShouldIncludeRelevantFields() {
        String userDtoString = userDto.toString();
        assertThat(userDtoString).contains("yoga@studio.com", "Studio", "Yoga", "true");
    }
}
