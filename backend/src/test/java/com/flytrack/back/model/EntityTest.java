package com.flytrack.back.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @Test
    void testVueloEntity() {
        LocalDateTime now = LocalDateTime.now();
        Vuelo v = new Vuelo("BOG", "MAD", "Desc", now, now.plusHours(1));
        v.setIdVuelo(1L);
        v.setEstadoVuelo(EstadoVuelo.PUNTUAL);
        v.setPuertaEmbarque(new PuertaEmbarque());
        v.setListNotificaciones(new java.util.ArrayList<>());
        v.setListReportes(new java.util.ArrayList<>());
        v.setUsuarios(new java.util.ArrayList<>());
        
        assertEquals(1L, v.getIdVuelo());
        assertEquals("BOG", v.getOrigen());
        assertEquals("MAD", v.getDestino());
        assertEquals("Desc", v.getDescripcion());
        assertEquals(now, v.getHoraPartida());
        assertEquals(now.plusHours(1), v.getHoraLlegada());
        assertEquals(EstadoVuelo.PUNTUAL, v.getEstadoVuelo());
        assertNotNull(v.getPuertaEmbarque());
        assertNotNull(v.getListNotificaciones());
        assertNotNull(v.getListReportes());
        assertNotNull(v.getUsuarios());
    }

    @Test
    void testUsuarioEntity() {
        Usuario u = new Usuario("Test", "test@test.com", "pass", Rol.USER);
        u.setIdUsuario(1L);
        u.setEstadoCuenta(EstadoCuenta.ACTIVO);
        u.setListVuelos(new java.util.ArrayList<>());
        
        assertEquals(1L, u.getIdUsuario());
        assertEquals("Test", u.getNombre());
        assertEquals("test@test.com", u.getCorreo());
        assertEquals("pass", u.getPassword());
        assertEquals(Rol.USER, u.getRol());
        assertEquals(EstadoCuenta.ACTIVO, u.getEstadoCuenta());
        assertNotNull(u.getListVuelos());
    }
    
    @Test
    void testNotificacionEntity() {
        Notificacion n = new Notificacion("Test", new Usuario(), new Vuelo());
        n.setId(1L);
        n.setFechaEnvio(LocalDateTime.now());
        
        assertEquals(1L, n.getId());
        assertEquals("Test", n.getContenido());
        assertNotNull(n.getDestinatario());
        assertNotNull(n.getVuelo());
        assertNotNull(n.getFechaEnvio());
    }

    @Test
    void testPuertaEmbarqueEntity() {
        PuertaEmbarque p = new PuertaEmbarque("A1", "T1", LocalDateTime.now(), new Vuelo());
        p.setIdPuerta(1L);
        
        assertEquals(1L, p.getIdPuerta());
        assertEquals("A1", p.getCodigo());
        assertEquals("T1", p.getTerminal());
        assertNotNull(p.getFechaCierre());
        assertNotNull(p.getVuelo());
    }

    @Test
    void testReporteEntity() {
        Reporte r = new Reporte("Desc", new Usuario(), new Vuelo());
        r.setId(1L);
        r.setEstadoReporte(EstadoReporte.PENDIENTE);
        r.setFechaCreacion(LocalDateTime.now());
        
        assertEquals(1L, r.getId());
        assertEquals("Desc", r.getDescripcion());
        assertEquals(EstadoReporte.PENDIENTE, r.getEstadoReporte());
        assertNotNull(r.getFechaCreacion());
        assertNotNull(r.getUsuario());
        assertNotNull(r.getVuelo());
    }
}
