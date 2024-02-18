package edu.java.bot.commands.commandsAccess;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.user.State;
import edu.java.bot.user.User;
import edu.java.bot.user.UsersBase;
import static edu.java.bot.user.State.EnumState.NONE;

public class RemoveLinkAcceptor implements Command {
    @Override
    public SendMessage apply(Update update, UsersBase usersBase) {
        String text = update.message().text();
        User user = usersBase.getUser(update.message().chat().id());
        String answer = "Вы ввели неправильную ссылку";
        if (user.getSites().contains(text)) {
            user.getSites().remove(text);
            answer = "Ссылка удалена";
            usersBase.getUser(update.message().chat().id()).setState(new State(NONE));
            return new SendMessage(update.message().chat().id(), answer);
        }
        return new SendMessage(update.message().chat().id(), answer);
    }
}
