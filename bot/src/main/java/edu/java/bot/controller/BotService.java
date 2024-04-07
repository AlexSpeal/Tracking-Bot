package edu.java.bot.controller;

import com.pengrad.telegrambot.request.SendMessage;
import jakarta.validation.Valid;
import org.example.dto.request.SendUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class BotService {
    private final TelegramBot telegramBot;

    public BotService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendUpdate(@RequestBody @Valid SendUpdateRequest sendUpdateRequest) {
        for (long id : sendUpdateRequest.tgChatIds()) {
            telegramBot.sendUpdate(new SendMessage(id, sendUpdateRequest.description()));
        }
    }
}
