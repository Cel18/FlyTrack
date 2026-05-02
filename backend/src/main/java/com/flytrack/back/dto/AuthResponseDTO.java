package com.flytrack.back.dto;

public record AuthResponseDTO(
    Long id,
    String token,
    String correo,
    String rol,
    String nombre
) {}
