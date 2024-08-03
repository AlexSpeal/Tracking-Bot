package edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.commandsAccess.AddLinkAcceptor;
import edu.java.bot.commands.commandsAccess.RemoveLinkAcceptor;
import edu.java.bot.commands.commandsExecute.Help;
import edu.java.bot.commands.commandsExecute.List;
import edu.java.bot.commands.commandsExecute.Start;
import edu.java.bot.commands.commandsExecute.Track;
import edu.java.bot.commands.commandsExecute.Untrack;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandsHandler {
    private ScrapperClient scrapperClient;
    private final Map<String, Command> commandsExecute;
    private final Map<String, Command> commandsAccess;
    public static final String NONE = "NONE";
    public static final String ADD = "ADD";
    public static final String DEL = "DEL";

    @Autowired
    public CommandsHandler(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
        this.commandsExecute = Map.of(
            "/start",
            new Start(scrapperClient),
            "/help",
            new Help(),
            "/list",
            new List(scrapperClient),
            "/track",
            new Track(scrapperClient),
            "/untrack",
            new Untrack(scrapperClient)
        );
        this.commandsAccess =
            Map.of(ADD, new AddLinkAcceptor(scrapperClient), DEL, new RemoveLinkAcceptor(scrapperClient));
    }

    public SendMessage commandsHandle(Update update) {
        long idChat = update.message().chat().id();
        String username = update.message().chat().username();
        String text = update.message().text();
        String answer = "Неизвестная команда";
        if (text != null) {
            try {
                scrapperClient.createChat(idChat, username);
                scrapperClient.deleteChat(idChat);
            } catch (Exception e) {
                if (text.equals("/cancel")) {
                    answer = "Вы вышли в меню!";
                    scrapperClient.setState(idChat, NONE);
                } else if (!scrapperClient.getState(idChat).state().equals(NONE)) {
                    Command command = commandsAccess.get(scrapperClient.getState(idChat).state());
                    return command.apply(update);
                }

            }
            Command command = commandsExecute.get(text);
            if (command != null) {
                return command.apply(update);
            }
        }

        return new SendMessage(idChat, answer);
    }
}
