package edu.java.clients;

import edu.java.dto.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
public class GitHubClient {
    @Autowired()
    @Qualifier("getGitHubClient")
    private WebClient webClient;

    @GetMapping("/repos/{name}/{reposName}")
    public Repository getRep(@PathVariable("name") String name, @PathVariable("reposName") String reposName) {
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
