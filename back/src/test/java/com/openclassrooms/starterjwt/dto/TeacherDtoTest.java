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

class TeacherDtoTest {

    private Validator validator;
    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("DELAHAYE");
        teacherDto.setFirstName("Margot");
        teacherDto.setCreatedAt(LocalDateTime.now());
        teacherDto.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void shouldCreateTeacherDtoWithValidAttributes() {
        assertThat(teacherDto.getId()).isEqualTo(1L);
        assertThat(teacherDto.getLastName()).isEqualTo("DELAHAYE");
        assertThat(teacherDto.getFirstName()).isEqualTo("Margot");
        assertThat(teacherDto.getCreatedAt()).isNotNull();
        assertThat(teacherDto.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldFailValidationWhenLastNameIsBlank() {
        teacherDto.setLastName("");

        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(teacherDto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
    }

    @Test
    void shouldFailValidationWhenLastNameExceedsMaxSize() {
        String longLastName = "A".repeat(21); // 1 character over the limit
        teacherDto.setLastName(longLastName);

        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(teacherDto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
    }

    @Test
    void shouldFailValidationWhenFirstNameIsBlank() {
        teacherDto.setFirstName("");

        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(teacherDto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @Test
    void shouldFailValidationWhenFirstNameExceedsMaxSize() {
        String longFirstName = "B".repeat(21);
        teacherDto.setFirstName(longFirstName);

        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(teacherDto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @Test
    void shouldPassValidationWithValidFields() {
        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(teacherDto);
        assertThat(violations).isEmpty();
    }

    @Test
    void equalsAndHashCode_ShouldWorkCorrectly() {
        TeacherDto teacherDto1 = new TeacherDto(1L, "DELAHAYE", "Margot", LocalDateTime.now(), LocalDateTime.now());
        TeacherDto teacherDto2 = new TeacherDto(1L, "DELAHAYE", "Margot", LocalDateTime.now(), LocalDateTime.now());

        assertThat(teacherDto1).isEqualTo(teacherDto2);
        assertThat(teacherDto1.hashCode()).isEqualTo(teacherDto2.hashCode());
    }

    @Test
    void toString_ShouldIncludeRelevantFields() {
        String teacherDtoString = teacherDto.toString();
        assertThat(teacherDtoString).contains("DELAHAYE", "Margot");
    }
}
