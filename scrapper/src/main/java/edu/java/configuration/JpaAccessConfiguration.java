package edu.java.configuration;

import edu.java.domain.implementations.jpa.JpaChatRepository;
import edu.java.domain.implementations.jpa.JpaLinkRepository;
import edu.java.servises.handlers.GithubHandler;
import edu.java.servises.handlers.StackOverflowHandler;
import edu.java.servises.interfaces.LinkService;
import edu.java.servises.interfaces.LinkUpdater;
import edu.java.servises.interfaces.TgChatService;
import edu.java.servises.jpa.JpaChatService;
import edu.java.servises.jpa.JpaLinkService;
import edu.java.servises.jpa.JpaLinkUpdaterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {

    @Bean
    public LinkService linkService(
        JpaChatRepository jpaChatRepository,
        JpaLinkRepository jpaLinkRepository,
        GithubHandler githubHandler,
        StackOverflowHandler stackOverflowHandler
    ) {
        return new JpaLinkService(jpaChatRepository, jpaLinkRepository, githubHandler, stackOverflowHandler);
    }

    @Bean
    public TgChatService tgChatService(
        JpaChatRepository jpaChatRepository, JpaLinkRepository jpaLinkRepository
    ) {
        return new JpaChatService(jpaChatRepository, jpaLinkRepository);
    }

    @Bean
    public LinkUpdater linkUpdater(
        JpaLinkRepository jpaLinkRepository
    ) {
        return new JpaLinkUpdaterService(jpaLinkRepository);
    }
}
