package edu.java.bot.commands.commandsExecute;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.commands.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Unsubscribe implements Command {
    @Autowired
    private final ScrapperClient scrapperClient;

    public Unsubscribe(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public SendMessage apply(Update update) {
        long idChat = update.message().chat().id();

        if (scrapperClient.isRegister(idChat)) {
            scrapperClient.deleteChat(idChat);
            return new SendMessage(idChat, "Вы отписались от бота. Жаль...");
        }
        return new SendMessage(idChat, "Для отписки нужна подписка)");

    }
}
