package edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.commandsAccess.AddLinkAcceptor;
import edu.java.bot.commands.commandsAccess.RemoveLinkAcceptor;
import edu.java.bot.commands.commandsExecute.Help;
import edu.java.bot.commands.commandsExecute.List;
import edu.java.bot.commands.commandsExecute.Start;
import edu.java.bot.commands.commandsExecute.Track;
import edu.java.bot.commands.commandsExecute.Untrack;
import edu.java.bot.user.State;
import edu.java.bot.user.User;
import edu.java.bot.user.UsersBase;
import java.util.ArrayList;
import java.util.Map;
import static edu.java.bot.user.State.EnumState.FILE_ADD;
import static edu.java.bot.user.State.EnumState.FILE_DEL;
import static edu.java.bot.user.State.EnumState.NONE;

public class CommandsHandler {
    private final Map<String, Command> commandsExecute;
    private final Map<State.EnumState, Command> commandsAccess;

    public CommandsHandler() {
        this.commandsExecute = Map.of("/start", new Start(), "/help", new Help(),
            "/list", new List(), "/track", new Track(), "/untrack", new Untrack()
        );
        this.commandsAccess = Map.of(FILE_ADD, new AddLinkAcceptor(), FILE_DEL, new RemoveLinkAcceptor());
    }

    public SendMessage commandsHandle(Update update, UsersBase usersBase) {
        User user = new User(update.message().chat().username(), update.message().chat().id(), new ArrayList<>());
        User userInBase = usersBase.getUser(user.getId());
        String text = update.message().text();
        String answer = "Неизвестная команда";
        if (userInBase != null) {
            if (text.equals("/cancel")) {
                answer = "Вы вышли в меню!";
                userInBase.getState().setNowState(NONE);
            } else if (!userInBase.getState().getNowState().equals(NONE)) {
                Command command = commandsAccess.get(userInBase.getState().getNowState());
                return command.apply(update, usersBase);
            }
        }

        Command command = commandsExecute.get(text);
        if (command != null) {
            return command.apply(update, usersBase);
        }
        return new SendMessage(update.message().chat().id(), answer);
    }
}
