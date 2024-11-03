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

class TeacherTest {

    private Validator validator;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        teacher = Teacher.builder()
                .firstName("Margot")
                .lastName("DELAHAYE")
                .build();
    }

    @Test
    void shouldCreateTeacherWithValidAttributes() {
        assertThat(teacher.getFirstName()).isEqualTo("Margot");
        assertThat(teacher.getLastName()).isEqualTo("DELAHAYE");
    }

    @Test
    void shouldFailValidationWhenFirstNameIsBlank() {
        teacher.setFirstName("");

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @Test
    void shouldFailValidationWhenLastNameExceedsMaxSize() {
        teacher.setLastName("ThisLastNameIsWayTooLongForTheField");

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
    }

    @Test
    void createdAt_ShouldNotBeNull_WhenTeacherIsPersisted() {
        LocalDateTime createdAt = LocalDateTime.now();
        ReflectionTestUtils.setField(teacher, "createdAt", createdAt);

        assertThat(teacher.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void updatedAt_ShouldUpdate_WhenTeacherIsUpdated() {
        LocalDateTime initialUpdate = LocalDateTime.now();
        ReflectionTestUtils.setField(teacher, "updatedAt", initialUpdate);
        assertThat(teacher.getUpdatedAt()).isEqualTo(initialUpdate);

        LocalDateTime newUpdate = initialUpdate.plusDays(1);
        ReflectionTestUtils.setField(teacher, "updatedAt", newUpdate);

        assertThat(teacher.getUpdatedAt()).isEqualTo(newUpdate);
    }

    @Test
    void toString_ShouldIncludeRelevantFields() {
        String teacherString = teacher.toString();
        assertThat(teacherString).contains("Margot", "DELAHAYE");
    }

    @Test
    void equalsAndHashCode_ShouldWorkCorrectly() {
        Teacher teacher1 = new Teacher(1L, "DELAHAYE", "Margot", null, null);
        Teacher teacher2 = new Teacher(1L, "DELAHAYE", "Margot", null, null);

        assertThat(teacher1).isEqualTo(teacher2);
        assertThat(teacher1.hashCode()).isEqualTo(teacher2.hashCode());
    }
}
