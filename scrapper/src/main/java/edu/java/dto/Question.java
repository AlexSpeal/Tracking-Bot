package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record Question(List<ItemResponse> items) {
   public record ItemResponse(@JsonProperty("question_id") long id,
                        @JsonProperty("creation_date") OffsetDateTime creationDate,
                        @JsonProperty("last_edit_date") OffsetDateTime lastEditDate) {
    }
}

