package edu.java.clients;

import edu.java.dto.jdbc.github.Branch;
import edu.java.dto.jdbc.github.Github;
import edu.java.dto.jdbc.github.PullRequest;
import edu.java.dto.jdbc.github.Repository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

public class GitHubClient {
    private final WebClient webClient;
    private final static String LINK_IS_NOT_VALID = "Link is not valid";
    private final static String INTERNAL_SERVER_ERROR = "Internal Server Error";

    public GitHubClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Github getRep(String name, String reposName) {
        Repository repository = webClient.get().uri("/repos/{name}/{reposName}", name, reposName)
            .retrieve().onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new ResponseStatusException(
                    HttpStatus.NOT_FOUND, LINK_IS_NOT_VALID
                ))
            ).onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR
                ))
            ).bodyToMono(Repository.class).block();
        Branch[] branches = webClient.get().uri("/repos/{name}/{repo}/branches", name, reposName)
            .retrieve().onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new ResponseStatusException(
                    HttpStatus.NOT_FOUND, LINK_IS_NOT_VALID
                ))
            ).onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR
                ))
            ).bodyToMono(Branch[].class).block();
        PullRequest[] pullRequests = webClient.get().uri("/repos/{name}/{repo}/pulls", name, reposName)
            .retrieve().onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new ResponseStatusException(
                    HttpStatus.NOT_FOUND, LINK_IS_NOT_VALID
                ))
            ).onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR
                ))
            ).bodyToMono(PullRequest[].class).block();
        return new Github(repository, branches, pullRequests);
    }
}
