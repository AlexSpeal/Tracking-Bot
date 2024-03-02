package edu.java.clients;

import edu.java.dto.Repository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

public class GitHubClient {
    private final WebClient webClient;

    public GitHubClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Repository getRep(String name, String reposName) {
        return webClient.get().uri("/repos/{name}/{reposName}", name, reposName)
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
            ).bodyToMono(Repository.class).block();
    }
}
