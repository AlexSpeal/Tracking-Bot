package edu.java.domain.implementations.jooq.repository;

import edu.java.dto.jdbc.LinkDto;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;

@DirtiesContext
class JooqLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JooqLinkRepository jooqLinkRepository;

    @Test
    @Transactional
    @Rollback
    void add() throws URISyntaxException {
        OffsetDateTime data = OffsetDateTime.parse("2022-01-01T10:10:00+00:00");
        LinkDto linkDto = new LinkDto(new URI("https://github.com/"), data, data, "github", "{бурури,парирам}");
        jooqLinkRepository.add(linkDto);
        assertThat(jooqLinkRepository.findAll().size()).isEqualTo(1);
        assertThrows(DuplicateKeyException.class, () -> jooqLinkRepository.add(linkDto));
    }

    @Test
    @Transactional
    @Rollback
    void remove() throws URISyntaxException {
        OffsetDateTime data = OffsetDateTime.parse("2022-01-01T10:30:00+00:00");
        LinkDto linkDto = new LinkDto(new URI("https://github.com/"), data, data, "github", "{мими,мамому}");
        jooqLinkRepository.add(linkDto);

        assertThat(jooqLinkRepository.findAll().size()).isEqualTo(1);
        jooqLinkRepository.remove(jooqLinkRepository.findAll().getFirst().getLinkId());
        assertThat(jooqLinkRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void findAll() throws URISyntaxException {

        OffsetDateTime data = OffsetDateTime.parse("2022-01-01T10:30:00+00:00");
        LinkDto linkDto1 = new LinkDto(1L, new URI("https://github.com/"), data, data, "github", "{мими,мамому}");
        data = OffsetDateTime.parse("2021-01-01T10:30:00+00:00");
        LinkDto linkDto2 =
            new LinkDto(2L, new URI("https://stackoverflow.com/"), data, data, "github", "{мими,мамому}");
        jooqLinkRepository.add(linkDto1);
        var x = jooqLinkRepository.findAll().getFirst().getLinkId();
        jooqLinkRepository.add(linkDto2);
        List<LinkDto> linkDtoList = List.of(linkDto1, linkDto2);
        assertThat(jooqLinkRepository.findAll().getFirst().getUrl().toString()).isEqualTo(linkDtoList.getFirst()
            .getUrl().toString());
        assertThat(jooqLinkRepository.findAll().getLast().getUrl().toString()).isEqualTo(linkDtoList.getLast().getUrl()
            .toString());
    }
}
