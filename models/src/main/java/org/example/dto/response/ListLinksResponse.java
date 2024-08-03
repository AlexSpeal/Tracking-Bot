package org.example.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ListLinksResponse(
    @Schema(description = "Массив ссылок")
    @NotNull
    List<LinkResponse> links,
    @Schema(description = "Размер", example = "132")
    @NotNull
    Integer size
) {
}
