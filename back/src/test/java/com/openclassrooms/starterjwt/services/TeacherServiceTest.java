package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
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

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher().setFirstName("Margot").setLastName("DELAHAYE");
    }

    @Test
    void findAll_ShouldReturnListOfTeachers() {
        teacherRepository.save(teacher);

        List<Teacher> teachers = teacherService.findAll();

        assertEquals(3, teachers.size());

        assertTrue(teachers.stream().anyMatch(t -> t.getFirstName().equals(teacher.getFirstName()) &&
                t.getLastName().equals(teacher.getLastName())));
    }

    @Test
    void findById_ShouldReturnTeacherIfFound() {
        teacherRepository.save(teacher);

        Teacher foundTeacher = teacherService.findById(teacher.getId());

        assertNotNull(foundTeacher);
        assertEquals(teacher.getFirstName(), foundTeacher.getFirstName());
        assertEquals(teacher.getLastName(), foundTeacher.getLastName());
    }

    @Test
    void findById_ShouldReturnNullIfNotFound() {
        Teacher foundTeacher = teacherService.findById(999L);

        assertNull(foundTeacher);
    }
}
