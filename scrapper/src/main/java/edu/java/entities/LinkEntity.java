package edu.java.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "link")
@Getter
@Setter
@NoArgsConstructor
public class LinkEntity {
    public LinkEntity(
        String url,
        OffsetDateTime updatedAt,
        OffsetDateTime lastUpdate,
        String type,
        String data,
        Set<ChatEntity> chats
    ) {
        this.url = url;
        this.updatedAt = updatedAt;
        this.lastUpdate = lastUpdate;
        this.type = type;
        this.data = data;
        this.chats = chats;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long linkId;
    private String url;
    private OffsetDateTime updatedAt;
    private OffsetDateTime lastUpdate;
    private String type;
    private String data;
    @ManyToMany(mappedBy = "links")
    private Set<ChatEntity> chats = new HashSet<>();
}
