package com.flytrack.back;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flytrack.back.dto.LoginRequestDTO;
import com.flytrack.back.dto.RegisterRequestDTO;
import com.flytrack.back.model.Rol;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullFlowTest() throws Exception {
        // 1. Registro de usuario
        RegisterRequestDTO register = new RegisterRequestDTO("Integration User", "integration@test.com", "password123", "USER");
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        // 2. Login
        LoginRequestDTO login = new LoginRequestDTO("integration@test.com", "password123");
        String response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        
        String token = objectMapper.readTree(response).get("token").asText();

        // 3. Acceso a recursos protegidos con Token
        mockMvc.perform(get("/api/vuelos")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
                
        // 4. Acceso público (sin token)
        mockMvc.perform(get("/api/vuelos"))
                .andExpect(status().isOk());

        // 5. Fallo de login (credenciales erróneas)
        LoginRequestDTO badLogin = new LoginRequestDTO("integration@test.com", "wrongpass");
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badLogin)))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    void testPublicEndpoints() throws Exception {
        mockMvc.perform(get("/api/puertas"))
                .andExpect(status().isOk());
                
        mockMvc.perform(get("/api/vuelos/999"))
                .andExpect(status().isNotFound());
    }
}
