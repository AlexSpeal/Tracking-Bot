package edu.java.domain.implementations.jdbc;

import edu.java.domain.implementations.jdbc.JdbcChatLinkRepository;
import edu.java.domain.implementations.jdbc.JdbcChatRepository;
import edu.java.domain.implementations.jdbc.JdbcLinkRepository;
import edu.java.dto.jdbc.ChatDto;
import edu.java.dto.jdbc.ChatLinkDto;
import edu.java.dto.jdbc.LinkDto;
import edu.java.scrapper.IntegrationTest;
import errors.LinkWasTrackedException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
@DirtiesContext
public class JdbcChatLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcChatLinkRepository jdbcChatLinkRepository;
    @Autowired
    private JdbcChatRepository jdbcChatRepository;
    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    private static final long CHAT_ID = 14L;
    private static final OffsetDateTime data1 =
        OffsetDateTime.of(2024, 3, 13, 1, 42, 0, 0, ZoneOffset.UTC);
    private static final OffsetDateTime data2 =
        OffsetDateTime.of(2023, 3, 13, 1, 42, 0, 0, ZoneOffset.UTC);
    public static final ChatDto chatDto = new ChatDto(
        CHAT_ID,
        OffsetDateTime.of(2024, 3, 13, 1, 42, 0, 0, ZoneOffset.UTC),
        "Alex","NONE"
    );
    private static final LinkDto linkDto =
        new LinkDto(
            URI.create("https://github.com/onevoker/LinkTrackerBot"),data1,data1
,"github","{бурури,парирам}"
            );
    private static final LinkDto linkDto2 =
        new LinkDto(
            URI.create("https://github.com/onevoker/LinkTracker"),
            data2,data2,"github","{мими,мамому}"
        );

    @Test
    @Transactional
    @Rollback
    void addTest() {
        jdbcChatRepository.add(chatDto);
        jdbcLinkRepository.add(linkDto);

        ChatLinkDto chatLink = new ChatLinkDto(CHAT_ID, jdbcLinkRepository.findAll().getFirst().getLinkId());
        jdbcChatLinkRepository.add(chatLink);

        assertThat(jdbcChatLinkRepository.findAll().size()).isEqualTo(1);
        var exception = assertThrows(LinkWasTrackedException.class, () -> jdbcChatLinkRepository.add(chatLink));
        assertThat(exception.getMessage()).isEqualTo("Данная ссылка уже отслеживается!");
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        jdbcChatRepository.add(chatDto);
        jdbcLinkRepository.add(linkDto);
        ChatLinkDto chatLink = new ChatLinkDto(CHAT_ID, jdbcLinkRepository.findAll().getFirst().getLinkId());
        jdbcChatLinkRepository.add(chatLink);
        assertThat(jdbcChatLinkRepository.findAll().size()).isEqualTo(1);
        jdbcChatLinkRepository.remove(chatLink);
        assertThat(jdbcChatLinkRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void findAll() throws URISyntaxException {

        jdbcLinkRepository.add(linkDto);
        jdbcChatRepository.add(chatDto);
        jdbcLinkRepository.add(linkDto2);
        jdbcChatLinkRepository.add(new ChatLinkDto(chatDto.getChatId(), jdbcLinkRepository.findAll().getFirst().getLinkId()));
        jdbcChatLinkRepository.add(new ChatLinkDto(chatDto.getChatId(), jdbcLinkRepository.findAll().getLast().getLinkId()));
        List<ChatLinkDto> chatLinkDtoList =
            List.of(new ChatLinkDto(chatDto.getChatId(), jdbcLinkRepository.findAll().getFirst()
                .getLinkId()), new ChatLinkDto(chatDto.getChatId(), jdbcLinkRepository.findAll().getLast()
                .getLinkId()));
        assertThat(jdbcChatLinkRepository.findAll()).isEqualTo(chatLinkDtoList);
    }

}
