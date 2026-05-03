package com.flytrack.back.service;

import com.flytrack.back.dto.PuertaEmbarqueDTO;
import com.flytrack.back.exception.BadRequestException;
import com.flytrack.back.exception.ResourceNotFoundException;
import com.flytrack.back.model.PuertaEmbarque;
import com.flytrack.back.model.Vuelo;
import com.flytrack.back.repository.PuertaEmbarqueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PuertaEmbarqueService {

    private final PuertaEmbarqueRepository puertaRepository;
    private final VueloService vueloService;

    public PuertaEmbarqueService(PuertaEmbarqueRepository puertaRepository, VueloService vueloService) {
        this.puertaRepository = puertaRepository;
        this.vueloService = vueloService;
    }

    public List<PuertaEmbarque> getAll() {
        return puertaRepository.findAll();
    }

    public PuertaEmbarque getById(Long id) {
        return puertaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Puerta de embarque no encontrada con ID: " + id));
    }

    public PuertaEmbarque getByVueloId(Long idVuelo) {
        return puertaRepository.findByVueloIdVuelo(idVuelo)
                .orElseThrow(() -> new ResourceNotFoundException("Puerta de embarque no encontrada para el vuelo: " + idVuelo));
    }

    public PuertaEmbarque create(PuertaEmbarqueDTO dto) {
        if (puertaRepository.existsByVueloIdVuelo(dto.vueloId())) {
            throw new BadRequestException("Este vuelo ya tiene una puerta de embarque asignada.");
        }

        Vuelo vuelo = vueloService.getById(dto.vueloId());

        PuertaEmbarque puerta = new PuertaEmbarque(
                dto.codigo(),
                dto.terminal(),
                dto.fechaCierre(),
                vuelo
        );

        return puertaRepository.save(puerta);
    }

    public PuertaEmbarque update(Long id, PuertaEmbarqueDTO dto) {
        PuertaEmbarque puerta = getById(id);

        puerta.setCodigo(dto.codigo());
        puerta.setTerminal(dto.terminal());
        puerta.setFechaCierre(dto.fechaCierre());

        if (!puerta.getVuelo().getIdVuelo().equals(dto.vueloId())) {
            if (puertaRepository.existsByVueloIdVuelo(dto.vueloId())) {
                throw new BadRequestException("El nuevo vuelo ya tiene una puerta de embarque asignada.");
            }
            Vuelo vuelo = vueloService.getById(dto.vueloId());
            puerta.setVuelo(vuelo);
        }

        return puertaRepository.save(puerta);
    }

    public void delete(Long id) {
        PuertaEmbarque puerta = getById(id);
        puertaRepository.delete(puerta);
    }
}
