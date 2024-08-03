package edu.java.bot.controller;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.CommandsHandler;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.configuration.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;

public class TelegramBot extends com.pengrad.telegrambot.TelegramBot {
    private final CommandsHandler handler;

    @Autowired
    public TelegramBot(ApplicationConfig token, ScrapperClient scrapperClient) {
        super(token.telegramToken());
        handler = new CommandsHandler(scrapperClient);
    }

    public void run() {
        this.setUpdatesListener(updates -> {
            updates.forEach(update -> execute(handler.commandsHandle(update)));
            return UpdatesListener.CONFIRMED_UPDATES_ALL;

        });
    }

    public void sendUpdate(SendMessage sendMessage) {
        execute(sendMessage);
    }
}
