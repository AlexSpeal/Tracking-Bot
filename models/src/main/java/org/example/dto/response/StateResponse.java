package org.example.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record StateResponse(
    @Schema(description = "Состояние")
    @NotNull String state
) {
}
