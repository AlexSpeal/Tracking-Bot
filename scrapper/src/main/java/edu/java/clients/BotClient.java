package edu.java.clients;

import org.example.dto.request.SendUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class BotClient {
    private final WebClient webClient;
    @Autowired
    private Retry retry;

    public BotClient(WebClient webClient) {
        this.webClient = webClient;
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
            ).bodyToMono(Void.class).retryWhen(retry).block();
    }
}
