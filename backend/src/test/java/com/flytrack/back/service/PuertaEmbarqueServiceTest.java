package com.flytrack.back.service;

import com.flytrack.back.dto.PuertaEmbarqueDTO;
import com.flytrack.back.exception.BadRequestException;
import com.flytrack.back.exception.ResourceNotFoundException;
import com.flytrack.back.model.PuertaEmbarque;
import com.flytrack.back.model.Vuelo;
import com.flytrack.back.repository.PuertaEmbarqueRepository;
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
class PuertaEmbarqueServiceTest {

    @Mock
    private PuertaEmbarqueRepository puertaRepository;

    @Mock
    private VueloService vueloService;

    @InjectMocks
    private PuertaEmbarqueService puertaEmbarqueService;

    private PuertaEmbarque puerta;
    private Vuelo vuelo;
    private PuertaEmbarqueDTO dto;

    @BeforeEach
    void setUp() {
        vuelo = new Vuelo();
        vuelo.setIdVuelo(1L);

        puerta = new PuertaEmbarque("A1", "T1", LocalDateTime.now().plusHours(2), vuelo);
        puerta.setIdPuerta(1L);

        dto = new PuertaEmbarqueDTO("A1", "T1", LocalDateTime.now().plusHours(2), 1L);
    }

    @Test
    void getAll_ShouldReturnList() {
        when(puertaRepository.findAll()).thenReturn(Arrays.asList(puerta));
        List<PuertaEmbarque> result = puertaEmbarqueService.getAll();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getById_WhenExists_ShouldReturnPuerta() {
        when(puertaRepository.findById(1L)).thenReturn(Optional.of(puerta));
        PuertaEmbarque result = puertaEmbarqueService.getById(1L);
        assertNotNull(result);
        assertEquals("A1", result.getCodigo());
    }

    @Test
    void getById_WhenNotExists_ShouldThrowException() {
        when(puertaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> puertaEmbarqueService.getById(1L));
    }

    @Test
    void getByVueloId_WhenExists_ShouldReturnPuerta() {
        when(puertaRepository.findByVueloIdVuelo(1L)).thenReturn(Optional.of(puerta));
        PuertaEmbarque result = puertaEmbarqueService.getByVueloId(1L);
        assertNotNull(result);
        assertEquals(1L, result.getVuelo().getIdVuelo());
    }

    @Test
    void getByVueloId_WhenNotExists_ShouldThrowException() {
        when(puertaRepository.findByVueloIdVuelo(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> puertaEmbarqueService.getByVueloId(1L));
    }

    @Test
    void create_WhenVueloHasNoGate_ShouldCreate() {
        when(puertaRepository.existsByVueloIdVuelo(1L)).thenReturn(false);
        when(vueloService.getById(1L)).thenReturn(vuelo);
        when(puertaRepository.save(any(PuertaEmbarque.class))).thenReturn(puerta);

        PuertaEmbarque result = puertaEmbarqueService.create(dto);

        assertNotNull(result);
        verify(puertaRepository).save(any(PuertaEmbarque.class));
    }

    @Test
    void create_WhenVueloAlreadyHasGate_ShouldThrowException() {
        when(puertaRepository.existsByVueloIdVuelo(1L)).thenReturn(true);
        assertThrows(BadRequestException.class, () -> puertaEmbarqueService.create(dto));
    }

    @Test
    void update_WhenValid_ShouldUpdate() {
        when(puertaRepository.findById(1L)).thenReturn(Optional.of(puerta));
        when(puertaRepository.save(any(PuertaEmbarque.class))).thenReturn(puerta);

        PuertaEmbarque result = puertaEmbarqueService.update(1L, dto);

        assertNotNull(result);
        verify(puertaRepository).save(any(PuertaEmbarque.class));
    }

    @Test
    void update_WhenChangingVueloAndNewVueloHasGate_ShouldThrowException() {
        Vuelo newVuelo = new Vuelo();
        newVuelo.setIdVuelo(2L);
        PuertaEmbarqueDTO newDto = new PuertaEmbarqueDTO("A2", "T1", LocalDateTime.now(), 2L);

        when(puertaRepository.findById(1L)).thenReturn(Optional.of(puerta));
        when(puertaRepository.existsByVueloIdVuelo(2L)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> puertaEmbarqueService.update(1L, newDto));
    }

    @Test
    void update_WhenChangingVueloAndNewVueloIsAvailable_ShouldUpdate() {
        Vuelo newVuelo = new Vuelo();
        newVuelo.setIdVuelo(2L);
        PuertaEmbarqueDTO newDto = new PuertaEmbarqueDTO("A2", "T1", LocalDateTime.now(), 2L);

        when(puertaRepository.findById(1L)).thenReturn(Optional.of(puerta));
        when(puertaRepository.existsByVueloIdVuelo(2L)).thenReturn(false);
        when(vueloService.getById(2L)).thenReturn(newVuelo);
        when(puertaRepository.save(any(PuertaEmbarque.class))).thenReturn(puerta);

        PuertaEmbarque result = puertaEmbarqueService.update(1L, newDto);

        assertNotNull(result);
        assertEquals(2L, puerta.getVuelo().getIdVuelo());
    }

    @Test
    void delete_WhenExists_ShouldDelete() {
        when(puertaRepository.findById(1L)).thenReturn(Optional.of(puerta));
        doNothing().when(puertaRepository).delete(puerta);

        assertDoesNotThrow(() -> puertaEmbarqueService.delete(1L));
        verify(puertaRepository).delete(puerta);
    }
}
