package com.flytrack.back.controller;

import com.flytrack.back.dto.UsuarioResumenDTO;
import com.flytrack.back.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UsuarioResumenDTO>> getAll() {
        List<UsuarioResumenDTO> result = usuarioService.getAll().stream()
                .map(u -> new UsuarioResumenDTO(
                        u.getIdUsuario(),
                        u.getNombre(),
                        u.getCorreo(),
                        u.getRol().name(),
                        u.getEstadoCuenta().name()
                ))
                .toList();
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN') or authentication.name == @usuarioService.getById(#id).correo")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResumenDTO> getById(@PathVariable Long id) {
        var u = usuarioService.getById(id);
        return ResponseEntity.ok(new UsuarioResumenDTO(
                u.getIdUsuario(), u.getNombre(), u.getCorreo(),
                u.getRol().name(), u.getEstadoCuenta().name()
        ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
