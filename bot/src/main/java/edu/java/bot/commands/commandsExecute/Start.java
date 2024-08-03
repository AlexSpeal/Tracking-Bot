package edu.java.bot.commands.commandsExecute;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.commands.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Start implements Command {
    private final ScrapperClient scrapperClient;

    @Autowired
    public Start(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public SendMessage apply(Update update) {
        long idChat = update.message().chat().id();
        String username = update.message().chat().username();

        if (scrapperClient.isRegister(idChat)) {
            return new SendMessage(idChat, "Вы уже зарегистрированы!");
        }
        scrapperClient.createChat(idChat, username);
        return new SendMessage(idChat, "Добро пожаловать, " + username);

    }
}
