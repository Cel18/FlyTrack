package com.flytrack.back.repository;

import com.flytrack.back.model.PuertaEmbarque;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PuertaEmbarqueRepository extends JpaRepository<PuertaEmbarque, Long> {
    Optional<PuertaEmbarque> findByVueloIdVuelo(Long idVuelo);
    boolean existsByVueloIdVuelo(Long idVuelo);
}
