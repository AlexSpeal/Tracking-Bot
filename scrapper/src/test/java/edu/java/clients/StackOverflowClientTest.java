package edu.java.clients;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.ScrapperApplication;
import edu.java.dto.jdbc.stackOverflow.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ScrapperApplication.class})
@WireMockTest
@DirtiesContext
class StackOverflowClientTest {
    static final String BODY_REQUEST = "{\"items\":[{\"question_id\":148}]}";
    static final String URL = "/questions/52841620?site=stackoverflow";

    @Autowired
    StackOverflowClient stackOverflowClient;
    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicPort()).build();

    @DynamicPropertySource
    public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("app.base-url.stack-overflow-base-url", wireMockExtension::baseUrl);
    }

    @Test
    @DisplayName("regular work")
    public void testRegWork() {
        wireMockExtension.stubFor(WireMock.get(WireMock.urlEqualTo(
            URL)
        ).willReturn(aResponse()
            .withBody(BODY_REQUEST)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        ));
        Question question = stackOverflowClient.getQuestion(52841620);
        assertEquals(148, question.items().getFirst().id());
    }

    @Test
    @DisplayName("link error")
    public void testLinkError() {
        wireMockExtension.stubFor(WireMock.get(WireMock.urlEqualTo(
            URL)
        ).willReturn(aResponse()
            .withBody(BODY_REQUEST)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        ));
        assertThrows(RuntimeException.class, () -> stackOverflowClient.getQuestion(777));
    }

    @Test
    @DisplayName("server error")
    public void testServerError() {
        wireMockExtension.stubFor(WireMock.get(WireMock.urlEqualTo(
            URL)
        ).willReturn(aResponse()
            .withBody(BODY_REQUEST).withStatus(500)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        ));

        assertThrows(RuntimeException.class, () -> stackOverflowClient.getQuestion(52841620));
    }
}
