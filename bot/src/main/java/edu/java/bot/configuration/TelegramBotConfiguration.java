package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.controller.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfiguration {
    @Autowired
    private ScrapperClient scrapperClient;

    @Bean
    public TelegramBot botRun(ApplicationConfig applicationConfig, ScrapperClient scrapperClient) {
        TelegramBot bot = new TelegramBot(applicationConfig, scrapperClient);
        bot.run();
        return bot;
    }

}
