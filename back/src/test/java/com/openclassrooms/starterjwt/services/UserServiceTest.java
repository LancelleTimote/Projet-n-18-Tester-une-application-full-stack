package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void delete_ShouldDeleteUserById() {
        User user = userRepository.findByEmail("yoga@studio.com").orElseThrow();

        userService.delete(user.getId());

        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertFalse(deletedUser.isPresent());
    }

    @Test
    void findById_ShouldReturnUserIfFound() {
        User user = userRepository.findByEmail("yoga@studio.com").orElseThrow();

        User foundUser = userService.findById(user.getId());
        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    void findById_ShouldReturnNullIfNotFound() {
        User foundUser = userService.findById(999L);
        assertNull(foundUser);
    }
}
