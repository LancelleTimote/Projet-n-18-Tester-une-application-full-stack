package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private Validator validator;
    private User user;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        user = User.builder()
                .email("yoga@studio.com")
                .firstName("Yoga")
                .lastName("Studio")
                .password("test!1234")
                .admin(false)
                .build();
    }

    @Test
    void shouldCreateUserWithValidAttributes() {
        assertThat(user.getEmail()).isEqualTo("yoga@studio.com");
        assertThat(user.getFirstName()).isEqualTo("Yoga");
        assertThat(user.getLastName()).isEqualTo("Studio");
        assertThat(user.getPassword()).isEqualTo("test!1234");
        assertThat(user.isAdmin()).isFalse();
    }

    @Test
    void shouldCreateUserWithRequiredConstructor() {
        User userWithConstructor = new User("toto3@toto.com", "toto", "toto", "test!1234", true);
        assertThat(userWithConstructor.getEmail()).isEqualTo("toto3@toto.com");
        assertThat(userWithConstructor.getFirstName()).isEqualTo("toto");
        assertThat(userWithConstructor.getLastName()).isEqualTo("toto");
        assertThat(userWithConstructor.getPassword()).isEqualTo("test!1234");
        assertThat(userWithConstructor.isAdmin()).isTrue();
    }

    @Test
    void shouldSetCreatedAtSuccessfully() {
        LocalDateTime createdAt = LocalDateTime.now();
        user.setCreatedAt(createdAt);
        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void shouldSetUpdatedAtSuccessfully() {
        LocalDateTime updatedAt = LocalDateTime.now();
        user.setUpdatedAt(updatedAt);
        assertThat(user.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparingWithDifferentObject() {
        Object otherObject = new Object();
        assertThat(user.equals(otherObject)).isFalse();
    }

    @Test
    void shouldFailValidationWhenPasswordExceedsMaxSize() {
        user.setPassword("a".repeat(121));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void setId_ShouldSetIdCorrectly() {
        user.setId(123L);
        assertThat(user.getId()).isEqualTo(123L);
    }

    @Test
    void shouldFailValidationWhenEmailIsInvalid() {
        user.setEmail("invalid-email");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void shouldFailValidationWhenFirstNameExceedsSize() {
        user.setFirstName("ThisNameIsWayTooLongForTheField");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @Test
    void createdAt_ShouldNotBeNull_WhenUserIsPersisted() {
        LocalDateTime createdAt = LocalDateTime.now();
        ReflectionTestUtils.setField(user, "createdAt", createdAt);

        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void updatedAt_ShouldUpdate_WhenUserIsUpdated() {
        LocalDateTime initialUpdate = LocalDateTime.now();
        ReflectionTestUtils.setField(user, "updatedAt", initialUpdate);
        assertThat(user.getUpdatedAt()).isEqualTo(initialUpdate);

        LocalDateTime newUpdate = initialUpdate.plusDays(1);
        ReflectionTestUtils.setField(user, "updatedAt", newUpdate);

        assertThat(user.getUpdatedAt()).isEqualTo(newUpdate);
    }

    @Test
    void shouldSetEmailSuccessfully() {
        user.setEmail("newemail@example.com");
        assertThat(user.getEmail()).isEqualTo("newemail@example.com");
    }

    @Test
    void shouldSetFirstNameSuccessfully() {
        user.setFirstName("NewFirstName");
        assertThat(user.getFirstName()).isEqualTo("NewFirstName");
    }

    @Test
    void shouldSetLastNameSuccessfully() {
        user.setLastName("NewLastName");
        assertThat(user.getLastName()).isEqualTo("NewLastName");
    }

    @Test
    void shouldSetPasswordSuccessfully() {
        user.setPassword("newpassword");
        assertThat(user.getPassword()).isEqualTo("newpassword");
    }

    @Test
    void shouldSetAdminFlagSuccessfully() {
        user.setAdmin(true);
        assertThat(user.isAdmin()).isTrue();
    }

    @Test
    void toString_ShouldIncludeRelevantFields() {
        String userString = user.toString();
        assertThat(userString).contains("yoga@studio.com", "Yoga", "Studio", "test!1234");
    }

    @Test
    void equalsAndHashCode_ShouldWorkCorrectly() {
        User user1 = new User("yoga@studio.com", "Studio", "Yoga", "test!1234", false);
        User user2 = new User("yoga@studio.com", "Studio", "Yoga", "test!1234", false);

        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    void canEqual_ShouldReturnTrueForSameClass() {
        User anotherUser = new User("test@example.com", "Test", "User", "password", true);
        assertThat(user.canEqual(anotherUser)).isTrue();
    }

    @Test
    void canEqual_ShouldReturnFalseForDifferentClass() {
        assertThat(user.canEqual(new Object())).isFalse();
    }

    @Test
    void setId_ShouldUpdateIdCorrectly() {
        user.setId(10L);
        assertThat(user.getId()).isEqualTo(10L);
    }
}
