package com.flytrack.back.controller;

import com.flytrack.back.dto.*;
import com.flytrack.back.model.*;
import com.flytrack.back.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControllerTest {

    @Mock private AuthService authService;
    @Mock private VueloService vueloService;
    @Mock private UsuarioService usuarioService;
    @Mock private PuertaEmbarqueService puertaEmbarqueService;
    @Mock private ReporteService reporteService;
    @Mock private NotificacionService notificacionService;

    @InjectMocks private AuthController authController;
    @InjectMocks private VueloController vueloController;
    @InjectMocks private UsuarioController usuarioController;
    @InjectMocks private PuertaEmbarqueController puertaEmbarqueController;
    @InjectMocks private ReporteController reporteController;
    @InjectMocks private NotificacionController notificacionController;

    @Test
    void testAuthController() {
        LoginRequestDTO req = new LoginRequestDTO("test@test.com", "pass");
        AuthResponseDTO res = new AuthResponseDTO(1L, "token", "test@test.com", "USER", "Test");
        when(authService.login(any())).thenReturn(res);

        ResponseEntity<AuthResponseDTO> response = authController.login(req);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token", response.getBody().token());
        
        RegisterRequestDTO reg = new RegisterRequestDTO("Test", "test@test.com", "pass", "USER");
        ResponseEntity<?> regResp = authController.register(reg);
        assertEquals(HttpStatus.OK, regResp.getStatusCode());
    }

    @Test
    void testVueloController() {
        Vuelo v = new Vuelo();
        when(vueloService.getAll()).thenReturn(List.of(v));
        when(vueloService.getById(1L)).thenReturn(v);
        when(vueloService.create(any())).thenReturn(v);
        when(vueloService.update(eq(1L), any())).thenReturn(v);

        assertEquals(HttpStatus.OK, vueloController.getAll().getStatusCode());
        assertEquals(HttpStatus.OK, vueloController.getById(1L).getStatusCode());
        
        VueloDTO dto = new VueloDTO("A", "B", "C", LocalDateTime.now(), LocalDateTime.now(), "PUNTUAL");
        assertEquals(HttpStatus.CREATED, vueloController.create(dto).getStatusCode());
        assertEquals(HttpStatus.OK, vueloController.update(1L, dto).getStatusCode());
        
        assertEquals(HttpStatus.NO_CONTENT, vueloController.delete(1L).getStatusCode());
        verify(vueloService).delete(1L);
    }
    
    @Test
    void testUsuarioController() {
        Usuario u = new Usuario("Test", "test@test.com", "pass", Rol.USER);
        u.setIdUsuario(1L);
        when(usuarioService.getAll()).thenReturn(List.of(u));
        assertEquals(HttpStatus.OK, usuarioController.getAll().getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, usuarioController.delete(1L).getStatusCode());
        verify(usuarioService).delete(1L);
    }

    @Test
    void testPuertaEmbarqueController() {
        PuertaEmbarque p = new PuertaEmbarque();
        when(puertaEmbarqueService.getAll()).thenReturn(List.of(p));
        when(puertaEmbarqueService.getById(1L)).thenReturn(p);
        when(puertaEmbarqueService.getByVueloId(1L)).thenReturn(p);
        when(puertaEmbarqueService.create(any())).thenReturn(p);
        when(puertaEmbarqueService.update(eq(1L), any())).thenReturn(p);

        assertEquals(HttpStatus.OK, puertaEmbarqueController.getAll().getStatusCode());
        assertEquals(HttpStatus.OK, puertaEmbarqueController.getById(1L).getStatusCode());
        assertEquals(HttpStatus.OK, puertaEmbarqueController.getByVuelo(1L).getStatusCode());
        
        PuertaEmbarqueDTO dto = new PuertaEmbarqueDTO("A", "B", LocalDateTime.now(), 1L);
        assertEquals(HttpStatus.CREATED, puertaEmbarqueController.create(dto).getStatusCode());
        assertEquals(HttpStatus.OK, puertaEmbarqueController.update(1L, dto).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, puertaEmbarqueController.delete(1L).getStatusCode());
    }

    @Test
    void testReporteController() {
        Reporte r = new Reporte();
        when(reporteService.getAll()).thenReturn(List.of(r));
        when(reporteService.create(any())).thenReturn(r);

        assertEquals(HttpStatus.OK, reporteController.getAll().getStatusCode());
        
        ReporteDTO dto = new ReporteDTO("D", 1L, 1L);
        assertEquals(HttpStatus.CREATED, reporteController.create(dto).getStatusCode());
        assertEquals(HttpStatus.OK, reporteController.updateEstado(1L, java.util.Map.of("estado", "FINALIZADO")).getStatusCode());
    }

    @Test
    void testNotificacionController() {
        Notificacion n = new Notificacion();
        when(notificacionService.getByUsuarioId(1L)).thenReturn(List.of(n));
        assertEquals(HttpStatus.OK, notificacionController.getByUsuario(1L).getStatusCode());
    }
}
