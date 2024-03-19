package edu.java.dao.implementations;

import edu.java.dto.jdbc.ChatDto;
import edu.java.dto.jdbc.ChatLinkDto;
import edu.java.dto.jdbc.LinkDto;
import edu.java.errors.LinkWasTrackedException;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;

public class JdbcChatLinkDaoTest extends IntegrationTest {
    @Autowired
    private JdbcChatLinkDao jdbcChatLinkDao;
    @Autowired
    private JdbcChatDao jdbcChatDao;
    @Autowired
    private JdbcLinkDao jdbcLinkDao;

    private static final long CHAT_ID = 14L;
    private static final OffsetDateTime data1 =
        OffsetDateTime.of(2024, 3, 13, 1, 42, 0, 0, ZoneOffset.UTC);
    private static final OffsetDateTime data2 =
        OffsetDateTime.of(2023, 3, 13, 1, 42, 0, 0, ZoneOffset.UTC);
    public static final ChatDto chatDto = new ChatDto(
        CHAT_ID,
        OffsetDateTime.of(2024, 3, 13, 1, 42, 0, 0, ZoneOffset.UTC),
        "Alex"
    );
    private static final LinkDto linkDto =
        new LinkDto(
            URI.create("https://github.com/onevoker/LinkTrackerBot"),data1,data1

            );
    private static final LinkDto linkDto2 =
        new LinkDto(
            URI.create("https://github.com/onevoker/LinkTracker"),
            data2,data2
        );

    @Test
    @Transactional
    @Rollback
    void addTest() {
        jdbcChatDao.add(chatDto);
        jdbcLinkDao.add(linkDto);

        ChatLinkDto chatLink = new ChatLinkDto(CHAT_ID, jdbcLinkDao.findAll().getFirst().getLinkId());
        jdbcChatLinkDao.add(chatLink);

        assertThat(jdbcChatLinkDao.findAll().size()).isEqualTo(1);
        var exception = assertThrows(LinkWasTrackedException.class, () -> jdbcChatLinkDao.add(chatLink));
        assertThat(exception.getMessage()).isEqualTo("Данная ссылка уже отслеживается!");
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        jdbcChatDao.add(chatDto);
        jdbcLinkDao.add(linkDto);
        ChatLinkDto chatLink = new ChatLinkDto(CHAT_ID, jdbcLinkDao.findAll().getFirst().getLinkId());
        jdbcChatLinkDao.add(chatLink);
        assertThat(jdbcChatLinkDao.findAll().size()).isEqualTo(1);
        jdbcChatLinkDao.remove(chatLink);
        assertThat(jdbcChatLinkDao.findAll().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void findAll() throws URISyntaxException {

        jdbcLinkDao.add(linkDto);
        jdbcChatDao.add(chatDto);
        jdbcLinkDao.add(linkDto2);
        jdbcChatLinkDao.add(new ChatLinkDto(chatDto.getChatId(), jdbcLinkDao.findAll().getFirst().getLinkId()));
        jdbcChatLinkDao.add(new ChatLinkDto(chatDto.getChatId(), jdbcLinkDao.findAll().getLast().getLinkId()));
        List<ChatLinkDto> chatLinkDtoList =
            List.of(new ChatLinkDto(chatDto.getChatId(), jdbcLinkDao.findAll().getFirst()
                .getLinkId()), new ChatLinkDto(chatDto.getChatId(), jdbcLinkDao.findAll().getLast()
                .getLinkId()));
        assertThat(jdbcChatLinkDao.findAll()).isEqualTo(chatLinkDtoList);
    }

}
