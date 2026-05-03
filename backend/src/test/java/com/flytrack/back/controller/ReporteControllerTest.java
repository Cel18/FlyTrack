package com.flytrack.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flytrack.back.dto.ReporteDTO;
import com.flytrack.back.model.Reporte;
import com.flytrack.back.model.Usuario;
import com.flytrack.back.model.Vuelo;
import com.flytrack.back.service.ReporteService;
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
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReporteService reporteService;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Reporte reporte;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setCorreo("user@test.com");

        Vuelo vuelo = new Vuelo();
        vuelo.setIdVuelo(1L);

        reporte = new Reporte("Test description", usuario, vuelo);
        reporte.setId(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAll_AsAdmin_ShouldReturnOk() throws Exception {
        when(reporteService.getAll()).thenReturn(Arrays.asList(reporte));
        mockMvc.perform(get("/api/reportes"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getById_AsAdmin_ShouldReturnOk() throws Exception {
        when(reporteService.getById(1L)).thenReturn(reporte);
        mockMvc.perform(get("/api/reportes/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void getById_AsOwner_ShouldReturnOk() throws Exception {
        when(reporteService.getById(1L)).thenReturn(reporte);
        mockMvc.perform(get("/api/reportes/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateEstado_AsAdmin_ShouldReturnOk() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("estado", "RESUELTO");
        
        when(reporteService.updateEstado(any(), anyString())).thenReturn(reporte);
        
        mockMvc.perform(put("/api/reportes/1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void create_ShouldReturnCreated() throws Exception {
        ReporteDTO dto = new ReporteDTO("New report", 1L, 1L);
        when(reporteService.create(any(ReporteDTO.class))).thenReturn(reporte);
        
        mockMvc.perform(post("/api/reportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_AsAdmin_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/reportes/1"))
                .andExpect(status().isNoContent());
    }
}
