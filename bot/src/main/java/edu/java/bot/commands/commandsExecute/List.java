package edu.java.bot.commands.commandsExecute;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.commands.Command;
import edu.java.bot.user.UsersBase;
import org.example.dto.response.ListLinksResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class List implements Command {
    private final ScrapperClient scrapperClient;

    @Autowired
    public List(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public SendMessage apply(Update update) {
        long idChat = update.message().chat().id();
        String username = update.message().chat().username();
        try {
            scrapperClient.createChat(idChat, username);
            scrapperClient.deleteChat(idChat);

        } catch (Exception e) {
            ListLinksResponse links = scrapperClient.getLinks(update.message().chat().id());
            StringBuilder resultLinks = new StringBuilder();
            if (links.size().equals(0)) {
                resultLinks.append("Отслеживаемых ссылок нет!");
            } else {
                /*resultLinks.append("Отслеживаемые ссылки:").append(System.lineSeparator());
                links.links()
                    .forEach(linkResponse -> resultLinks.append(linkResponse.url()).append(System.lineSeparator()));*/
                for (int i = 0; i < links.size(); ++i) {
                    resultLinks.append((i + 1)).append(". ").append(links.links().get(i).url()).append("\n\n");
                }
            }
            return new SendMessage(idChat, resultLinks.toString());
        }

        return new SendMessage(idChat, "Вы не авторизованы!");

    }
}
