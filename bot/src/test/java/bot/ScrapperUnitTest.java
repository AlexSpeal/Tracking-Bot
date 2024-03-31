package bot;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.BotApplication;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.configuration.ScrapperClientConfiguration;
import org.example.dto.request.AddLinkRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {BotApplication.class})
@WireMockTest
public class ScrapperUnitTest {
    @Autowired
    private ScrapperClient client;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicPort()).build();

    @DynamicPropertySource
    public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("app.base-url-scrapper", wireMockExtension::baseUrl);
    }

    @Test
    public void createLink() throws URISyntaxException {
        var request = new AddLinkRequest(new URI("https://api.github.com"));
        var response = """
            {
              "id": 1,
              "url": "https://api.github.com"
            }
            """;

        wireMockExtension.stubFor(post(urlEqualTo("/links"))
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
    public void deleteLink() throws URISyntaxException {
        var request = new AddLinkRequest(new URI("https://api.github.com"));
        var response = """
            {
              "id": 1,
              "url": "https://api.github.com"
            }
            """;

        wireMockExtension.stubFor(delete(urlEqualTo("/links"))
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
