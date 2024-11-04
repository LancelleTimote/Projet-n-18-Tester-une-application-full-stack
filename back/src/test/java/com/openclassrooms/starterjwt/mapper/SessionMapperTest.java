package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SessionMapperTest {

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toEntity_shouldMapSessionDtoToSession() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setDescription("My description");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Arrays.asList(2L, 3L));

        Teacher teacher = new Teacher();
        teacher.setId(1L);

        User user1 = new User();
        user1.setId(2L);
        User user2 = new User();
        user2.setId(3L);

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(2L)).thenReturn(user1);
        when(userService.findById(3L)).thenReturn(user2);

        Session session = sessionMapper.toEntity(sessionDto);

        assertThat(session).isNotNull();
        assertThat(session.getDescription()).isEqualTo(sessionDto.getDescription());
        assertThat(session.getTeacher()).isNotNull();
        assertThat(session.getUsers()).hasSize(2);
        assertThat(session.getUsers()).containsExactlyInAnyOrder(user1, user2);
    }

    @Test
    void toDto_shouldMapSessionToSessionDto() {
        Session session = new Session();
        session.setDescription("My description");
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        session.setTeacher(teacher);
        User user1 = new User();
        user1.setId(2L);
        User user2 = new User();
        user2.setId(3L);
        session.setUsers(Arrays.asList(user1, user2));

        SessionDto sessionDto = sessionMapper.toDto(session);

        assertThat(sessionDto).isNotNull();
        assertThat(sessionDto.getDescription()).isEqualTo(session.getDescription());
        assertThat(sessionDto.getTeacher_id()).isEqualTo(teacher.getId());
        assertThat(sessionDto.getUsers()).hasSize(2);
        assertThat(sessionDto.getUsers()).containsExactlyInAnyOrder(2L, 3L);
    }

    @Test
    void toEntity_shouldReturnEmptyUsersWhenUsersAreNull() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setUsers(null);

        Session session = sessionMapper.toEntity(sessionDto);

        assertThat(session.getUsers()).isEmpty();
    }

    @Test
    void toDto_shouldReturnEmptyUsersWhenUsersAreNull() {
        Session session = new Session();
        session.setUsers(null);

        SessionDto sessionDto = sessionMapper.toDto(session);

        assertThat(sessionDto.getUsers()).isEmpty();
    }
}
