package com.flytrack.back.service;

import com.flytrack.back.dto.VueloDTO;
import com.flytrack.back.exception.BadRequestException;
import com.flytrack.back.exception.ResourceNotFoundException;
import com.flytrack.back.model.EstadoVuelo;
import com.flytrack.back.model.Notificacion;
import com.flytrack.back.model.Usuario;
import com.flytrack.back.model.Vuelo;
import com.flytrack.back.repository.NotificacionRepository;
import com.flytrack.back.repository.VueloRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VueloService {

    private final VueloRepository vueloRepository;
    private final NotificacionRepository notificacionRepository;
    private final UsuarioService usuarioService;

    public VueloService(VueloRepository vueloRepository,
                        NotificacionRepository notificacionRepository,
                        UsuarioService usuarioService) {
        this.vueloRepository = vueloRepository;
        this.notificacionRepository = notificacionRepository;
        this.usuarioService = usuarioService;
    }

    public List<Vuelo> getAll() {
        return vueloRepository.findAll();
    }

    public List<Vuelo> getByUsuarioId(Long usuarioId) {
        return vueloRepository.findByUsuariosIdUsuario(usuarioId);
    }

    public Vuelo getById(Long id) {
        return vueloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vuelo no encontrado con ID: " + id));
    }

    public Vuelo create(VueloDTO dto) {
        if (dto.horaLlegada().isBefore(dto.horaPartida())) {
            throw new BadRequestException("La hora de llegada no puede ser anterior a la hora de partida.");
        }

        EstadoVuelo estado = dto.estadoVuelo() != null ?
                EstadoVuelo.valueOf(dto.estadoVuelo().toUpperCase()) : EstadoVuelo.PUNTUAL;

        Vuelo vuelo = new Vuelo(
                dto.origen(),
                dto.destino(),
                dto.descripcion(),
                dto.horaPartida(),
                dto.horaLlegada()
        );
        vuelo.setEstadoVuelo(estado);

        return vueloRepository.save(vuelo);
    }

    @Transactional
    public Vuelo update(Long id, VueloDTO dto) {
        Vuelo vuelo = getById(id);

        if (dto.horaLlegada().isBefore(dto.horaPartida())) {
            throw new BadRequestException("La hora de llegada no puede ser anterior a la hora de partida.");
        }

        vuelo.setOrigen(dto.origen());
        vuelo.setDestino(dto.destino());
        vuelo.setDescripcion(dto.descripcion());
        vuelo.setHoraPartida(dto.horaPartida());
        vuelo.setHoraLlegada(dto.horaLlegada());

        if (dto.estadoVuelo() != null) {
            EstadoVuelo nuevoEstado = EstadoVuelo.valueOf(dto.estadoVuelo().toUpperCase());
            EstadoVuelo estadoAnterior = vuelo.getEstadoVuelo();

            if (nuevoEstado != estadoAnterior) {
                vuelo.setEstadoVuelo(nuevoEstado);
                String contenido = String.format(
                        "El vuelo %s → %s ha cambiado de estado: %s → %s",
                        vuelo.getOrigen(), vuelo.getDestino(), estadoAnterior, nuevoEstado);

                for (Usuario usuario : vuelo.getUsuarios()) {
                    notificacionRepository.save(new Notificacion(contenido, usuario, vuelo));
                }
            }
        }

        return vueloRepository.save(vuelo);
    }

    public void delete(Long id) {
        Vuelo vuelo = getById(id);
        vueloRepository.delete(vuelo);
    }

    @Transactional
    public void suscribir(Long vueloId, Long usuarioId) {
        Vuelo vuelo = getById(vueloId);
        Usuario usuario = usuarioService.getById(usuarioId);

        boolean yaInscrito = vuelo.getUsuarios().stream()
                .anyMatch(u -> u.getIdUsuario().equals(usuarioId));
        if (yaInscrito) {
            throw new BadRequestException("El usuario ya está suscrito a este vuelo.");
        }

        vuelo.getUsuarios().add(usuario);
        vueloRepository.save(vuelo);
    }

    @Transactional
    public void desuscribir(Long vueloId, Long usuarioId) {
        Vuelo vuelo = getById(vueloId);

        boolean removido = vuelo.getUsuarios().removeIf(u -> u.getIdUsuario().equals(usuarioId));
        if (!removido) {
            throw new BadRequestException("El usuario no está suscrito a este vuelo.");
        }

        vueloRepository.save(vuelo);
    }
}
