package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void findById_shouldReturnUser_whenUserExists() {
        User user = new User();
        user.setId(1L);

        when(userService.findById(1L)).thenReturn(user);

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> response = userController.findById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(userDto, response.getBody());
    }

    @Test
    void findById_shouldReturnNotFound_whenUserDoesNotExist() {
        when(userService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = userController.findById("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void delete_shouldReturnOk_whenUserIsDeleted() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("yoga@studio.com");

        when(userService.findById(1L)).thenReturn(mockUser);

        UserDetails mockUserDetails = org.springframework.security.core.userdetails.User
                .withUsername("yoga@studio.com")
                .password("test!1234")
                .authorities(new ArrayList<>())
                .build();

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(mockUserDetails, null, mockUserDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);

        ResponseEntity<?> response = userController.delete("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(userService, times(1)).delete(1L);
    }

}
