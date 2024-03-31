package bot;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.commandsAccess.AddLinkAcceptor;
import edu.java.bot.commands.commandsAccess.RemoveLinkAcceptor;
import edu.java.bot.commands.commandsExecute.Start;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.example.dto.response.LinkResponse;
import org.example.dto.response.ListLinksResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class botTest {
    private static final String LINK_FIRST = "https://github.com/AlexSpeal/Tracking-Bot";
    private static final Long ID = 12L;
    private static final WireMockServer server = new WireMockServer();
    ScrapperClient scrapperClient = new ScrapperClient(WebClient.builder().baseUrl("http://localhost:8080").build());

    @AfterAll
    static void tearDown() {
        server.stop();
    }

    @BeforeAll
    static void serverUp() {
        server.start();
    }

    @Test
    @DisplayName("Test /start command")
    void startCommandTest() {
        Command startCommand = new Start(scrapperClient);
        stubFor(post(urlEqualTo("/tg-chat/1")).willReturn(aResponse().withStatus(200)));
        Update update = mock(Update.class);
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().text()).thenReturn("/start");
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().username()).thenReturn("AlexSpeal");
        when(update.message().chat().id()).thenReturn(1L);
        SendMessage response = startCommand.apply(update);
        assertThat("Добро пожаловать, AlexSpeal").isEqualTo(response.getParameters().get("text"));

    }

    @Test
    @DisplayName("Test /test with authorization")
    void CommandsHandlerTest() {
        Command startCommand = new Start(scrapperClient);
        stubFor(post(urlEqualTo("/tg-chat/1")).willReturn(aResponse().withStatus(500)));
        Update update = mock(Update.class);
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().text()).thenReturn("/start");
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().username()).thenReturn("AlexSpeal");
        when(update.message().chat().id()).thenReturn(1L);
        SendMessage response = startCommand.apply(update);
        assertThat("Вы уже зарегистрированы!").isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test checking the inclusion of links in the database")
    void IncludeLinksTest() {
        List<LinkResponse> listLinkResponse =
            List.of(new LinkResponse(10L, URI.create("https://github.com/AlexSpeal/Track")));
        ListLinksResponse listLinksResponse = new ListLinksResponse(listLinkResponse, 1);
        ScrapperClient scrapperClient1 = mock(ScrapperClient.class);
        when(scrapperClient1.getLinks(anyLong())).thenReturn(listLinksResponse);
        AddLinkAcceptor addLinkAcceptor = new AddLinkAcceptor(scrapperClient1);
        Update update = mock(Update.class);
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().text()).thenReturn(LINK_FIRST);
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(ID);
        SendMessage response = addLinkAcceptor.apply(update);
        assertThat("Ссылка принята!").isEqualTo(response.getParameters().get("text"));

    }

    @Test
    @DisplayName("Test checking the remove of links in the database")
    void RemoveLinksTest() {

        List<LinkResponse> listLinkResponse =
            List.of(new LinkResponse(10L, URI.create(LINK_FIRST)));
        ListLinksResponse listLinksResponse = new ListLinksResponse(listLinkResponse, 1);
        ScrapperClient scrapperClient1 = mock(ScrapperClient.class);
        when(scrapperClient1.getLinks(anyLong())).thenReturn(listLinksResponse);
        RemoveLinkAcceptor removeLinkAcceptor = new RemoveLinkAcceptor(scrapperClient1);
        Update update = mock(Update.class);
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().text()).thenReturn(LINK_FIRST);
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(ID);
        SendMessage response = removeLinkAcceptor.apply(update);
        assertThat("Ссылка удалена!").isEqualTo(response.getParameters().get("text"));

    }

    @Test
    @DisplayName("Test checking the correct operation of the command /list")
    void listTest() {

        List<LinkResponse> listLinkResponse =
            List.of(new LinkResponse(10L, URI.create(LINK_FIRST)));
        ListLinksResponse listLinksResponse = new ListLinksResponse(listLinkResponse, 1);
        ScrapperClient scrapperClient1 = mock(ScrapperClient.class);
        when(scrapperClient1.getLinks(anyLong())).thenReturn(listLinksResponse);
        doThrow(RuntimeException.class).when(scrapperClient1).deleteChat(anyLong());
        edu.java.bot.commands.commandsExecute.List list =
            new edu.java.bot.commands.commandsExecute.List(scrapperClient1);
        Update update = mock(Update.class);
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().text()).thenReturn(LINK_FIRST);
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(ID);
        SendMessage response = list.apply(update);
        assertThat("1. https://github.com/AlexSpeal/Tracking-Bot\n" + "\n").isEqualTo(response.getParameters()
            .get("text"));

    }

    @Test
    @DisplayName("Test checking the correct operation of the command /list if the user does not contain links")
    void listTestWithOutLinks() {

        ScrapperClient scrapperClient1 = mock(ScrapperClient.class);
        when(scrapperClient1.getLinks(anyLong())).thenReturn(new ListLinksResponse(new ArrayList<>(), 0));
        doThrow(RuntimeException.class).when(scrapperClient1).deleteChat(anyLong());
        edu.java.bot.commands.commandsExecute.List list =
            new edu.java.bot.commands.commandsExecute.List(scrapperClient1);
        Update update = mock(Update.class);
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().text()).thenReturn(LINK_FIRST);
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(ID);
        SendMessage response = list.apply(update);
        assertThat("Отслеживаемых ссылок нет!").isEqualTo(response.getParameters()
            .get("text"));
    }
}
