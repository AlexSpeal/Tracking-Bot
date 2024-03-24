package edu.java.bot.commands.commandsExecute;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.user.UsersBase;

public class Help implements Command {
    @Override
    public SendMessage apply(Update update) {
        return new SendMessage(
            update.message().chat().id(),
            "/Start -- зарегистрировать пользователя\n"
                + "/Help -- вывести окно с командами\n"
                + "/Track -- начать отслеживание ссылки\n"
                + "/Untrack -- прекратить отслеживание ссылки\n"
                + "/List -- показать список отслеживаемых ссылок"
        );
    }
}
