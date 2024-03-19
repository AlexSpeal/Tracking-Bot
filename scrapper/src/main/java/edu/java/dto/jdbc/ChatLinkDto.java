package edu.java.dto.jdbc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatLinkDto {

    private Long chatId;
    private Long linkId;
}
