package edu.java.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.example.dto.request.AddLinkRequest;
import org.example.dto.request.SendUpdateRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class BotUnitTest {
    private WireMockServer wireMockServer;
    private BotClient client;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
        client = new BotClient("http://localhost:" + wireMockServer.port());
    }

    @AfterEach
    public void teardown() {
        wireMockServer.stop();
    }

    @Test
    public void updates() {
        List<Long> list = new ArrayList<>();
        list.add(0, 2L);
        var request = new SendUpdateRequest(1L, "https://api.github.com", "лол", list);
        stubFor(post(urlEqualTo("/updates"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
            ));

        assertDoesNotThrow(() -> client.updates(request));
    }
}
