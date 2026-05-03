package com.flytrack.back.controller;

import com.flytrack.back.dto.ReporteDTO;
import com.flytrack.back.model.Reporte;
import com.flytrack.back.service.ReporteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Reporte>> getAll() {
        return ResponseEntity.ok(reporteService.getAll());
    }

    @PreAuthorize("hasRole('ADMIN') or authentication.name == @reporteService.getById(#id).usuario.correo")
    @GetMapping("/{id}")
    public ResponseEntity<Reporte> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN') or authentication.name == @usuarioService.getById(#idUsuario).correo")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Reporte>> getByUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(reporteService.getByUsuarioId(idUsuario));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/vuelo/{idVuelo}")
    public ResponseEntity<List<Reporte>> getByVuelo(@PathVariable Long idVuelo) {
        return ResponseEntity.ok(reporteService.getByVueloId(idVuelo));
    }

    @PostMapping
    public ResponseEntity<Reporte> create(@Valid @RequestBody ReporteDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteService.create(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Reporte> updateEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String estado = body.get("estado");
        return ResponseEntity.ok(reporteService.updateEstado(id, estado));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reporteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
