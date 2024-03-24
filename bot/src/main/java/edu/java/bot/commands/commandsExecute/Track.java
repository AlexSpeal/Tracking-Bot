package edu.java.bot.commands.commandsExecute;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.commands.Command;
import edu.java.bot.user.State;
import edu.java.bot.user.User;
import edu.java.bot.user.UsersBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class Track implements Command {
    private final ScrapperClient scrapperClient;

    @Autowired
    public Track(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public SendMessage apply(Update update) {
       /* User user = new User(update.message().chat().username(), update.message().chat().id(), new ArrayList<>());
        if (!usersBase.containId(user.getId())) {
            return new SendMessage(update.message().chat().id(), "Вы не авторизованы!");
        } else {
            usersBase.getUser(user.getId()).setState(new State(State.EnumState.FILE_ADD));

        }*/
        long idChat = update.message().chat().id();
        String username = update.message().chat().username();
        try {
            scrapperClient.createChat(idChat, username);
            scrapperClient.deleteChat(idChat);

        } catch (Exception e) {
            scrapperClient.setState(idChat, "ADD");
            return new SendMessage(idChat, "Вставьте ссылку ( /cancel для отмены ввода)");
        }
        return new SendMessage(idChat, "Вы не авторизованы!");
    }
}
