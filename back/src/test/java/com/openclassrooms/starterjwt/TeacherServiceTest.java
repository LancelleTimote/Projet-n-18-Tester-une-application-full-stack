package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        teacher = new Teacher().setId(1L).setFirstName("Margot").setLastName("DELAHAYE");
    }

    @Test
    void findAll_ShouldReturnListOfTeachers() {
        when(teacherRepository.findAll()).thenReturn(List.of(teacher));
        List<Teacher> teachers = teacherService.findAll();
        assertEquals(1, teachers.size());
        assertEquals(teacher, teachers.get(0));
    }

    @Test
    void findById_ShouldReturnTeacherIfFound() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        Teacher foundTeacher = teacherService.findById(1L);
        assertEquals(teacher, foundTeacher);
    }

    @Test
    void findById_ShouldReturnNullIfNotFound() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());
        Teacher foundTeacher = teacherService.findById(1L);
        assertNull(foundTeacher);
    }
}
