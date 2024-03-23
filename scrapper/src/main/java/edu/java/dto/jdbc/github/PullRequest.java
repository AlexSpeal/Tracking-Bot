package edu.java.dto.jdbc.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record PullRequest(@JsonProperty("number") int number,
                          @JsonProperty("title") String title,
                          @JsonProperty("created_at") OffsetDateTime createdAt) {

}
