package com.flytrack.back.dto;

/**
 * DTO para listar usuarios en el panel de admin sin exponer el hash de la contraseña.
 */
public record UsuarioResumenDTO(
        Long idUsuario,
        String nombre,
        String correo,
        String rol,
        String estadoCuenta
) {}
