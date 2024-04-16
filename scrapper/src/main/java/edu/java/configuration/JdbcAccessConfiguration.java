package edu.java.configuration;

import edu.java.domain.implementations.jdbc.JdbcChatLinkRepository;
import edu.java.domain.implementations.jdbc.JdbcChatRepository;
import edu.java.domain.implementations.jdbc.JdbcLinkRepository;
import edu.java.servises.handlers.GithubHandler;
import edu.java.servises.handlers.StackOverflowHandler;
import edu.java.servises.interfaces.LinkService;
import edu.java.servises.interfaces.LinkUpdater;
import edu.java.servises.interfaces.TgChatService;
import edu.java.servises.jdbc.JdbcLinkService;
import edu.java.servises.jdbc.JdbcLinkUpdaterService;
import edu.java.servises.jdbc.JdbcTgChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {
    @Bean
    public LinkService linkService(
        JdbcLinkRepository linkRepository,
        JdbcChatLinkRepository chatLinkRepository,
        GithubHandler githubHandler,
        StackOverflowHandler stackOverflowHandler

    ) {
        return new JdbcLinkService(linkRepository, chatLinkRepository, githubHandler, stackOverflowHandler);
    }

    @Bean
    public TgChatService tgChatService(
        JdbcLinkRepository jdbcLinkRepository,
        JdbcChatLinkRepository jdbcChatLinkRepository,
        JdbcChatRepository jdbcChatRepository
    ) {
        return new JdbcTgChatService(jdbcLinkRepository, jdbcChatLinkRepository, jdbcChatRepository);
    }

    @Bean
    public LinkUpdater linkUpdater(
        JdbcLinkRepository jdbcLinkRepository
    ) {
        return new JdbcLinkUpdaterService(jdbcLinkRepository);
    }

}
