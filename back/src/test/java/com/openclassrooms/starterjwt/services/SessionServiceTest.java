package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class SessionServiceTest {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    private Session session;
    private User user;

    @BeforeEach
    void setUp() {
        session = new Session()
                .setName("Session 1")
                .setDescription("My description")
                .setDate(new java.util.Date())
                .setUsers(new ArrayList<>());

        user = userRepository.findByEmail("yoga@studio.com").orElseThrow(NotFoundException::new);
    }

    @Test
    void createSession_ShouldSaveAndReturnSession() {
        Session createdSession = sessionService.create(session);
        assertNotNull(createdSession.getId());
        assertEquals(session.getName(), createdSession.getName());
    }

    @Test
    void deleteSession_ShouldDeleteSessionById() {
        Session createdSession = sessionService.create(session);
        Long sessionId = createdSession.getId();

        sessionService.delete(sessionId);
        Optional<Session> deletedSession = sessionRepository.findById(sessionId);

        assertFalse(deletedSession.isPresent());
    }

    @Test
    void getSessionById_ShouldReturnSessionIfFound() {
        Session createdSession = sessionService.create(session);
        Session foundSession = sessionService.getById(createdSession.getId());

        assertNotNull(foundSession);
        assertEquals(createdSession.getId(), foundSession.getId());
    }

    @Test
    void getSessionById_ShouldReturnNullIfNotFound() {
        Session foundSession = sessionService.getById(999L);
        assertNull(foundSession);
    }

    @Test
    void participate_ShouldAddUserToSession() {
        Session createdSession = sessionService.create(session);
        sessionService.participate(createdSession.getId(), user.getId());

        Session updatedSession = sessionService.getById(createdSession.getId());
        assertTrue(updatedSession.getUsers().contains(user));
    }

    @Test
    void noLongerParticipate_ShouldRemoveUserFromSession() {
        session.setUsers(new ArrayList<>());
        Session createdSession = sessionService.create(session);
        sessionService.participate(createdSession.getId(), user.getId());

        sessionService.noLongerParticipate(createdSession.getId(), user.getId());

        Session updatedSession = sessionService.getById(createdSession.getId());
        assertFalse(updatedSession.getUsers().contains(user));
    }

    @Test
    void noLongerParticipate_ShouldThrowBadRequestExceptionIfUserNotInSession() {
        session.setUsers(new ArrayList<>());
        Session createdSession = sessionService.create(session);
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(createdSession.getId(), user.getId())); // Utilisateur non trouvé dans la session
    }

    @Test
    void updateSession_ShouldUpdateAndReturnSession() {
        Session createdSession = sessionService.create(session);
        createdSession.setName("Updated Session");

        Session updatedSession = sessionService.update(createdSession.getId(), createdSession);

        assertEquals("Updated Session", updatedSession.getName());
    }

    @Test
    void deleteSession_ShouldThrowNotFoundException_WhenSessionDoesNotExist() {
        assertThrows(EmptyResultDataAccessException.class, () -> sessionService.delete(999L));
    }
}
