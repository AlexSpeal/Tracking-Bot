package edu.java.dto.jdbc.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record Repository(@JsonProperty("name") String name,
                         @JsonProperty("pushed_at") OffsetDateTime pushedAt) {

}
