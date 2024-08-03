package edu.java.bot.commands.commandsExecute;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.commands.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Track implements Command {
    private final ScrapperClient scrapperClient;

    @Autowired
    public Track(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public SendMessage apply(Update update) {
        long idChat = update.message().chat().id();

        if (scrapperClient.isRegister(idChat)) {
            scrapperClient.setState(idChat, "ADD");
            return new SendMessage(idChat, "Вставьте ссылку ( /cancel для отмены ввода)");
        }
        return new SendMessage(idChat, "Вы не авторизованы!");

    }
}
