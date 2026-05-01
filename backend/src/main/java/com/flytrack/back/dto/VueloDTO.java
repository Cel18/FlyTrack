package com.flytrack.back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record VueloDTO(
    @NotBlank String origen,
    @NotBlank String destino,
    String descripcion,
    @NotNull LocalDateTime horaPartida,
    @NotNull LocalDateTime horaLlegada,
    String estadoVuelo
) {}
