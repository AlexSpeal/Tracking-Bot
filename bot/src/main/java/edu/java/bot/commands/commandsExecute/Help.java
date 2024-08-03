package edu.java.bot.commands.commandsExecute;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;

public class Help implements Command {
    @Override
    public SendMessage apply(Update update) {
        return new SendMessage(
            update.message().chat().id(),
            "/start -- зарегистрировать пользователя\n"
                + "/help -- вывести окно с командами\n"
                + "/track -- начать отслеживание ссылки\n"
                + "/untrack -- прекратить отслеживание ссылки\n"
                + "/list -- показать список отслеживаемых ссылок"
        );
    }
}
