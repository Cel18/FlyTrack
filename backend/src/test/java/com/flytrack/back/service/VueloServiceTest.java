package com.flytrack.back.service;

import com.flytrack.back.dto.VueloDTO;
import com.flytrack.back.exception.ResourceNotFoundException;
import com.flytrack.back.model.EstadoVuelo;
import com.flytrack.back.model.Vuelo;
import com.flytrack.back.repository.VueloRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VueloServiceTest {

    @Mock
    private VueloRepository vueloRepository;

    @InjectMocks
    private VueloService vueloService;

    private Vuelo vueloMock;

    @BeforeEach
    void setUp() {
        vueloMock = new Vuelo("BOG", "MED", "Vuelo de prueba", 
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));
        vueloMock.setIdVuelo(1L);
        vueloMock.setEstadoVuelo(EstadoVuelo.PUNTUAL);
    }

    @Test
    void shouldReturnFlightById() {
        when(vueloRepository.findById(1L)).thenReturn(Optional.of(vueloMock));

        Vuelo result = vueloService.getById(1L);

        assertNotNull(result);
        assertEquals("BOG", result.getOrigen());
        assertEquals("MED", result.getDestino());
        verify(vueloRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenFlightNotFound() {
        when(vueloRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vueloService.getById(99L));
        verify(vueloRepository, times(1)).findById(99L);
    }

    @Test
    void shouldCreateFlightSuccessfully() {
        VueloDTO dto = new VueloDTO("BOG", "MED", "Vuelo de prueba", 
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), "PUNTUAL");

        when(vueloRepository.save(any(Vuelo.class))).thenReturn(vueloMock);

        Vuelo result = vueloService.create(dto);

        assertNotNull(result);
        assertEquals(EstadoVuelo.PUNTUAL, result.getEstadoVuelo());
        verify(vueloRepository, times(1)).save(any(Vuelo.class));
    }
}
