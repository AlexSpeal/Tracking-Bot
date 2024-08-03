package edu.java.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.controller.TelegramBot;
import org.example.dto.request.SendUpdateRequest;
import org.springframework.stereotype.Service;

@Service
public class BotService {
    private final TelegramBot telegramBot;

    public BotService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendUpdate(SendUpdateRequest sendUpdateRequest) {
        for (long id : sendUpdateRequest.tgChatIds()) {
            telegramBot.sendUpdate(new SendMessage(id, sendUpdateRequest.description()));
        }
    }
}
