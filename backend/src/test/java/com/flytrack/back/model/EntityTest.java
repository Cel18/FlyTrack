package com.flytrack.back.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @Test
    void testVueloEntity() {
        Vuelo v = new Vuelo();
        v.setOrigen("BOG");
        v.setDestino("MAD");
        v.setEstadoVuelo(EstadoVuelo.PUNTUAL);
        
        assertEquals("BOG", v.getOrigen());
        assertEquals("MAD", v.getDestino());
        assertEquals(EstadoVuelo.PUNTUAL, v.getEstadoVuelo());
    }

    @Test
    void testUsuarioEntity() {
        Usuario u = new Usuario();
        u.setNombre("Test");
        u.setCorreo("test@test.com");
        u.setRol(Rol.USER);
        
        assertEquals("Test", u.getNombre());
        assertEquals("test@test.com", u.getCorreo());
        assertEquals(Rol.USER, u.getRol());
    }
    
    @Test
    void testNotificacionEntity() {
        Notificacion n = new Notificacion();
        n.setContenido("Test");
        assertEquals("Test", n.getContenido());
    }
}
