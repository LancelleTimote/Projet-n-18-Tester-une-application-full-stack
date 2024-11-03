package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class TeacherControllerTest {
    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherController teacherController;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void findById_shouldReturnTeacherDto_onExistingTeacher() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        when(teacherService.findById(1L)).thenReturn(teacher);

        ResponseEntity<?> response = teacherController.findById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void findAll_shouldReturnListOfTeacherDtos() {
        List<Teacher> teachers = Collections.singletonList(new Teacher());
        when(teacherService.findAll()).thenReturn(teachers);

        ResponseEntity<?> response = teacherController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoTeachers() {
        when(teacherService.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = teacherController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(((List<?>) response.getBody()).isEmpty());
    }

    @Test
    void findById_shouldReturnNotFound_ifTeacherDoesNotExist() {
        when(teacherService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = teacherController.findById("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
