package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SessionTest {

    private Validator validator;
    private Session session;
    private Teacher teacher;
    private List<User> users;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        teacher = Teacher.builder()
                .firstName("Margot")
                .lastName("DELAHAYE")
                .build();

        User user1 = User.builder().email("yoga@studio.com").firstName("Yoga").lastName("Studio").password("test!1234").admin(false).build();
        User user2 = User.builder().email("toto3@toto.com").firstName("toto").lastName("toto").password("test!1234").admin(false).build();
        users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        session = Session.builder()
                .name("Session 1")
                .date(new Date())
                .description("My description")
                .teacher(teacher)
                .users(users)
                .build();
    }

    @Test
    void shouldCreateSessionWithValidAttributes() {
        assertThat(session.getName()).isEqualTo("Session 1");
        assertThat(session.getDate()).isNotNull();
        assertThat(session.getDescription()).isEqualTo("My description");
        assertThat(session.getTeacher()).isEqualTo(teacher);
        assertThat(session.getUsers()).containsExactlyElementsOf(users);
    }

    @Test
    void shouldFailValidationWhenNameIsBlank() {
        session.setName("");

        Set<ConstraintViolation<Session>> violations = validator.validate(session);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void shouldFailValidationWhenDescriptionExceedsMaxSize() {
        String longDescription = "A".repeat(2501);
        session.setDescription(longDescription);

        Set<ConstraintViolation<Session>> violations = validator.validate(session);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("description"));
    }

    @Test
    void shouldFailValidationWhenDateIsNull() {
        session.setDate(null);

        Set<ConstraintViolation<Session>> violations = validator.validate(session);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("date"));
    }

    @Test
    void shouldAssignTeacherCorrectly() {
        assertThat(session.getTeacher()).isEqualTo(teacher);
    }

    @Test
    void shouldAssignUsersCorrectly() {
        assertThat(session.getUsers()).containsExactlyElementsOf(users);
    }

    @Test
    void createdAt_ShouldNotBeNull_WhenSessionIsPersisted() {
        LocalDateTime createdAt = LocalDateTime.now();
        ReflectionTestUtils.setField(session, "createdAt", createdAt);

        assertThat(session.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void updatedAt_ShouldUpdate_WhenSessionIsUpdated() {
        LocalDateTime initialUpdate = LocalDateTime.now();
        ReflectionTestUtils.setField(session, "updatedAt", initialUpdate);
        assertThat(session.getUpdatedAt()).isEqualTo(initialUpdate);

        LocalDateTime newUpdate = initialUpdate.plusDays(1);
        ReflectionTestUtils.setField(session, "updatedAt", newUpdate);

        assertThat(session.getUpdatedAt()).isEqualTo(newUpdate);
    }

    @Test
    void toString_ShouldIncludeRelevantFields() {
        String sessionString = session.toString();
        assertThat(sessionString).contains("Session 1", "Margot", "DELAHAYE");
    }

    @Test
    void equalsAndHashCode_ShouldWorkCorrectly() {
        Session session1 = new Session(1L, "Session 1", new Date(), "My description", teacher, users, null, null);
        Session session2 = new Session(1L, "Session 1", new Date(), "My description", teacher, users, null, null);

        assertThat(session1).isEqualTo(session2);
        assertThat(session1.hashCode()).isEqualTo(session2.hashCode());
    }
}
