package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.controller.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {
    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
        TelegramBot bot = edu.java.bot.controller.TelegramBot.builder()
            .config(new ApplicationConfig("6278402620:AAHqZgjAbOMGFZl8tY6jW66gPoWz_Uy0Ukw")).build();
        bot.run();
    }
}
