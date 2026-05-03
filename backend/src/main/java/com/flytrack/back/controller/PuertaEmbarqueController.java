package com.flytrack.back.controller;

import com.flytrack.back.dto.PuertaEmbarqueDTO;
import com.flytrack.back.model.PuertaEmbarque;
import com.flytrack.back.service.PuertaEmbarqueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/puertas")
public class PuertaEmbarqueController {

    private final PuertaEmbarqueService puertaService;

    public PuertaEmbarqueController(PuertaEmbarqueService puertaService) {
        this.puertaService = puertaService;
    }

    @GetMapping
    public ResponseEntity<List<PuertaEmbarque>> getAll() {
        return ResponseEntity.ok(puertaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PuertaEmbarque> getById(@PathVariable Long id) {
        return ResponseEntity.ok(puertaService.getById(id));
    }

    @GetMapping("/vuelo/{idVuelo}")
    public ResponseEntity<PuertaEmbarque> getByVuelo(@PathVariable Long idVuelo) {
        return ResponseEntity.ok(puertaService.getByVueloId(idVuelo));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PuertaEmbarque> create(@Valid @RequestBody PuertaEmbarqueDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(puertaService.create(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PuertaEmbarque> update(@PathVariable Long id, @Valid @RequestBody PuertaEmbarqueDTO request) {
        return ResponseEntity.ok(puertaService.update(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        puertaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
