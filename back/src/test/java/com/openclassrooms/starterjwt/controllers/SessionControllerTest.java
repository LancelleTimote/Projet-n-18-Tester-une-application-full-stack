package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        String loginRequest = "{\"email\": \"yoga@studio.com\", \"password\": \"test!1234\"}";
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Map<String, String> responseMap = objectMapper.readValue(response, Map.class);
        jwtToken = responseMap.get("token");
    }

    @Test
    void findById_shouldReturnSessionDto_onExistingSession() throws Exception {
        mockMvc.perform(get("/api/session/1")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Session 1"))
                .andExpect(jsonPath("$.description").value("My description"));
    }

    @Test
    void findById_shouldReturnNotFound_ifSessionDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/session/999")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAll_shouldReturnListOfSessions() throws Exception {
        mockMvc.perform(get("/api/session")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void create_shouldReturnCreatedSession() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("New Session");
        sessionDto.setDescription("A new session description");

        LocalDateTime localDateTime = LocalDateTime.parse("2024-12-05T10:00:00");
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        sessionDto.setDate(date);
        sessionDto.setTeacher_id(1L);

        mockMvc.perform(post("/api/session")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Session"))
                .andExpect(jsonPath("$.description").value("A new session description"));
    }

    @Test
    void participate_shouldAddUserToSession_onValidRequest() throws Exception {
        mockMvc.perform(post("/api/session/1/participate/2")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }

    @Test
    void update_shouldReturnUpdatedSession_onValidRequest() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Updated Session");
        sessionDto.setDescription("Updated session description");

        LocalDateTime localDateTime = LocalDateTime.parse("2024-12-05T10:00:00");
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        sessionDto.setDate(date);

        sessionDto.setTeacher_id(1L);

        mockMvc.perform(put("/api/session/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Session"))
                .andExpect(jsonPath("$.description").value("Updated session description"));
    }

    @Test
    void noLongerParticipate_shouldReturnOk_whenUserLeavesSession() throws Exception {
        mockMvc.perform(delete("/api/session/1/participate/2")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }
}
