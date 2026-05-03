package com.flytrack.back.service;

import com.flytrack.back.dto.NotificacionDTO;
import com.flytrack.back.exception.ResourceNotFoundException;
import com.flytrack.back.model.Notificacion;
import com.flytrack.back.model.Usuario;
import com.flytrack.back.model.Vuelo;
import com.flytrack.back.repository.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private VueloService vueloService;

    @InjectMocks
    private NotificacionService notificacionService;

    private Vuelo vueloMock;
    private Usuario usuarioMock;
    private Notificacion notificacionMock;

    @BeforeEach
    void setUp() {
        vueloMock = new Vuelo();
        vueloMock.setIdVuelo(1L);

        usuarioMock = new Usuario();
        usuarioMock.setIdUsuario(1L);
        usuarioMock.setCorreo("pasajero@test.com");

        notificacionMock = new Notificacion("Vuelo retrasado 30 minutos", usuarioMock, vueloMock);
        notificacionMock.setId(1L);
    }

    @Test
    void shouldCreateNotificacionSuccessfully() {
        NotificacionDTO dto = new NotificacionDTO("Vuelo retrasado 30 minutos", 1L, 1L);

        when(usuarioService.getById(1L)).thenReturn(usuarioMock);
        when(vueloService.getById(1L)).thenReturn(vueloMock);
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacionMock);

        Notificacion result = notificacionService.create(dto);

        assertNotNull(result);
        assertEquals("Vuelo retrasado 30 minutos", result.getContenido());
        assertEquals(usuarioMock, result.getDestinatario());
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }

    @Test
    void shouldReturnNotificacionById() {
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacionMock));

        Notificacion result = notificacionService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(notificacionRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenNotificacionNotFound() {
        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> notificacionService.getById(99L));
        verify(notificacionRepository, times(1)).findById(99L);
    }

    @Test
    void shouldReturnNotificacionesByUsuarioId() {
        when(notificacionRepository.findByDestinatarioIdUsuario(1L)).thenReturn(List.of(notificacionMock));

        List<Notificacion> result = notificacionService.getByUsuarioId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(notificacionRepository, times(1)).findByDestinatarioIdUsuario(1L);
    }

    @Test
    void shouldDeleteNotificacionSuccessfully() {
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacionMock));
        doNothing().when(notificacionRepository).delete(notificacionMock);

        notificacionService.delete(1L);

        verify(notificacionRepository, times(1)).delete(notificacionMock);
    }
}
