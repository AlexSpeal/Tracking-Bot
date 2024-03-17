package org.example.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import java.net.URI;

public record AddLinkRequest(
    @Schema(description = "Ссылка", example = "https://api.github.com/AlexSpeal")
    @NotNull
    URI link
) {
}
