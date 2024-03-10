package bot;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.configuration.ScrapperClientConfiguration;
import org.example.dto.request.AddLinkRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ScrapperUnitTest {
    private WireMockServer wireMockServer;
    private ScrapperClient client;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
        client = new ScrapperClientConfiguration().getScrapperClient("http://localhost:" + wireMockServer.port());
    }

    @AfterEach
    public void teardown() {
        wireMockServer.stop();
    }

    @Test
    public void createLink() {
        var request = new AddLinkRequest("https://api.github.com");
        var response = """
            {
              "id": 1,
              "url": "https://api.github.com"
            }
            """;

        stubFor(post(urlEqualTo("/links"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(response)
            ));

        var clientResponse = client.setLink(1L, request);
        assertThat(clientResponse.id()).isEqualTo(1L);
        assertThat(clientResponse.url().toString()).isEqualTo("https://api.github.com");
    }

    @Test
    public void deleteLink() {
        var request = new AddLinkRequest("https://api.github.com");
        var response = """
            {
              "id": 1,
              "url": "https://api.github.com"
            }
            """;

        stubFor(delete(urlEqualTo("/links"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(response)
            ));

        var clientResponse = client.deleteLink(1L, request);
        assertThat(clientResponse.id()).isEqualTo(1L);
        assertThat(clientResponse.url().toString()).isEqualTo("https://api.github.com");
    }

}
