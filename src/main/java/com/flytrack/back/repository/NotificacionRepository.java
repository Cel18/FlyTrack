package com.flytrack.back.repository;

import com.flytrack.back.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByDestinatarioIdUsuario(Long idUsuario);
    List<Notificacion> findByVueloIdVuelo(Long idVuelo);
}
