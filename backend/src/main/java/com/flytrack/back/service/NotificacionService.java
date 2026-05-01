package com.flytrack.back.service;

import com.flytrack.back.dto.NotificacionDTO;
import com.flytrack.back.exception.ResourceNotFoundException;
import com.flytrack.back.model.Notificacion;
import com.flytrack.back.model.Usuario;
import com.flytrack.back.model.Vuelo;
import com.flytrack.back.repository.NotificacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioService usuarioService;
    private final VueloService vueloService;

    public NotificacionService(NotificacionRepository notificacionRepository,
            UsuarioService usuarioService,
            VueloService vueloService) {
        this.notificacionRepository = notificacionRepository;
        this.usuarioService = usuarioService;
        this.vueloService = vueloService;
    }

    public List<Notificacion> getByVueloId(Long idVuelo) {
        return notificacionRepository.findByVueloIdVuelo(idVuelo);
    }

    public List<Notificacion> getByUsuarioId(Long idUsuario) {
        return notificacionRepository.findByDestinatarioIdUsuario(idUsuario);
    }

    public Notificacion getById(Long id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con ID: " + id));
    }

    public Notificacion create(NotificacionDTO dto) {
        Usuario destinatario = usuarioService.getById(dto.usuarioId());
        Vuelo vuelo = vueloService.getById(dto.vueloId());

        Notificacion notificacion = new Notificacion(
                dto.contenido(),
                destinatario,
                vuelo);

        return notificacionRepository.save(notificacion);
    }

    public void delete(Long id) {
        Notificacion notificacion = getById(id);
        notificacionRepository.delete(notificacion);
    }
}
