package edu.java.bot.configuration;

import edu.java.bot.controller.TelegramBot;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {

    @Autowired
    private ApplicationConfig applicationConfig;

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }

    @PostConstruct
    private void runBot() {
        TelegramBot bot = new TelegramBot(applicationConfig.telegramToken());
        bot.run();
    }
}
