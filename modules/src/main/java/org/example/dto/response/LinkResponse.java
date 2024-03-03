package org.example.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.net.URI;

public record LinkResponse(
    @Schema(description = "Идентификатор",
            example = "14")
    @NotNull
    @Positive
    long id,
    @Schema(nullable = true, example = "https://api.github.com")

    URI url) {
}

