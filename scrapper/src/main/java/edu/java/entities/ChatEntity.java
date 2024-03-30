package edu.java.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat")
@Getter
@Setter
@NoArgsConstructor
public class ChatEntity {
    public ChatEntity(long chatId, OffsetDateTime createdAt, String createdBy, String state) {
        this.chatId = chatId;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.state = state;
    }

    @Id
    private long chatId;
    private OffsetDateTime createdAt;
    private String createdBy;
    private String state;
    @ManyToMany
    @JoinTable(name = "chat_link",
               joinColumns = @JoinColumn(name = "chat_id"),
               inverseJoinColumns = @JoinColumn(name = "link_id"))
    private Set<LinkEntity> links = new HashSet<>();

}
