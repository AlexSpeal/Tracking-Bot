package edu.java.bot.configuration;

import edu.java.bot.controller.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfiguration {
    @Bean
    public TelegramBot botRun(ApplicationConfig applicationConfig) {
        TelegramBot bot = new TelegramBot(applicationConfig.telegramToken());
        bot.run();
        return bot;
    }

}
