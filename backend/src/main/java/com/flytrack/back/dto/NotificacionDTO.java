package com.flytrack.back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificacionDTO(
    @NotBlank String contenido,
    @NotNull Long usuarioId,
    @NotNull Long vueloId
) {}
