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
    private String type;
    private String data;

    public LinkDto(URI url, OffsetDateTime updatedAt, OffsetDateTime lastUpdate, String type, String data) {
        this.url = url;
        this.updatedAt = updatedAt;
        this.lastUpdate = lastUpdate;
        this.type = type;
        this.data = data;
    }

    public LinkDto(Long id, URI url, OffsetDateTime updatedAt, OffsetDateTime lastUpdate, String type, String data) {
        this.linkId = id;
        this.url = url;
        this.updatedAt = updatedAt;
        this.lastUpdate = lastUpdate;
        this.type = type;
        this.data = data;
    }
}
