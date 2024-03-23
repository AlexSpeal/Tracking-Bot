package edu.java.servises.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.clients.GitHubClient;
import edu.java.data.GitHubData;
import edu.java.dto.jdbc.github.Github;
import io.swagger.v3.core.util.Json;
import java.net.URI;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GithubHandler implements Handler<Github> {
    @Autowired
    private GitHubClient gitHubClient;

    @Override
    public String getData(Github dto) {
        try {
            return Json.mapper().writeValueAsString(
                new GitHubData(
                    dto.branches().length,
                    dto.pullRequests().length,
                    Arrays.hashCode(dto.branches()),
                    Arrays.hashCode(dto.pullRequests())
                )
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Github getInfoByUrl(URI url) {
        String[] splitUrl = url.toString().split("/");
        return gitHubClient.getRep(splitUrl[splitUrl.length - 2], splitUrl[splitUrl.length - 1]);
    }
}
