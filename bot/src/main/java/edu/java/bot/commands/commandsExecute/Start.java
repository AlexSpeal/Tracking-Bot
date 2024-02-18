package edu.java.bot.commands.commandsExecute;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.user.User;
import edu.java.bot.user.UsersBase;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class Start implements Command {
    @Override
    public SendMessage apply(Update update, UsersBase usersBase) {
        User user = new User(update.message().chat().username(), update.message().chat().id(), new ArrayList<>());
        if (!usersBase.containId(user.getId())) {
            usersBase.getUserBase().add(user);
            return new SendMessage(update.message().chat().id(), "Добро пожаловать!");
        }
        return new SendMessage(update.message().chat().id(), "Вы уже зарегистрировались!");
    }
}
