package com.flytrack.back.service;

import com.flytrack.back.dto.AuthResponseDTO;
import com.flytrack.back.dto.LoginRequestDTO;
import com.flytrack.back.dto.RegisterRequestDTO;
import com.flytrack.back.exception.BadRequestException;
import com.flytrack.back.model.Rol;
import com.flytrack.back.model.Usuario;
import com.flytrack.back.repository.UsuarioRepository;
import com.flytrack.back.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public void register(RegisterRequestDTO request) {
        if (usuarioRepository.existsByCorreo(request.correo())) {
            throw new BadRequestException("El correo ya está registrado.");
        }

        Rol rol;
        try {
            rol = Rol.valueOf(request.rol().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Rol inválido. Debe ser USER o ADMIN.");
        }

        Usuario nuevoUsuario = new Usuario(
                request.nombre(),
                request.correo(),
                passwordEncoder.encode(request.password()),
                rol
        );

        usuarioRepository.save(nuevoUsuario);
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        // Autenticar credenciales
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.correo(), request.password())
        );

        // Si llega aquí, es válido. Obtener usuario
        Usuario usuario = usuarioRepository.findByCorreo(request.correo())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado post-autenticación"));

        String token = jwtService.generateToken(usuario.getCorreo(), usuario.getRol().name());

        return new AuthResponseDTO(
                token,
                usuario.getCorreo(),
                usuario.getRol().name(),
                usuario.getNombre()
        );
    }
}
