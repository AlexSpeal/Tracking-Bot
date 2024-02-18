package edu.java.bot.commands.commandsExecute;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.user.State;
import edu.java.bot.user.User;
import edu.java.bot.user.UsersBase;
import java.util.ArrayList;

public class Track implements Command {

    @Override
    public SendMessage apply(Update update, UsersBase usersBase) {
        User user = new User(update.message().chat().username(), update.message().chat().id(), new ArrayList<>());
        if (!usersBase.containId(user.getId())) {
            return new SendMessage(update.message().chat().id(), "Вы не авторизованы!");
        } else {
            usersBase.getUser(user.getId()).setState(new State(State.EnumState.FILE_ADD));
            return new SendMessage(update.message().chat().id(), "Вставьте ссылку ( /cancel для отмены ввода)");
        }
    }
}
