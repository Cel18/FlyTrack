package com.flytrack.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flytrack.back.dto.NotificacionDTO;
import com.flytrack.back.model.Notificacion;
import com.flytrack.back.model.Usuario;
import com.flytrack.back.model.Vuelo;
import com.flytrack.back.service.NotificacionService;
import com.flytrack.back.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificacionService notificacionService;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Notificacion notificacion;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setCorreo("user@test.com");

        Vuelo vuelo = new Vuelo();
        vuelo.setIdVuelo(1L);

        notificacion = new Notificacion("Test content", usuario, vuelo);
        notificacion.setId(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getByUsuario_AsAdmin_ShouldReturnOk() throws Exception {
        when(notificacionService.getByUsuarioId(1L)).thenReturn(Arrays.asList(notificacion));

        mockMvc.perform(get("/api/notificaciones/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].contenido").value("Test content"));
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void getByUsuario_AsOwner_ShouldReturnOk() throws Exception {
        when(usuarioService.getById(1L)).thenReturn(usuario);
        when(notificacionService.getByUsuarioId(1L)).thenReturn(Arrays.asList(notificacion));

        mockMvc.perform(get("/api/notificaciones/usuario/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getByVuelo_Authenticated_ShouldReturnOk() throws Exception {
        when(notificacionService.getByVueloId(1L)).thenReturn(Arrays.asList(notificacion));

        mockMvc.perform(get("/api/notificaciones/vuelo/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_AsAdmin_ShouldReturnCreated() throws Exception {
        NotificacionDTO dto = new NotificacionDTO("New notification", 1L, 1L);
        when(notificacionService.create(any(NotificacionDTO.class))).thenReturn(notificacion);

        mockMvc.perform(post("/api/notificaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void create_AsUser_ShouldReturnForbidden() throws Exception {
        NotificacionDTO dto = new NotificacionDTO("New notification", 1L, 1L);

        mockMvc.perform(post("/api/notificaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_AsAdmin_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/notificaciones/1"))
                .andExpect(status().isNoContent());
    }
}
