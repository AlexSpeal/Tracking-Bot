package edu.java.dto.jdbc;

import edu.java.entities.LinkEntity;
import java.net.URI;
import java.net.URISyntaxException;
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

    public LinkDto(LinkEntity linkEntity) {
        this.linkId = linkEntity.getLinkId();
        try {
            this.url = new URI(linkEntity.getUrl());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        this.updatedAt = linkEntity.getUpdatedAt();
        this.lastUpdate = linkEntity.getLastUpdate();
        this.type = linkEntity.getType();
        this.data = linkEntity.getData();
    }
}
