package edu.java.bot.commands.commandsExecute;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.commands.Command;
import edu.java.bot.user.State;
import edu.java.bot.user.User;
import edu.java.bot.user.UsersBase;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Untrack implements Command {
    private final ScrapperClient scrapperClient;

    @Autowired
    public Untrack(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public SendMessage apply(Update update) {
        long idChat = update.message().chat().id();
        String username = update.message().chat().username();
        String answer = "Список пуст, ничего удалять не нужно!";
        try {
            scrapperClient.createChat(idChat, username);
            scrapperClient.deleteChat(idChat);

        } catch (Exception e) {
            if (!scrapperClient.getLinks(idChat).links().isEmpty()) {
                scrapperClient.setState(idChat, "DEL");
                answer = "Вставьте ссылку ( /cancel для отмены ввода)";

            }
            return new SendMessage(idChat, answer);
        }
        return new SendMessage(idChat, "Вы не авторизованы!");
    }
}
