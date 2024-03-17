package edu.java.dto.jdbc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {

    private Long chat_id;
    private OffsetDateTime createdAt;
    private String createdBy;
}
