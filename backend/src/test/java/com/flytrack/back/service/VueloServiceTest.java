package com.flytrack.back.service;

import com.flytrack.back.dto.VueloDTO;
import com.flytrack.back.exception.BadRequestException;
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
import java.util.Arrays;
import java.util.List;
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
    private LocalDateTime partida;
    private LocalDateTime llegada;

    @BeforeEach
    void setUp() {
        partida = LocalDateTime.now().plusDays(1);
        llegada = partida.plusHours(1);
        vueloMock = new Vuelo("BOG", "MED", "Vuelo de prueba", partida, llegada);
        vueloMock.setIdVuelo(1L);
        vueloMock.setEstadoVuelo(EstadoVuelo.PUNTUAL);
    }

    @Test
    void getAll_ShouldReturnList() {
        when(vueloRepository.findAll()).thenReturn(Arrays.asList(vueloMock));
        List<Vuelo> result = vueloService.getAll();
        assertEquals(1, result.size());
        verify(vueloRepository).findAll();
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
        VueloDTO dto = new VueloDTO("BOG", "MED", "Vuelo de prueba", partida, llegada, "PUNTUAL");

        when(vueloRepository.save(any(Vuelo.class))).thenReturn(vueloMock);

        Vuelo result = vueloService.create(dto);

        assertNotNull(result);
        assertEquals(EstadoVuelo.PUNTUAL, result.getEstadoVuelo());
        verify(vueloRepository, times(1)).save(any(Vuelo.class));
    }

    @Test
    void create_WhenArrivalBeforeDeparture_ShouldThrowException() {
        VueloDTO dto = new VueloDTO("BOG", "MED", "Desc", llegada, partida, "PUNTUAL");
        assertThrows(BadRequestException.class, () -> vueloService.create(dto));
    }

    @Test
    void create_WithNullStatus_ShouldDefaultToPuntual() {
        VueloDTO dto = new VueloDTO("BOG", "MED", "Desc", partida, llegada, null);
        when(vueloRepository.save(any(Vuelo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Vuelo result = vueloService.create(dto);
        assertEquals(EstadoVuelo.PUNTUAL, result.getEstadoVuelo());
    }

    @Test
    void update_WhenValid_ShouldUpdate() {
        VueloDTO dto = new VueloDTO("CLO", "CTG", "Updated", partida, llegada, "RETRASADO");
        when(vueloRepository.findById(1L)).thenReturn(Optional.of(vueloMock));
        when(vueloRepository.save(any(Vuelo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Vuelo result = vueloService.update(1L, dto);

        assertEquals("CLO", result.getOrigen());
        assertEquals("CTG", result.getDestino());
        assertEquals(EstadoVuelo.RETRASADO, result.getEstadoVuelo());
    }

    @Test
    void update_WhenArrivalBeforeDeparture_ShouldThrowException() {
        VueloDTO dto = new VueloDTO("BOG", "MED", "Desc", llegada, partida, "PUNTUAL");
        when(vueloRepository.findById(1L)).thenReturn(Optional.of(vueloMock));
        assertThrows(BadRequestException.class, () -> vueloService.update(1L, dto));
    }

    @Test
    void delete_WhenExists_ShouldDelete() {
        when(vueloRepository.findById(1L)).thenReturn(Optional.of(vueloMock));
        vueloService.delete(1L);
        verify(vueloRepository).delete(vueloMock);
    }
}
