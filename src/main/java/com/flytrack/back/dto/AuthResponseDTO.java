package com.flytrack.back.dto;

public record AuthResponseDTO(
    String token,
    String correo,
    String rol,
    String nombre
) {}
