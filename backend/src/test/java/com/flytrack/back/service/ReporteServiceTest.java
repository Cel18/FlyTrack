package com.flytrack.back.service;

import com.flytrack.back.dto.ReporteDTO;
import com.flytrack.back.model.EstadoReporte;
import com.flytrack.back.model.Reporte;
import com.flytrack.back.model.Usuario;
import com.flytrack.back.model.Vuelo;
import com.flytrack.back.repository.ReporteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private VueloService vueloService;

    @InjectMocks
    private ReporteService reporteService;

    private Vuelo vueloMock;
    private Usuario usuarioMock;
    private Reporte reporteMock;

    @BeforeEach
    void setUp() {
        vueloMock = new Vuelo();
        vueloMock.setIdVuelo(1L);

        usuarioMock = new Usuario();
        usuarioMock.setIdUsuario(1L);
        usuarioMock.setCorreo("test@test.com");

        reporteMock = new Reporte("Maleta perdida", usuarioMock, vueloMock);
        reporteMock.setId(1L);
        reporteMock.setEstadoReporte(EstadoReporte.PENDIENTE);
    }

    @Test
    void shouldCreateReporteSuccessfully() {
        ReporteDTO dto = new ReporteDTO("Maleta perdida", 1L, 1L);

        when(vueloService.getById(1L)).thenReturn(vueloMock);
        when(usuarioService.getById(1L)).thenReturn(usuarioMock);
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporteMock);

        Reporte result = reporteService.create(dto);

        assertNotNull(result);
        assertEquals("Maleta perdida", result.getDescripcion());
        assertEquals(EstadoReporte.PENDIENTE, result.getEstadoReporte());
        verify(reporteRepository, times(1)).save(any(Reporte.class));
    }

    @Test
    void shouldReturnReporteById() {
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporteMock));

        Reporte result = reporteService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(reporteRepository, times(1)).findById(1L);
    }
}
