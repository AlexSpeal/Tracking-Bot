package org.example.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record SendUpdateRequest(
    @Schema(description = "Идентификатор",
            example = "14")
    @NotNull
    @Positive
    long id,
    @Schema(nullable = true, example = "https://api.github.com")
    String url,
    @Schema(nullable = true, example = "Какое-то описание")
    String description,
    @Schema(description = "Массив айдишников чатов",
            example = "[1,2,3,4]")
    @NotNull
    List<Long> tgChatIds
) {
}
