package edu.java.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.dto.jdbc.github.Github;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest
class GitHubClientTest {
    static final String BODY_REQUEST = "{\"name\": \"vkusnoe_brevno\",\"pushed_at\": \"123\"}";
    static final String BODY_REQUEST2 = "[{\"name\": \"main\",\"commit\": {\"sha\": \"qwerty\"}}\n]";
    static final String BODY_REQUEST3 = "[{\"number\": 1,\"title\": \"zxc\",\"created_at\": \"2024-03-23T20:13:54Z\"}]";
    static final String URL = "/repos/AlexBebrovich/Plotina";
    static final String URL2 = "/repos/AlexBebrovich/Plotina/branches";
    static final String URL3 = "/repos/AlexBebrovich/Plotina/pulls";
    static final String ERROR_404 = "404 NOT_FOUND \"Link is not valid\"";
    static final String ERROR_500 = "500 INTERNAL_SERVER_ERROR \"Internal Server Error\"";
    GitHubClient gitHubClient = new GitHubClient(WebClient.builder().baseUrl("http://localhost:8080").build());
    private static final WireMockServer wireMockServer = new WireMockServer();

    @AfterAll
    static void tearDown() {
        wireMockServer.stop();
    }

    @BeforeAll
    static void serverUp() {
        wireMockServer.start();
        configureFor("localhost", 8080);
    }

    @Test
    @DisplayName("regular work")
    public void testRegWork() {
        stubFor(WireMock.get(WireMock.urlEqualTo(
            URL)
        ).willReturn(aResponse()
            .withBody(BODY_REQUEST)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        ));

        stubFor(WireMock.get(WireMock.urlEqualTo(
            URL2)
        ).willReturn(aResponse()
            .withBody(BODY_REQUEST2)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        ));

        stubFor(WireMock.get(WireMock.urlEqualTo(
            URL3)
        ).willReturn(aResponse()
            .withBody(BODY_REQUEST3)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        ));
        Github rep = gitHubClient.getRep("AlexBebrovich", "Plotina");
        assertEquals("vkusnoe_brevno", rep.repository().name());
    }

    @Test
    @DisplayName("link error")
    public void testLinkError() {
        stubFor(WireMock.get(WireMock.urlEqualTo(
            URL)
        ).willReturn(aResponse()
            .withBody(BODY_REQUEST)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        ));

        String message = "";
        try {
            gitHubClient.getRep("AlexNone", "Plotina");
        } catch (ResponseStatusException e) {
            message = e.getMessage();
        }
        assertEquals(ERROR_404, message);
    }

    @Test
    @DisplayName("server error")
    public void testServerError() {
        stubFor(WireMock.get(WireMock.urlEqualTo(
            URL)
        ).willReturn(aResponse()
            .withBody(BODY_REQUEST).withStatus(500)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        ));

        String message = "";
        try {
            gitHubClient.getRep("AlexBebrovich", "Plotina");
        } catch (ResponseStatusException e) {
            message = e.getMessage();
        }
        assertEquals(ERROR_500, message);
    }
}
