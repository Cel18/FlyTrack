package com.flytrack.back.service;

import com.flytrack.back.dto.VueloDTO;
import com.flytrack.back.exception.BadRequestException;
import com.flytrack.back.exception.ResourceNotFoundException;
import com.flytrack.back.model.EstadoVuelo;
import com.flytrack.back.model.Vuelo;
import com.flytrack.back.repository.VueloRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VueloService {

    private final VueloRepository vueloRepository;

    public VueloService(VueloRepository vueloRepository) {
        this.vueloRepository = vueloRepository;
    }

    public List<Vuelo> getAll() {
        return vueloRepository.findAll();
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
            vuelo.setEstadoVuelo(EstadoVuelo.valueOf(dto.estadoVuelo().toUpperCase()));
        }

        return vueloRepository.save(vuelo);
    }

    public void delete(Long id) {
        Vuelo vuelo = getById(id);
        vueloRepository.delete(vuelo);
    }
}
