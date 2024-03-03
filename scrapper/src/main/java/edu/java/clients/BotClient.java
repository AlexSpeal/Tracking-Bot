package edu.java.clients;

import org.example.dto.request.SendUpdateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

public class BotClient {
    private final WebClient webClient;

    public BotClient(@Value("${bot-client.base-url-bot}") String url) {
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    public void updates(SendUpdateRequest request) {
        webClient.post().uri("/updates").accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), SendUpdateRequest.class)
            .retrieve().onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Link is not valid"
                ))
            ).onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"
                ))
            ).bodyToMono(Void.class).block();
    }
}
