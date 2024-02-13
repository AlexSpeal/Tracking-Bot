package edu.java.bot.controller;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import edu.java.bot.configuration.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class TelegramBot extends com.pengrad.telegrambot.TelegramBot {
    @lombok.Builder
    public TelegramBot(ApplicationConfig config) {
        super(config.telegramToken());
    }

    public void run() {
        this.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;

        });
    }

    private void process(Update update) {
        Message message = update.message();
        if (message != null) {
            String text = message.text();
            Optional.ofNullable(text).ifPresent(command -> this.command(command, message.chat().id()));
        }
    }

    private void command(String command, Long id) {
        switch (command) {
            case "/start": {
                SendMessage response = new SendMessage(id, "Добро пожаловать!");
                this.execute(response);
                break;
            }
            case "/track": {
                SendMessage response = new SendMessage(id, "Я пока такого не умею(");
                this.execute(response);
                break;
            }
            case "/help": {
                SendMessage response = new SendMessage(
                    id,
                    "/start -- зарегистрировать пользователя\n" +
                        "/help -- вывести окно с командами\n" +
                        "/track -- начать отслеживание ссылки\n" +
                        "/untrack -- прекратить отслеживание ссылки\n" +
                        "/list -- показать список отслеживаемых ссылок"
                );
                this.execute(response);
                break;
            }
            default:
                SendMessage response = new SendMessage(id, "Введена неправильная команда");
                this.execute(response);

        }
    }
}
