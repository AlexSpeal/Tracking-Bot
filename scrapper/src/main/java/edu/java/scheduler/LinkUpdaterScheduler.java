package edu.java.scheduler;

import edu.java.LinkParser;
import edu.java.clients.BotClient;
import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.dto.Question;
import edu.java.dto.Repository;
import edu.java.dto.jdbc.LinkDto;
import edu.java.servises.jdbc.JdbcLinkService;
import edu.java.servises.jdbc.LinkUpdaterService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.SendUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
@SuppressWarnings("MultipleStringLiterals")
public class LinkUpdaterScheduler {
    private final JdbcLinkService jdbcLinkService;

    private final LinkUpdaterService linkUpdaterService;
    @Autowired
    private final BotClient botClient;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;

    @Scheduled(fixedDelayString = "#{scheduler.interval}")
    public void update() {
        OffsetDateTime time = OffsetDateTime.now();
        List<LinkDto> linkDtoList = linkUpdaterService.findOldLinksToUpdate(time.minusMinutes(1));
        for (var links : linkDtoList) {
            linkUpdaterService.check(links.getLinkId(), OffsetDateTime.now());
            if (links.getUrl().getHost().contains("github")) {
                gitHubUpdate(links);
            } else if (links.getUrl().getHost().contains("stackoverflow")) {
                stackOverflowUpdate(links);
            }

        }
    }

    public void gitHubUpdate(LinkDto link) {
        String owner = LinkParser.getGitHubOwner(link.getUrl());
        String repo = LinkParser.getGitHubRepo(link.getUrl());
        Repository repository = gitHubClient.getRep(owner, repo);
        sender(link, repository.pushedAt());

    }

    public void stackOverflowUpdate(LinkDto link) {
        long questions = LinkParser.getQuestionId(link.getUrl());
        Question question = stackOverflowClient.getQuestion(questions);
        sender(link, question.items().getFirst().lastEditDate());
    }

    public void sender(LinkDto link, OffsetDateTime data) {
        if (link.getUpdatedAt().isBefore(data)) {
            linkUpdaterService.update(link.getLinkId(), data);
            String str = link.getUrl().toString();
            botClient.updates(new SendUpdateRequest(link.getLinkId(), str,
                    "По вашей ссылке появилось новое обновление!",
                    jdbcLinkService.getLinkedChadId(link.getLinkId())
                )
            );
        }

    }
}
