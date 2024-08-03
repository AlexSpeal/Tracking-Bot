package bot;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.BotApplication;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {BotApplication.class})
@WireMockTest
class botTest {
    private static final String LINK_FIRST = "https://github.com/AlexSpeal/Tracking-Bot";
    private static final Long ID = 12L;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicPort()).build();

    @DynamicPropertySource
    public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("app.base-url-scrapper", wireMockExtension::baseUrl);
    }

    @Test
    @DisplayName("Test /start command")
    void startCommandTest() {
        ScrapperClient scrapperClient1 = mock(ScrapperClient.class);
        Command startCommand = new Start(scrapperClient1);
        when(scrapperClient1.isRegister(anyLong())).thenReturn(false);
        wireMockExtension.stubFor(post(urlEqualTo("/tg-chat/1")).willReturn(aResponse().withStatus(200)));
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
        ScrapperClient scrapperClient1 = mock(ScrapperClient.class);
        Command startCommand = new Start(scrapperClient1);
        wireMockExtension.stubFor(post(urlEqualTo("/tg-chat/1")).willReturn(aResponse().withStatus(500)));
        Update update = mock(Update.class);
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().text()).thenReturn("/start");
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().username()).thenReturn("AlexSpeal");
        when(update.message().chat().id()).thenReturn(1L);
        when(scrapperClient1.isRegister(anyLong())).thenReturn(true);
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
        when(scrapperClient1.isRegister(anyLong())).thenReturn(true);
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
        when(scrapperClient1.isRegister(anyLong())).thenReturn(true);
        SendMessage response = list.apply(update);
        assertThat("Отслеживаемых ссылок нет!").isEqualTo(response.getParameters()
            .get("text"));
    }
}
