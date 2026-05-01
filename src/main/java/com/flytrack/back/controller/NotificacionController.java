package com.flytrack.back.controller;

import com.flytrack.back.dto.NotificacionDTO;
import com.flytrack.back.model.Notificacion;
import com.flytrack.back.service.NotificacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @PreAuthorize("hasRole('ADMIN') or authentication.name == @usuarioService.getById(#idUsuario).correo")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Notificacion>> getByUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(notificacionService.getByUsuarioId(idUsuario));
    }

    @GetMapping("/vuelo/{idVuelo}")
    public ResponseEntity<List<Notificacion>> getByVuelo(@PathVariable Long idVuelo) {
        return ResponseEntity.ok(notificacionService.getByVueloId(idVuelo));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Notificacion> create(@Valid @RequestBody NotificacionDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificacionService.create(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        notificacionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
