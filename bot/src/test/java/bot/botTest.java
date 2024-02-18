package bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.commandsExecute.Start;
import edu.java.bot.user.UsersBase;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class botTest {

    /*@Test
    @DisplayName("Test /start command")
    void test1() {
        UsersBase base=new UsersBase();
        Command startCommand = new Start();
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/start");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response=startCommand.apply(update,base);
        Assert.assertArrayEquals();
    }*/
}
