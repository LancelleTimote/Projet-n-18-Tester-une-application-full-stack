package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TeacherServiceTest {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    void findAll_ShouldReturnListOfTeachers() {
        List<Teacher> teachers = teacherService.findAll();
        assertEquals(2, teachers.size());
    }

    @Test
    void findById_ShouldReturnTeacherIfFound() {
        List<Teacher> teachers = teacherRepository.findAll();

        Teacher teacher = teachers.stream()
                .filter(t -> "DELAHAYE".equals(t.getLastName()))
                .findFirst()
                .orElseThrow();

        Teacher foundTeacher = teacherService.findById(teacher.getId());

        assertNotNull(foundTeacher);
        assertEquals(teacher.getFirstName(), foundTeacher.getFirstName());
    }

    @Test
    void findById_ShouldReturnNullIfNotFound() {
        Teacher foundTeacher = teacherService.findById(999L);
        assertNull(foundTeacher);
    }
}
