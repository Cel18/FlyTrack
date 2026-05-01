package com.flytrack.back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReporteDTO(
    @NotBlank String descripcion,
    @NotNull Long usuarioId,
    @NotNull Long vueloId
) {}
