package edu.java.bot.commands.commandsAccess;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.CheckerURL;
import edu.java.bot.commands.Command;
import edu.java.bot.user.State;
import edu.java.bot.user.User;
import edu.java.bot.user.UsersBase;
import lombok.Data;
import static edu.java.bot.user.State.EnumState.NONE;

@Data
public class AddLinkAcceptor implements Command {

    @Override
    public SendMessage apply(Update update, UsersBase usersBase) {

        String text = update.message().text();
        String answer = "Данная ссылка уже отслеживается!";
        if (CheckerURL.check(text)) {
            User user = usersBase.getUser(update.message().chat().id());
            if (!user.getSites().contains(text)) {
                answer = "Ссылка принята!";
                user.getSites().add(text);
            }

            usersBase.getUser(update.message().chat().id()).setState(new State(NONE));
            return new SendMessage(update.message().chat().id(), answer);
        }
        answer = "Некорректная ссылка!";
        return new SendMessage(update.message().chat().id(), answer);
    }

}
