package com.flytrack.back.service;

import com.flytrack.back.dto.PuertaEmbarqueDTO;
import com.flytrack.back.model.*;
import com.flytrack.back.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FullCoverageTest {

    @Mock private PuertaEmbarqueRepository puertaRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private VueloService vueloService;
    
    @InjectMocks private PuertaEmbarqueService puertaEmbarqueService;
    @InjectMocks private UsuarioService usuarioService;

    @Test
    void testPuertaEmbarqueService() {
        Vuelo vuelo = new Vuelo();
        vuelo.setIdVuelo(1L);
        
        PuertaEmbarque puerta = new PuertaEmbarque("A1", "T1", LocalDateTime.now(), vuelo);
        puerta.setIdPuerta(1L);

        when(puertaRepository.findAll()).thenReturn(List.of(puerta));
        when(puertaRepository.findById(1L)).thenReturn(Optional.of(puerta));
        when(puertaRepository.existsByVueloIdVuelo(1L)).thenReturn(false);
        when(vueloService.getById(1L)).thenReturn(vuelo);
        when(puertaRepository.save(any())).thenReturn(puerta);

        assertNotNull(puertaEmbarqueService.getAll());
        assertNotNull(puertaEmbarqueService.getById(1L));
        
        PuertaEmbarqueDTO dto = new PuertaEmbarqueDTO("A1", "T1", LocalDateTime.now(), 1L);
        assertNotNull(puertaEmbarqueService.create(dto));
        
        verify(puertaRepository, atLeastOnce()).save(any());
    }

    @Test
    void testUsuarioService() {
        Usuario user = new Usuario("Test", "test@test.com", "pass", Rol.USER);
        user.setIdUsuario(1L);

        when(usuarioRepository.findAll()).thenReturn(List.of(user));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(user));

        assertNotNull(usuarioService.getAll());
        assertNotNull(usuarioService.getById(1L));
        
        usuarioService.delete(1L);
        verify(usuarioRepository).delete(any());
    }
}
