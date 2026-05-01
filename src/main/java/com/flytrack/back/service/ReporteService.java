package com.flytrack.back.service;

import com.flytrack.back.dto.ReporteDTO;
import com.flytrack.back.exception.ResourceNotFoundException;
import com.flytrack.back.model.EstadoReporte;
import com.flytrack.back.model.Reporte;
import com.flytrack.back.model.Usuario;
import com.flytrack.back.model.Vuelo;
import com.flytrack.back.repository.ReporteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final UsuarioService usuarioService;
    private final VueloService vueloService;

    public ReporteService(ReporteRepository reporteRepository,
                          UsuarioService usuarioService,
                          VueloService vueloService) {
        this.reporteRepository = reporteRepository;
        this.usuarioService = usuarioService;
        this.vueloService = vueloService;
    }

    public List<Reporte> getAll() {
        return reporteRepository.findAll();
    }

    public Reporte getById(Long id) {
        return reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con ID: " + id));
    }

    public List<Reporte> getByUsuarioId(Long idUsuario) {
        return reporteRepository.findByUsuarioIdUsuario(idUsuario);
    }

    public List<Reporte> getByVueloId(Long idVuelo) {
        return reporteRepository.findByVueloIdVuelo(idVuelo);
    }

    public Reporte create(ReporteDTO dto) {
        Usuario usuario = usuarioService.getById(dto.usuarioId());
        Vuelo vuelo = vueloService.getById(dto.vueloId());

        Reporte reporte = new Reporte(
                dto.descripcion(),
                usuario,
                vuelo
        );

        return reporteRepository.save(reporte);
    }

    public Reporte updateEstado(Long id, String estado) {
        Reporte reporte = getById(id);
        reporte.setEstadoReporte(EstadoReporte.valueOf(estado.toUpperCase()));
        return reporteRepository.save(reporte);
    }

    public void delete(Long id) {
        Reporte reporte = getById(id);
        reporteRepository.delete(reporte);
    }
}
