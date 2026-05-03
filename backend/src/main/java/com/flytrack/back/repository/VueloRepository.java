package com.flytrack.back.repository;

import com.flytrack.back.model.EstadoVuelo;
import com.flytrack.back.model.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VueloRepository extends JpaRepository<Vuelo, Long> {
    List<Vuelo> findByEstadoVuelo(EstadoVuelo estadoVuelo);
    List<Vuelo> findByOrigenAndDestino(String origen, String destino);
}
