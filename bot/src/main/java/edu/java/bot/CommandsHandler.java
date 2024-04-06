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
import edu.java.bot.commands.commandsExecute.Unsubscribe;
import edu.java.bot.commands.commandsExecute.Untrack;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;

@Component
public class CommandsHandler {
    private final ScrapperClient scrapperClient;
    private final Map<String, Command> commandsExecute;
    private final Map<String, Command> commandsAccess;
    public static final String NONE = "NONE";
    public static final String ADD = "ADD";
    public static final String DEL = "DEL";
    public static final String SERVER_ERROR_RESPONSE = "На данный момент сервер испытывает некоторые сложности...";

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
            new Untrack(scrapperClient),
            "/unsubscribe",
            new Unsubscribe(scrapperClient)
        );
        this.commandsAccess =
            Map.of(ADD, new AddLinkAcceptor(scrapperClient), DEL, new RemoveLinkAcceptor(scrapperClient));
    }

    public SendMessage commandsHandle(Update update) {
        long idChat = update.message().chat().id();
        String text = update.message().text();
        String answer = "Неизвестная команда";
        Command command;
        try {
            if (scrapperClient.isRegister(idChat)) {

                if (!scrapperClient.getState(idChat).state().equals(NONE)) {
                    if (text.equals("/cancel")) {
                        answer = "Вы вышли в меню!";
                        scrapperClient.setState(idChat, NONE);
                    } else {
                        command = commandsAccess.get(scrapperClient.getState(idChat).state());
                        return command.apply(update);
                    }

                }

            }
            if (text != null) {
                command = commandsExecute.get(text);
                if (command != null) {
                    return command.apply(update);
                }
            }
        } catch (WebClientRequestException e) {
            answer = SERVER_ERROR_RESPONSE;
        } catch (Exception e) {
            answer = e.getCause().getMessage();
        }

        return new SendMessage(idChat, answer);

    }
}
