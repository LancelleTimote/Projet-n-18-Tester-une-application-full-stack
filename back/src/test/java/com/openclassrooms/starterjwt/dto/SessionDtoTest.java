package com.openclassrooms.starterjwt.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SessionDtoTest {

    private Validator validator;
    private SessionDto sessionDto;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Session 1");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(2L);
        sessionDto.setDescription("My description");
        sessionDto.setUsers(Collections.singletonList(1L));
        sessionDto.setCreatedAt(LocalDateTime.now());
        sessionDto.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void shouldCreateSessionDtoWithValidAttributes() {
        assertThat(sessionDto.getId()).isEqualTo(1L);
        assertThat(sessionDto.getName()).isEqualTo("Session 1");
        assertThat(sessionDto.getDate()).isNotNull();
        assertThat(sessionDto.getTeacher_id()).isEqualTo(2L);
        assertThat(sessionDto.getDescription()).isEqualTo("My description");
        assertThat(sessionDto.getUsers()).containsExactly(1L);
        assertThat(sessionDto.getCreatedAt()).isNotNull();
        assertThat(sessionDto.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldFailValidationWhenNameIsBlank() {
        sessionDto.setName("");

        Set<ConstraintViolation<SessionDto>> violations = validator.validate(sessionDto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void shouldFailValidationWhenDescriptionExceedsMaxSize() {
        String longDescription = "A".repeat(2501);
        sessionDto.setDescription(longDescription);

        Set<ConstraintViolation<SessionDto>> violations = validator.validate(sessionDto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("description"));
    }

    @Test
    void shouldFailValidationWhenDateIsNull() {
        sessionDto.setDate(null);

        Set<ConstraintViolation<SessionDto>> violations = validator.validate(sessionDto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("date"));
    }

    @Test
    void shouldFailValidationWhenTeacherIdIsNull() {
        sessionDto.setTeacher_id(null);

        Set<ConstraintViolation<SessionDto>> violations = validator.validate(sessionDto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("teacher_id"));
    }

    @Test
    void shouldPassValidationWithValidFields() {
        Set<ConstraintViolation<SessionDto>> violations = validator.validate(sessionDto);
        assertThat(violations).isEmpty();
    }

    @Test
    void equalsAndHashCode_ShouldWorkCorrectly() {
        SessionDto sessionDto1 = new SessionDto(1L, "Session 1", new Date(), 2L, "My description", Collections.singletonList(1L), LocalDateTime.now(), LocalDateTime.now());
        SessionDto sessionDto2 = new SessionDto(1L, "Session 1", new Date(), 2L, "My description", Collections.singletonList(1L), LocalDateTime.now(), LocalDateTime.now());

        assertThat(sessionDto1).isEqualTo(sessionDto2);
        assertThat(sessionDto1.hashCode()).isEqualTo(sessionDto2.hashCode());
    }

    @Test
    void toString_ShouldIncludeRelevantFields() {
        String sessionDtoString = sessionDto.toString();
        assertThat(sessionDtoString).contains("Session 1", "My description");
    }
}
