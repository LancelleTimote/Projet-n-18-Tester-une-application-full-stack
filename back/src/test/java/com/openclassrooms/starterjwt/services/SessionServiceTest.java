package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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
        user = userRepository.findByEmail("yoga@studio.com").orElseThrow(NotFoundException::new);

        session = sessionRepository.findAll().get(0);
    }

    @Test
    void createSession_ShouldSaveAndReturnSession() {
        Session newSession = new Session()
                .setName("New Yoga Session")
                .setDescription("A new description")
                .setDate(new java.util.Date());

        Session createdSession = sessionService.create(newSession);
        assertNotNull(createdSession.getId());
        assertEquals("New Yoga Session", createdSession.getName());
    }

    @Test
    void participate_ShouldAddUserToSession() {
        sessionService.noLongerParticipate(1L, 2L);

        sessionService.participate(1L, 2L);
        Session session = sessionService.getById(1L);
        assertTrue(session.getUsers().contains(userRepository.findById(2L).get()));
    }

    @Test
    void noLongerParticipate_ShouldRemoveUserFromSession() {
        sessionService.noLongerParticipate(1L, 1L);
        Session session = sessionService.getById(1L);
        assertFalse(session.getUsers().contains(userRepository.findById(1L).get()));
    }
}
