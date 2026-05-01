package com.flytrack.back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
    @NotBlank String nombre,
    @NotBlank @Email String correo,
    @NotBlank @Size(min = 6) String password,
    @NotBlank String rol
) {}
