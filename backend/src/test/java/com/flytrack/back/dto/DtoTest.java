package com.flytrack.back.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class DtoTest {

    @Test
    void testVueloDTO() {
        LocalDateTime now = LocalDateTime.now();
        VueloDTO dto = new VueloDTO("Origen", "Destino", "Desc", now, now.plusHours(1), "PUNTUAL");
        
        assertEquals("Origen", dto.origen());
        assertEquals("Destino", dto.destino());
        assertEquals("Desc", dto.descripcion());
        assertEquals(now, dto.horaPartida());
        assertEquals(now.plusHours(1), dto.horaLlegada());
        assertEquals("PUNTUAL", dto.estadoVuelo());
    }

    @Test
    void testPuertaEmbarqueDTO() {
        LocalDateTime now = LocalDateTime.now();
        PuertaEmbarqueDTO dto = new PuertaEmbarqueDTO("A1", "T1", now, 1L);
        
        assertEquals("A1", dto.codigo());
        assertEquals("T1", dto.terminal());
        assertEquals(now, dto.fechaCierre());
        assertEquals(1L, dto.vueloId());
    }

    @Test
    void testLoginRequestDTO() {
        LoginRequestDTO req = new LoginRequestDTO("test@test.com", "pass");
        assertEquals("test@test.com", req.correo());
        assertEquals("pass", req.password());
    }

    @Test
    void testReporteDTO() {
        ReporteDTO dto = new ReporteDTO("Desc", 1L, 2L);
        assertEquals("Desc", dto.descripcion());
        assertEquals(1L, dto.usuarioId());
        assertEquals(2L, dto.vueloId());
    }
}
