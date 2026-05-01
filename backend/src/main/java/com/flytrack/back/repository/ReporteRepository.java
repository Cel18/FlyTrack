package com.flytrack.back.repository;

import com.flytrack.back.model.EstadoReporte;
import com.flytrack.back.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByUsuarioIdUsuario(Long idUsuario);
    List<Reporte> findByVueloIdVuelo(Long idVuelo);
    List<Reporte> findByEstadoReporte(EstadoReporte estadoReporte);
}
