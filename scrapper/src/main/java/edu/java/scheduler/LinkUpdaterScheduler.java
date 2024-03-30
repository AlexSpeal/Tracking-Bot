package edu.java.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.clients.BotClient;
import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.data.GitHubData;
import edu.java.data.StackOverflowData;
import edu.java.dto.jdbc.LinkDto;
import edu.java.dto.jdbc.github.Github;
import edu.java.dto.jdbc.stackOverflow.Question;
import edu.java.servises.handlers.GithubHandler;
import edu.java.servises.handlers.StackOverflowHandler;
import edu.java.servises.interfaces.LinkService;
import edu.java.servises.interfaces.LinkUpdater;
import io.swagger.v3.core.util.Json;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dto.LinkParser;
import org.example.dto.request.SendUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
@SuppressWarnings("MultipleStringLiterals")
public class LinkUpdaterScheduler {
    @Qualifier("jpaLinkService")
    @Autowired
    private LinkService linkService;
    //@Qualifier("jdbcLinkUpdaterService")
    @Qualifier("jpaLinkUpdaterService")
    @Autowired
    private LinkUpdater linkUpdaterService;
    private final BotClient botClient;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final GithubHandler githubHandler;
    private final StackOverflowHandler stackOverflowHandler;

    private static final int DELAY = 5;

    @Scheduled(fixedDelayString = "#{scheduler.interval}")
    public void update() {
        OffsetDateTime time = OffsetDateTime.now();
        List<LinkDto> linkDtoList = linkUpdaterService.findOldLinksToUpdate(time.minusMinutes(DELAY));
        for (var links : linkDtoList) {
            linkUpdaterService.check(links.getLinkId(), OffsetDateTime.now());
            String description = switch (links.getType()) {
                case "github" -> gitHubUpdate(links);
                case "stackoverflow" -> stackOverflowUpdate(links);
                default -> "";
            };
            if (!description.isEmpty()) {
                botClient.updates(new SendUpdateRequest(
                    links.getLinkId(),
                    links.getUrl().toString(),
                    description,
                    linkService.getLinkedChadId(links.getLinkId())
                ));
            }
        }
    }

    public String gitHubUpdate(LinkDto link) {
        StringBuilder startDescription = new StringBuilder();
        startDescription.append("Обновление по ссылке ").append(link.getUrl()).append(" Пояснение:");
        StringBuilder description = new StringBuilder();
        String owner = LinkParser.getGitHubOwner(link.getUrl());
        String repo = LinkParser.getGitHubRepo(link.getUrl());
        Github repository = gitHubClient.getRep(owner, repo);
        OffsetDateTime timeUpdate = repository.repository().pushedAt();
        if (link.getUpdatedAt().isBefore(timeUpdate)) {
            linkUpdaterService.update(link.getLinkId(), timeUpdate, githubHandler.getData(repository));
            description.append(startDescription);
            try {
                GitHubData gitHubData = Json.mapper().readValue(link.getData(), GitHubData.class);
                if (repository.branches().length > gitHubData.numberBranches()) {
                    description.append(" В репозитории ").append(repository.repository().name())
                        .append(" появилась новая ветка.").append("\n");
                } else if (repository.branches().length < gitHubData.numberBranches()) {
                    description.append(" В репозитории ").append(repository.repository().name())
                        .append(" исчезла ветка.").append("\n");
                }
                if (repository.pullRequests().length > gitHubData.numberPullRequests()) {
                    description.append(" В репозитории ").append(repository.repository().name())
                        .append(" появился новый pull request ")
                        .append(repository.pullRequests()[0].title()).append(".\n");
                } else if (repository.pullRequests().length < gitHubData.numberPullRequests()) {
                    description.append(" В репозитории ").append(repository.repository().name())
                        .append(" удалили pull request.").append("\n");
                } else if (Arrays.hashCode(repository.pullRequests()) != gitHubData.pullRequestsHash()) {
                    description.append(" В репозитории ").append(repository.repository().name())
                        .append(" добавлен новый коммит ").append(" в pull request ")
                        .append(repository.pullRequests()[0])
                        .append(".\n");
                } else if (Arrays.hashCode(repository.branches()) != gitHubData.branchesHash()) {
                    description.append(" В репозитории ").append(repository.repository().name())
                        .append(" добавлен новый коммит в ветку.").append("\n");
                }
                if (description.compareTo(startDescription) == 0) {
                    description.append(" Произошло какое-то обновление");
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return description.toString();
    }

    public String stackOverflowUpdate(LinkDto link) {
        StringBuilder startDescription = new StringBuilder();
        startDescription.append("Обновление по ссылке ").append(link.getUrl()).append(" Пояснение:");
        StringBuilder description = new StringBuilder();
        long questions = LinkParser.getQuestionId(link.getUrl());
        Question question = stackOverflowClient.getQuestion(questions);
        OffsetDateTime timeUpdate = question.items().getFirst().lastEditDate();
        if (link.getUpdatedAt().isBefore(timeUpdate)) {
            description.append(startDescription);
            linkUpdaterService.update(link.getLinkId(), timeUpdate, stackOverflowHandler.getData(question));
            try {
                StackOverflowData stackOverflowData = Json.mapper().readValue(link.getData(), StackOverflowData.class);
                if (question.items().getFirst().answerCount() > stackOverflowData.answerCount()) {
                    description.append(" В вопросе ").append(questions).append(" появился новый ответ.").append("\n");
                }
                if (question.items().getFirst().isAnswered() && !stackOverflowData.isAnswered()) {
                    description.append(" По вопросу ").append(questions).append(" нашлось решение.").append("\n");
                } else if (!question.items().getFirst().isAnswered() && stackOverflowData.isAnswered()) {
                    description.append(" По вопросу ").append(questions).append(" отсутствует решение.").append("\n");
                }
                if (description.compareTo(startDescription) == 0) {
                    description.append(" Произошло какое-то обновление");
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return description.toString();
    }
}
