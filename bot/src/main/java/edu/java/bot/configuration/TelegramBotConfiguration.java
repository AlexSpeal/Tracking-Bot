package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.controller.TelegramBot;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfiguration {
    @Autowired
    private ScrapperClient scrapperClient;
    @Autowired CompositeMeterRegistry meterRegistry;

    @Bean
    public TelegramBot botRun(
        ApplicationConfig applicationConfig,
        ScrapperClient scrapperClient,
        CompositeMeterRegistry meterRegistry
    ) {
        TelegramBot bot = new TelegramBot(applicationConfig, scrapperClient, meterRegistry);
        bot.run();
        return bot;
    }

}
