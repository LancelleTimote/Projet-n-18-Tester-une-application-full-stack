package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        session = new Session().setId(1L).setName("Session 1");
        user = new User().setId(1L).setFirstName("Yoga").setLastName("Studio").setEmail("yoga@studio.com");
    }

    @Test
    void createSession_ShouldSaveAndReturnSession() {
        when(sessionRepository.save(session)).thenReturn(session);
        Session createdSession = sessionService.create(session);
        assertEquals(session, createdSession);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void deleteSession_ShouldDeleteSessionById() {
        sessionService.delete(1L);
        verify(sessionRepository, times(1)).deleteById(1L);
    }

    @Test
    void getSessionById_ShouldReturnSessionIfFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        Session foundSession = sessionService.getById(1L);
        assertEquals(session, foundSession);
    }

    @Test
    void getSessionById_ShouldReturnNullIfNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        Session foundSession = sessionService.getById(1L);
        assertNull(foundSession);
    }

    @Test
    void participate_ShouldAddUserToSession() {
        session.setUsers(new ArrayList<>());
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        sessionService.participate(1L, 1L);
        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void participate_ShouldThrowNotFoundExceptionIfSessionOrUserNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    void noLongerParticipate_ShouldRemoveUserFromSession() {
        session.setUsers(List.of(user));
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(1L, 1L);
        assertFalse(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void noLongerParticipate_ShouldThrowBadRequestExceptionIfUserNotInSession() {
        session.setUsers(new ArrayList<>());
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }
}
