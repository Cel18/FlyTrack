package com.flytrack.back.controller;

import com.flytrack.back.exception.BadRequestException;
import com.flytrack.back.model.Usuario;
import com.flytrack.back.service.UsuarioService;
import com.flytrack.back.service.VueloService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VueloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VueloService vueloService;

    @MockBean
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setCorreo("user@test.com");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void suscribir_AsAdmin_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(post("/api/vuelos/1/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(vueloService).suscribir(1L, 1L);
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void suscribir_AsOwner_ShouldReturnNoContent() throws Exception {
        when(usuarioService.getById(1L)).thenReturn(usuario);

        mockMvc.perform(post("/api/vuelos/1/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(vueloService).suscribir(1L, 1L);
    }

    @Test
    @WithMockUser(username = "other@test.com")
    void suscribir_AsOtherUser_ShouldReturnForbidden() throws Exception {
        when(usuarioService.getById(1L)).thenReturn(usuario);

        mockMvc.perform(post("/api/vuelos/1/usuarios/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void suscribir_WhenAlreadySubscribed_ShouldReturnBadRequest() throws Exception {
        doThrow(new BadRequestException("El usuario ya está suscrito a este vuelo."))
                .when(vueloService).suscribir(1L, 1L);

        mockMvc.perform(post("/api/vuelos/1/usuarios/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void desuscribir_AsAdmin_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/vuelos/1/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(vueloService).desuscribir(1L, 1L);
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void desuscribir_AsOwner_ShouldReturnNoContent() throws Exception {
        when(usuarioService.getById(1L)).thenReturn(usuario);

        mockMvc.perform(delete("/api/vuelos/1/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(vueloService).desuscribir(1L, 1L);
    }
}
