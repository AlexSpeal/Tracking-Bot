package edu.java.bot.commands.commandsAccess;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.commands.Command;
import java.net.URI;
import java.net.URISyntaxException;
import org.example.dto.LinkParser;
import org.example.dto.request.AddLinkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class RemoveLinkAcceptor implements Command {
    private final ScrapperClient scrapperClient;

    @Autowired
    public RemoveLinkAcceptor(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public SendMessage apply(Update update) {
        String text = update.message().text();
        String answer = "Вы ввели неправильную ссылку!";
        long idChat = update.message().chat().id();

        if (LinkParser.check(text)) {
            if (scrapperClient.getLinks(idChat).links().stream()
                .anyMatch(link -> link.url().toString().equals(text))) {
                try {
                    scrapperClient.deleteLink(idChat, new AddLinkRequest(new URI(text)));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
                answer = "Ссылка удалена!";
                scrapperClient.setState(idChat, "NONE");

            } else {
                answer = "У вас отсутсвует данная ссылка!";
                return new SendMessage(idChat, answer);
            }

        }
        return new SendMessage(idChat, answer);

    }
}
