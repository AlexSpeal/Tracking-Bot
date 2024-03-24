package edu.java.bot.commands.commandsAccess;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.commands.Command;
import org.example.dto.LinkParser;
import org.example.dto.request.AddLinkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.URISyntaxException;

@Component
public class AddLinkAcceptor implements Command {
    private final ScrapperClient scrapperClient;

    @Autowired
    public AddLinkAcceptor(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public SendMessage apply(Update update) {
        String text = update.message().text();
        String answer = "Данная ссылка уже отслеживается!";
        long idChat = update.message().chat().id();
        if (LinkParser.check(text)) {
            if (scrapperClient.getLinks(idChat).links().stream()
                .noneMatch(link -> link.url().toString().equals(text))) {
                answer = "Ссылка принята!";
                try {
                    scrapperClient.setLink(idChat, new AddLinkRequest(new URI(text)));

                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }

            }
            scrapperClient.setState(idChat, "NONE");
            return new SendMessage(idChat, answer);
        }
        answer = "Некорректная ссылка!";
        return new SendMessage(idChat, answer);

    }

}
