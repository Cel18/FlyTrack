package com.flytrack.back.controller;

import com.flytrack.back.dto.VueloDTO;
import com.flytrack.back.model.Vuelo;
import com.flytrack.back.service.VueloService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vuelos")
public class VueloController {

    private final VueloService vueloService;

    public VueloController(VueloService vueloService) {
        this.vueloService = vueloService;
    }

    @GetMapping
    public ResponseEntity<List<Vuelo>> getAll() {
        return ResponseEntity.ok(vueloService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vuelo> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vueloService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN') or authentication.name == @usuarioService.getById(#usuarioId).correo")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Vuelo>> getByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(vueloService.getByUsuarioId(usuarioId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Vuelo> create(@Valid @RequestBody VueloDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vueloService.create(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Vuelo> update(@PathVariable Long id, @Valid @RequestBody VueloDTO request) {
        return ResponseEntity.ok(vueloService.update(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vueloService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or authentication.name == @usuarioService.getById(#usuarioId).correo")
    @PostMapping("/{id}/usuarios/{usuarioId}")
    public ResponseEntity<Void> suscribir(@PathVariable Long id, @PathVariable Long usuarioId) {
        vueloService.suscribir(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or authentication.name == @usuarioService.getById(#usuarioId).correo")
    @DeleteMapping("/{id}/usuarios/{usuarioId}")
    public ResponseEntity<Void> desuscribir(@PathVariable Long id, @PathVariable Long usuarioId) {
        vueloService.desuscribir(id, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
