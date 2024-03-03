package org.example.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ListLinksResponse(
    @Schema(description = "Массив ссылок")
    @NotNull
    LinkResponse[] links,
    @Schema(description = "Размер", example = "132")
    @NotNull
    Integer size
) {
}
