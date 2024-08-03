package edu.java.configuration;

import edu.java.domain.implementations.jooq.repository.JooqChatLinkRepository;
import edu.java.domain.implementations.jooq.repository.JooqChatRepository;
import edu.java.domain.implementations.jooq.repository.JooqLinkRepository;
import edu.java.servises.handlers.GithubHandler;
import edu.java.servises.handlers.StackOverflowHandler;
import edu.java.servises.interfaces.LinkService;
import edu.java.servises.interfaces.LinkUpdater;
import edu.java.servises.interfaces.TgChatService;
import edu.java.servises.jooq.JooqLinkService;
import edu.java.servises.jooq.JooqLinkUpdaterService;
import edu.java.servises.jooq.JooqTgChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    @Bean
    public LinkService linkService(
        JooqLinkRepository linkRepository,
        JooqChatLinkRepository chatLinkRepository,
        GithubHandler githubHandler,
        StackOverflowHandler stackOverflowHandler

    ) {
        return new JooqLinkService(linkRepository, chatLinkRepository, githubHandler, stackOverflowHandler);
    }

    @Bean
    public TgChatService tgChatService(
        JooqLinkRepository linkRepository,
        JooqChatLinkRepository chatLinkRepository,
        JooqChatRepository chatRepository
    ) {
        return new JooqTgChatService(chatRepository, linkRepository, chatLinkRepository);
    }

    @Bean
    public LinkUpdater linkUpdater(
        JooqLinkRepository linkRepository
    ) {
        return new JooqLinkUpdaterService(linkRepository);
    }
}
