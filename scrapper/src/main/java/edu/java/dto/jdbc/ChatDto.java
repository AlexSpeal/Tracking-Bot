package edu.java.dto.jdbc;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {

    private Long chatId;
    private OffsetDateTime createdAt;
    private String createdBy;
}
