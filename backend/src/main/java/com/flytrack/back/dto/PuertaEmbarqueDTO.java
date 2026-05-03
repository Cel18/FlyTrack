package com.flytrack.back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record PuertaEmbarqueDTO(
    @NotBlank String codigo,
    @NotBlank String terminal,
    LocalDateTime fechaCierre,
    @NotNull Long vueloId
) {}
