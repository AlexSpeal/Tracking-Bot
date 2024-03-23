package edu.java.servises.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.clients.StackOverflowClient;
import edu.java.data.StackOverflowData;
import edu.java.dto.jdbc.stackOverflow.Question;
import io.swagger.v3.core.util.Json;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class StackOverflowHandler implements Handler<Question> {
    @Autowired
    private StackOverflowClient stackOverflowClient;

    @Override
    public String getData(Question dto) {
        try {
            return Json.mapper().writeValueAsString(
                new StackOverflowData(
                    dto.items().getFirst().isAnswered(), dto.items().getFirst().answerCount()
                )
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Question getInfoByUrl(URI url) {
        String[] splitUrl = url.toString().split("/");
        int id = Integer.parseInt(splitUrl[splitUrl.length - 2]);
        return stackOverflowClient.getQuestion(id);
    }
}
