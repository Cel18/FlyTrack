package com.flytrack.back.service;

import com.flytrack.back.exception.ResourceNotFoundException;
import com.flytrack.back.model.Rol;
import com.flytrack.back.model.Usuario;
import com.flytrack.back.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> getAll() {
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getRol() != Rol.ADMIN)
                .toList();
    }

    public Usuario getById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }

    public void delete(Long id) {
        Usuario usuario = getById(id);
        usuarioRepository.delete(usuario);
    }
}
