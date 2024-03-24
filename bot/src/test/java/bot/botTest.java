/*
package bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.CommandsHandler;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.commandsAccess.AddLinkAcceptor;
import edu.java.bot.commands.commandsAccess.RemoveLinkAcceptor;
import edu.java.bot.commands.commandsExecute.List;
import edu.java.bot.commands.commandsExecute.Start;
import edu.java.bot.user.User;
import edu.java.bot.user.UsersBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class botTest {
    private static final String LINK_FIRST = "https://github.com/AlexSpeal";
    private static final String LINK_SECOND = "https://github.com/aleckbb";
    private static final Long ID = 12L;

    @Test
    @DisplayName("Test /start command")
    void startCommandTest() {
        UsersBase base = new UsersBase();
        Command startCommand = new Start();
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/start");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response = startCommand.apply(update, base);

        assertThat("Добро пожаловать!").isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test valid CommandsHandler without authorization")
    void CommandsHandlerTest() {
        UsersBase base = new UsersBase();
        Message message = mock(Message.class);
        Update update = mock(Update.class);
        Chat chat = mock(Chat.class);
        CommandsHandler commandsHandler = new CommandsHandler();
        when(update.message()).thenReturn(message);
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().username()).thenReturn("alexspeal");
        when(update.message().chat().id()).thenReturn(ID);
        when(update.message().text()).thenReturn("/track");
        assertThat("Вы не авторизованы!")
            .isEqualTo(commandsHandler.commandsHandle(update, base).getParameters().get("text"));
    }

    @Test
    @DisplayName("Test checking the inclusion of links in the database")
    void IncludeLinksTest() {

        UsersBase usersBase = new UsersBase();
        User user = new User("Alex", ID, new ArrayList<>());
        usersBase.getUserBase().add(user);
        AddLinkAcceptor addLinkAcceptor = new AddLinkAcceptor();
        Message message = mock(Message.class);
        Update update = mock(Update.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(update.message().text()).thenReturn(LINK_FIRST);
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(ID);
        SendMessage response = addLinkAcceptor.apply(update, usersBase);
        assertTrue(usersBase.getUser(ID).getSites().contains(LINK_FIRST));

    }

    @Test
    @DisplayName("Test checking the remove of links in the database")
    void RemoveLinksTest() {

        UsersBase usersBase = new UsersBase();
        User user = new User("Alex", ID, new ArrayList<>());
        user.getSites().add(LINK_FIRST);
        usersBase.getUserBase().add(user);
        RemoveLinkAcceptor removeLinkAcceptor = new RemoveLinkAcceptor();
        Message message = mock(Message.class);
        Update update = mock(Update.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(update.message().text()).thenReturn(LINK_FIRST);
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(ID);
        SendMessage response = removeLinkAcceptor.apply(update, usersBase);
        assertFalse(usersBase.getUser(ID).getSites().contains(LINK_FIRST));

    }

    @Test
    @DisplayName("Test checking the correct operation of the command /list")
    void listTest() {

        UsersBase usersBase = new UsersBase();
        List listCommand = new List();
        User user = new User("Alex", ID, new ArrayList<>());
        usersBase.getUserBase().add(user);
        usersBase.getUser(ID).getSites().add(LINK_FIRST);
        usersBase.getUser(ID).getSites().add(LINK_SECOND);
        Message message = mock(Message.class);
        Update update = mock(Update.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(update.message().text()).thenReturn("/list");
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().username()).thenReturn("Alex");
        when(update.message().chat().id()).thenReturn(ID);
        SendMessage response = listCommand.apply(update, usersBase);
        assertThat("1. https://github.com/AlexSpeal\n"
            + "\n" + "2. https://github.com/aleckbb\n"
            + "\n").isEqualTo(response.getParameters().get("text"));

    }

    @Test
    @DisplayName("Test checking the correct operation of the command /list if the user does not contain links")
    void listTestWithOutLinks() {

        UsersBase usersBase = new UsersBase();
        List listCommand = new List();
        User user = new User("Alex", ID, new ArrayList<>());
        usersBase.getUserBase().add(user);
        Message message = mock(Message.class);
        Update update = mock(Update.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(update.message().text()).thenReturn("/list");
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().username()).thenReturn("Alex");
        when(update.message().chat().id()).thenReturn(ID);
        SendMessage response = listCommand.apply(update, usersBase);
        assertThat("Список отслеживаемых ссылок пуст!").isEqualTo(response.getParameters().get("text"));
    }
}
*/
