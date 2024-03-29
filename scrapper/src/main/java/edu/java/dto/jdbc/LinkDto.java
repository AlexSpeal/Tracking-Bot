package edu.java.dto.jdbc;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LinkDto {
    private Long linkId;
    private URI url;
    private OffsetDateTime updatedAt;
    private OffsetDateTime lastUpdate;

    public LinkDto(URI url, OffsetDateTime updatedAt, OffsetDateTime lastUpdate) {
        this.url = url;
        this.updatedAt = updatedAt;
        this.lastUpdate = lastUpdate;
    }

    public LinkDto(Long id, URI url, OffsetDateTime updatedAt, OffsetDateTime lastUpdate) {
        this.linkId = id;
        this.url = url;
        this.updatedAt = updatedAt;
        this.lastUpdate = lastUpdate;
    }
}
