package edu.java.bot.controller;

import com.pengrad.telegrambot.UpdatesListener;
import edu.java.bot.CommandsHandler;
import edu.java.bot.user.UsersBase;

public class TelegramBot extends com.pengrad.telegrambot.TelegramBot {
    @lombok.Builder
    public TelegramBot(String token) {
        super(token);
    }

    private final CommandsHandler handler = new CommandsHandler();
    private UsersBase usersBase = new UsersBase();

    public void run() {
        this.setUpdatesListener(updates -> {
            updates.forEach(update -> execute(handler.commandsHandle(update, usersBase)));
            return UpdatesListener.CONFIRMED_UPDATES_ALL;

        });
    }
}
